package com.laffuste.ordo.validation.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laffuste.ordo.validation.application.in.OrderValidationUseCase;
import com.laffuste.ordo.validation.domain.Order;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;

@Slf4j
public class SimpleController {

    private static final ObjectMapper mapper = new ObjectMapper();
    private final ValidationHandler handler;
    private HttpServer server;

    public SimpleController(OrderValidationUseCase orderValidationUseCase) {
        this.handler = new ValidationHandler(orderValidationUseCase);
    }

    public void start(int port) throws Exception {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/validate", handler);
        server.setExecutor(null); // creates a default executor
        server.start();
        log.info("server started in port {}", port);
    }

    public void stop() {
        server.stop(0);
    }

    @Slf4j
    private static class ValidationHandler implements HttpHandler {
        private final OrderValidationUseCase orderValidationUseCase;

        public ValidationHandler(OrderValidationUseCase orderValidationUseCase) {
            this.orderValidationUseCase = orderValidationUseCase;
        }

        @Override
        public void handle(HttpExchange t) throws IOException {
            InputStream is = t.getRequestBody();
            if (t.getRequestMethod().equals("POST")) {
                Order order = mapper.readValue(is, Order.class);

                List<String> errors = orderValidationUseCase.validate(order);
                String jsonInString = mapper.writeValueAsString(errors);
                response(t, 200, jsonInString);
                return;
            }
            response(t, 401, "Bad request");
        }

        private void response(HttpExchange t, int responseCode, String response) throws IOException {
            t.sendResponseHeaders(responseCode, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
