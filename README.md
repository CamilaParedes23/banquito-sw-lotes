# banquito-switch-batch-service

## Integracion Core REST/Kong vigente

Este servicio ya crea el fondeo/reserva global por Core REST via Kong/API Manager.

Operacion usada:

```http
POST http://localhost:8000/api/v1/switch-core/payment-reservations
```

El resultado real de fondeo/reserva se persiste en el campo legacy `coreFundingId`. En la nueva integracion, `coreFundingId` contiene el `reservationUuid` devuelto por Core.

La integracion legacy Switch-Core por gRPC fue retirada. El gRPC que permanece en este servicio corresponde solo al contrato interno `batch_gateway.proto`.

El request a Core envia `commissionAmount` estimado con el mismo tarifario por tramos que usa `billing-service`.
Esto mantiene consistente la reserva de Core R9I con el cobro final de comision.

Autenticacion vigente:

- Si `CORE_KONG_AUTH_TOKEN` esta configurado, se usa como override manual.
- Si no hay override y `CORE_KONG_CLIENT_TOKEN_ENABLED=true`, el servicio obtiene `client-token` por `POST /api/v1/auth/client-token`, lo cachea en memoria y lo renueva antes de expirar.

SFTP sera una entrada futura del Switch y debe reutilizar este mismo flujo de batch. Core no debe saber si el lote llego por WEB, SFTP u otro canal.

## Responsabilidad

Servicio responsable de recibir lotes de pagos masivos por HTTP publico, registrar metadata, ejecutar validacion profunda, solicitar fondeo global al Core Bancario por REST via Kong y publicar lineas `PaymentLineRequestedEvent` en RabbitMQ solo cuando el lote queda fondeado.

No acredita beneficiarios, no genera compensacion Off-Us, no calcula comisiones y no genera reportes finales.

## Ejecucion local

Desde la raiz del workspace:

```powershell
docker compose build batch-service
docker compose up -d postgres rabbitmq batch-service
```

Puerto por defecto: `8081`.

Health:

```http
GET http://localhost:8081/actuator/health
```

## Variables de entorno

- `SERVER_PORT`
- `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`
- `RABBITMQ_HOST`, `RABBITMQ_PORT`, `RABBITMQ_USERNAME`, `RABBITMQ_PASSWORD`
- `RABBIT_EXCHANGE_BATCH`
- `RABBIT_QUEUE_ROUTING_PAYMENT_LINES`
- `RABBIT_ROUTING_KEY_PAYMENT_LINE_REQUESTED`
- `CORE_KONG_BASE_URL`
- `CORE_KONG_SWITCH_CORE_PATH`
- `CORE_KONG_PAYMENT_RESERVATIONS_PATH`
- `CORE_KONG_CUSTOMERS_BY_IDENTIFICATION_PATH`
- `CORE_KONG_ACCOUNT_BY_NUMBER_PATH`
- `CORE_KONG_AUTH_TOKEN`
- `CORE_KONG_CLIENT_TOKEN_ENABLED`
- `CORE_KONG_CLIENT_ID`
- `CORE_KONG_CLIENT_SECRET`
- `CORE_KONG_REQUIRED_SCOPE`
- `CORE_KONG_CLIENT_TOKEN_PATH`
- `CORE_KONG_CLIENT_TOKEN_REFRESH_SKEW_SECONDS`
- `CORE_KONG_CONNECT_TIMEOUT_MS`
- `CORE_KONG_READ_TIMEOUT_MS`
- `CORE_SWITCH_DEFAULT_ACCOUNTING_DATE`
- `CORE_SWITCH_CHANNEL`
- `GRPC_SERVER_PORT`
- `SWITCH_FILES_OUTPUT_DIRECTORY`

Valores REST por defecto:

```properties
core.kong.base-url=${CORE_KONG_BASE_URL:http://localhost:8000}
core.kong.switch-core-path=${CORE_KONG_SWITCH_CORE_PATH:/api/v1/switch-core}
core.kong.payment-reservations-path=${CORE_KONG_PAYMENT_RESERVATIONS_PATH:/payment-reservations}
core.kong.customers-by-identification-path=${CORE_KONG_CUSTOMERS_BY_IDENTIFICATION_PATH:/api/v1/customers/by-identification/{identification}}
core.kong.account-by-number-path=${CORE_KONG_ACCOUNT_BY_NUMBER_PATH:/api/v1/accounts/{accountNumber}}
core.kong.auth-token=${CORE_KONG_AUTH_TOKEN:}
core.kong.client-token.enabled=${CORE_KONG_CLIENT_TOKEN_ENABLED:true}
core.kong.client-id=${CORE_KONG_CLIENT_ID:switch-pagos-internos-service}
core.kong.client-secret=${CORE_KONG_CLIENT_SECRET:}
core.kong.required-scope=${CORE_KONG_REQUIRED_SCOPE:core.reserve.consume}
core.kong.client-token-path=${CORE_KONG_CLIENT_TOKEN_PATH:/api/v1/auth/client-token}
core.kong.client-token-refresh-skew-seconds=${CORE_KONG_CLIENT_TOKEN_REFRESH_SKEW_SECONDS:60}
core.kong.connect-timeout-ms=${CORE_KONG_CONNECT_TIMEOUT_MS:5000}
core.kong.read-timeout-ms=${CORE_KONG_READ_TIMEOUT_MS:30000}
core.switch.default-accounting-date=${CORE_SWITCH_DEFAULT_ACCOUNTING_DATE:}
core.switch.channel=${CORE_SWITCH_CHANNEL:SWITCH_API}
```

