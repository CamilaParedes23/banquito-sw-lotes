package com.banquito.switchpagos.batch.config;

import com.banquito.switchpagos.batch.grpc.GrpcBatchGatewayService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcServerConfig {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcServerConfig.class);

    private final Integer grpcPort;
    private final GrpcBatchGatewayService grpcBatchGatewayService;
    private Server server;

    public GrpcServerConfig(
            @Value("${grpc.server.port}") Integer grpcPort,
            GrpcBatchGatewayService grpcBatchGatewayService) {
        this.grpcPort = grpcPort;
        this.grpcBatchGatewayService = grpcBatchGatewayService;
    }

    @PostConstruct
    public void start() throws IOException {
        server = ServerBuilder.forPort(grpcPort)
                .addService(grpcBatchGatewayService)
                .build()
                .start();
        LOG.info("batch-service gRPC server started on port {}", grpcPort);
    }

    @PreDestroy
    public void stop() {
        if (server != null) {
            server.shutdown();
            LOG.info("batch-service gRPC server stopped");
        }
    }
}
