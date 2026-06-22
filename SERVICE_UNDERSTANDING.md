# Entendimiento del servicio

`banquito-switch-batch-service` inicia el flujo automatico del Switch de Pagos Masivos.

## Integracion Core REST/Kong vigente

La integracion de fondeo global con Core ya usa REST via Kong:

```http
POST /api/v1/switch-core/payment-reservations
```

El `reservationUuid` devuelto por Core se guarda en los campos legacy `coreFundingId` de `"LOTE_PAGO_MASIVO"` y `"SOLICITUD_FONDEO_LOTE"`. Los eventos siguen publicando `coreFundingId` para no romper consumidores; desde esta fase ese valor es el `reservationUuid`.

Antes del fondeo, si el canal entrega `companyCustomerUuid`, el servicio usa `company-account-validation` de Core R9I
para validar empresa, cuenta matriz y cobertura del monto. El cliente Customer por RUC queda solo como fallback cuando
el UUID no viene informado. No existe dependencia funcional de `CORE_SWITCH_COMPANY_CUSTOMER_UUID`. `commissionAmount`
se estima con el tarifario por tramos vigente para que la reserva de Core y el cobro final de `billing-service` usen el
mismo subtotal de comision.

La integracion legacy Switch-Core por gRPC fue retirada. El unico gRPC conservado en este servicio es el contrato interno `batch_gateway.proto`.

## Flujo interno principal

1. Recibe un archivo CSV por `POST /api/v1/batches/upload`.
2. Valida estructura minima y registra el lote en `"LOTE_PAGO_MASIVO"`.
3. Responde `202 Accepted`.
4. En segundo plano ejecuta validacion profunda.
5. Si hay errores, guarda `"ERROR_VALIDACION_LOTE"`, marca el lote `RECHAZADO` y termina.
6. Si el lote es valido, resuelve y valida la empresa en Core Customer usando el RUC del lote.
7. Si la empresa no existe, esta inactiva, no tiene pagos masivos o Customer falla, marca el lote `RECHAZADO`.
8. Si la empresa y su cuenta matriz son validas, solicita fondeo global con su `customerUuid` mediante `CoreBankingClient`.
9. Si Core rechaza o falla, marca el lote `RECHAZADO` y no publica eventos.
10. Si Core aprueba, marca el lote `FONDEADO`, fragmenta las lineas y publica `PaymentLineRequestedEvent`.
11. Deja las lineas en `PUBLICADA` y el lote en `PROCESANDO_LINEAS`.

## Paquetes importantes

- `controller`: expone endpoints HTTP temporales de carga y consulta para pruebas controladas mientras el Gateway no enruta.
- `service`: define interfaces de aplicacion.
- `service.impl`: implementa registro, procesamiento asincrono y publicacion RabbitMQ.
- `client`: encapsula llamada REST al Core via Kong para reserva/fondeo global.
- `mapper`: parsea archivo y convierte entidades a DTOs/eventos.
- `model`: entidades JPA propias del batch-service.
- `repository`: acceso Spring Data JPA a tablas locales.
- `config`: configuracion async, RabbitMQ, REST/Kong y gRPC interno.
- `grpc`: servidor gRPC interno historico del Switch.

## Clases principales

- `BatchController`: endpoints permitidos para la fase Batch.
- `BatchServiceImpl`: validacion temprana, sanitizacion de nombre y registro inicial.
- `BatchProcessingServiceImpl`: validacion profunda, fondeo, fragmentacion y publicacion.
- `RabbitPaymentLineEventPublisher`: publicacion RabbitMQ con exchange/routing key configurables.
- `CoreBankingClient`: integracion REST con Core via Kong para `POST /payment-reservations`.
- `CoreCustomerClient`: consulta la empresa por RUC mediante Customer/Core via Kong.
- `GrpcBatchGatewayService`: adapter gRPC que delega en `BatchService`.
- `GrpcServerConfig`: arranca el servidor gRPC interno en `grpc.server.port`.
- `BatchFileParser`: parser del CSV `H/D/T`.

## Tablas propias

- `"LOTE_PAGO_MASIVO"`: estado general del lote.
- `"ARCHIVO_CARGADO"`: metadata del archivo recibido.
- `"ERROR_VALIDACION_LOTE"`: errores funcionales de validacion profunda.
- `"SOLICITUD_FONDEO_LOTE"`: solicitud y respuesta de fondeo global.
- `"LINEA_PAGO_LOTE"`: lineas fragmentadas y trazabilidad del evento publicado.

## Eventos propios

Publica `PaymentLineRequestedEvent` despues de fondeo aprobado. El evento incluye `eventId`, `batchId`, `lineId`, `correlationId`, `sourceService`, datos de beneficiario, routing code, monto, moneda, referencia y `coreFundingId`.

## Integraciones externas

- Core Customer: REST via Kong `GET /api/v1/customers/by-identification/{companyRuc}`.
- Core Account: REST via Kong `GET /api/v1/accounts/{sourceAccountNumber}`.
- Core Bancario: REST via Kong `POST /api/v1/switch-core/payment-reservations`.
- gRPC interno: `BatchGatewayService` para carga y consultas historicas.
- RabbitMQ: exchange `switch.batch.exchange`, routing key `payment.line.requested`, queue local de routing para pruebas.

## Que no hace

No acredita beneficiarios, no procesa lineas On-Us, no genera archivo Off-Us, no calcula comisiones, no genera reportes, no implementa seguridad y no expone endpoints manuales para validar/fondear/fragmentar.

## Pendientes

- Definir estrategia final de DLQ/reintentos para RabbitMQ.