`CORE_SWITCH_COMPANY_CUSTOMER_UUID` ya no se usa. Antes de solicitar fondeo, el servicio consulta Customer via Kong con el RUC persistido del lote, valida `customerUuid`, identificacion, estado `ACTIVO` y `massPaymentsEnabled`, y usa el UUID resuelto en `payment-reservations`.

## Base de datos

Usa `SWITCH_LOTES_DB`. La base se inicializa localmente con:

```text
src/main/resources/db/init/001_create_batch_tables.sql
```

Hibernate se mantiene en `spring.jpa.hibernate.ddl-auto=validate`; no genera ni actualiza tablas.

Tablas propias:

- `"LOTE_PAGO_MASIVO"`
- `"ARCHIVO_CARGADO"`
- `"ERROR_VALIDACION_LOTE"`
- `"SOLICITUD_FONDEO_LOTE"`
- `"LINEA_PAGO_LOTE"`

## Endpoints

Estos endpoints son HTTP publicos o de prueba controlada mientras el API Gateway no enruta el flujo completo. No son contratos REST internos entre microservicios.

```http
POST /api/v1/batches/upload
GET /api/v1/batches?companyRuc={companyRuc}&page={page}&size={size}
GET /api/v1/batches/{batchId}
GET /api/v1/batches/{batchId}/state-history
GET /api/v1/batches/{batchId}/validation-errors
GET /api/v1/batches/{batchId}/lines
```

En el endpoint HTTP publico `POST /api/v1/batches/upload`, `companyRuc` es obligatorio. Si falta, se responde `400` con `code=COMPANY_RUC_REQUIRED`. El valor debe venir de Core/Auth/Customer y coincidir con el RUC de la cabecera; si no coincide, se responde `COMPANY_RUC_MISMATCH`. El contrato gRPC interno se conserva sin cambios.

El endpoint de carga responde `202 Accepted` despues de registrar el lote. La validacion profunda, fondeo, fragmentacion y publicacion se ejecutan de forma asincrona.

Despues de la validacion profunda, `batch-service` consulta `GET /api/v1/customers/by-identification/{companyRuc}`. Luego consulta `GET /api/v1/accounts/{sourceAccountNumber}` para validar numero, titularidad por `customerUuid`, estado `ACTIVA` y bandera `massPaymentMainAccount=true`. Los rechazos quedan en `rejectionReason`; no se crea reserva ni se publican lineas.

## Exposicion por Kong local del Switch

En desarrollo local, Kong DB-less publica estas mismas rutas en:

```text
http://localhost:8010
```

Upstream interno:

```text
http://banquito-switch-batch-service:8081
```

Rutas publicas:

```http
POST /api/v1/batches/upload
GET /api/v1/batches?companyRuc={companyRuc}&page={page}&size={size}
GET /api/v1/batches/{batchId}
GET /api/v1/batches/{batchId}/state-history
GET /api/v1/batches/{batchId}/validation-errors
GET /api/v1/batches/{batchId}/lines
```

La entrada publica vigente del Switch es Kong Switch en `http://localhost:8010`. Este servicio puede seguir exponiendo REST directo para diagnostico tecnico, pero no como gateway publico.

## gRPC interno

Expone un servidor gRPC interno en `grpc.server.port` como contrato interno historico del Switch:

```text
banquito.switchpagos.batch.v1.BatchGatewayService
```

Operaciones:

- `UploadBatch`
- `GetBatchStatus`
- `GetBatchValidationErrors`
- `GetBatchLines`

El adapter gRPC delega en `BatchService`; no contiene reglas de negocio propias.

## Eventos

Publica `PaymentLineRequestedEvent` en:

- Exchange: `rabbit.exchange.batch`
- Routing key: `rabbit.routing-key.payment-line-requested`
- Queue enlazada localmente: `rabbit.queue.routing.payment-lines`

Regla clave: no se publica ninguna linea si la validacion profunda falla o si Core REST/Kong rechaza/falla el fondeo global.

El evento incluye `batchTotalLines` y `batchControlAmount` para que `reporting-service` pueda consolidar resultados por eventos sin consultar la base de datos del batch-service.

El evento sigue publicando `coreFundingId` por compatibilidad. Desde esta fase, su valor corresponde al `reservationUuid` de Core.

## Prueba manual basica

Para probar contra Core real/Kong, configurar al menos:

```powershell
$env:CORE_KONG_BASE_URL="http://kong-gateway:8000"
$env:CORE_KONG_CLIENT_SECRET="<secret-tecnico>"
$env:CORE_SWITCH_DEFAULT_ACCOUNTING_DATE="2026-06-05"
```

Para Docker Compose, copiar `.env.example` a `.env` y colocar el secreto real en `CORE_KONG_CLIENT_SECRET`. El Switch recibe el secreto en texto y Core conserva su hash. `.env` no debe versionarse ni el secreto debe aparecer en logs.

Si se ejecuta dentro de Docker Compose junto al stack Core, `CORE_KONG_BASE_URL` debe usar la red externa compartida `banquito-net` y apuntar a `http://kong-gateway:8000`. `host.docker.internal:8000` queda solo como alternativa local si no se usa la red compartida.

Token tecnico demo confirmado para validacion local con Core. El servicio lo obtiene automaticamente si `CORE_KONG_CLIENT_SECRET` esta configurado; el siguiente comando queda solo como referencia de contrato:

```powershell
$body = @{
  clientId = "switch-pagos-internos-service"
  clientSecret = "<demo-password>"
  requiredScope = "core.reserve.create"
} | ConvertTo-Json

$tokenResponse = Invoke-RestMethod `
  -Method Post `
  -Uri "http://localhost:8000/api/v1/auth/client-token" `
  -ContentType "application/json" `
  -Body $body

$tokenResponse.accessToken
```

Para el seed demo de Core, la empresa de pagos masivos validada por API es `1792103456001`; Customer devuelve dinamicamente `customerUuid=3f26a20e-c149-5666-84b9-7c8ce0ed2712`. La cuenta matriz usada es `0010000010599`.

Validacion final: el lote `b46e4239-375e-43da-baea-9caf783a579a` consulto Customer y Account con HTTP `200`, uso la reserva `76c8d2c6-1acb-4dd5-a2b5-ada859fd108a` y completo el flujo financiero. La cuenta ajena `0040000010608` fue rechazada antes del fondeo con `SOURCE_ACCOUNT_NOT_OWNED_BY_COMPANY`.

Subir lote valido:

```powershell
$response = Invoke-RestMethod -Method Post `
  -Uri "http://localhost:8081/api/v1/batches/upload" `
  -Form @{ file = Get-Item ".\docs\examples\files\valid_mixed_batch.csv"; channel = "WEB"; receivedBy = "local" }

$response.batchId
```

Subir lote valido via Kong local del Switch:

```powershell
$response = Invoke-RestMethod -Method Post `
  -Uri "http://localhost:8010/api/v1/batches/upload" `
  -Form @{ file = Get-Item ".\docs\examples\files\valid_core_integration_batch.csv"; companyRuc = "1792103456001"; channel = "SWITCH_API"; receivedBy = "kong-local" }
```

Consultar estado:

```powershell
Invoke-RestMethod -Method Get -Uri "http://localhost:8010/api/v1/batches/$($response.batchId)"
```

Consultar errores de validacion:

```powershell
Invoke-RestMethod -Method Get -Uri "http://localhost:8010/api/v1/batches/$($response.batchId)/validation-errors"
```

## Decisiones tecnicas

- El archivo se parsea como CSV simple con registros `H`, `D` y `T`.
- El hash/codigo de seguridad del pie se usa como `fileHash` para control de duplicidad.
- La duplicidad se evalua por nombre de archivo + hash contra lotes fondeados o en procesamiento de los ultimos 30 dias.
- El catalogo local de routing codes permitido en esta fase es `10`, `30`, `32`, `35`.
- La empresa se resuelve dinamicamente por RUC mediante `CoreCustomerClient`; no existe UUID global de empresa para fondeo.
- El fondeo global usa `CoreBankingClient` REST via Kong.
- El contrato gRPC interno conservado se define en `src/main/proto/batch_gateway.proto`.
- `commissionAmount` se estima con tarifario por tramos antes del fondeo para que Core R9I reserve y cobre el mismo valor.
- La cuenta matriz se valida mediante `company-account-validation` cuando el canal entrega `companyCustomerUuid`; el
  cliente Customer legacy queda solo como fallback cuando el UUID no viene informado.
