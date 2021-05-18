package io.github.glailton.orderservice.controller;

import io.github.glailton.orderservice.client.InventoryClient;
import io.github.glailton.orderservice.dto.OrderDto;
import io.github.glailton.orderservice.model.Order;
import io.github.glailton.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreaker;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrderService orderService;
    private final InventoryClient inventoryClient;
    private final Resilience4JCircuitBreakerFactory circuitBreakerFactory;
    private final StreamBridge streamBridge;
    private final ExecutorService traceableExecutorService;

    @PostMapping
    public String placeOrder(@RequestBody OrderDto orderDto) {
        circuitBreakerFactory.configureExecutorService(traceableExecutorService);
        Resilience4JCircuitBreaker circuitBreaker = circuitBreakerFactory.create("inventory");
        Supplier<Boolean> booleanSupplier = () -> orderDto.getOrderLineItemsList().stream()
                .allMatch(orderLineItems -> {
                    log.info("Making call to Inventory Service{}", orderLineItems.getSkuCode());
                    return inventoryClient.checkStock(orderLineItems.getSkuCode());
                });

        boolean allProductsIsInStock = circuitBreaker.run(booleanSupplier, throwable -> handleErrorCase());

        if(allProductsIsInStock) {
            Order order = new Order();
            order.setOrderLineItems(orderDto.getOrderLineItemsList());
            order.setOrderNumbers(UUID.randomUUID().toString());

            orderService.save(order);

            log.info("Sending Order Details to Notification Service");
            streamBridge.send("notificationEventSupplier-out-0", MessageBuilder.withPayload(order.getId()).build());

            return "Order Place Successfully";
        } else {
            return "Order Failed, One of the products in the order is not in stock";
        }

    }

    private Boolean handleErrorCase() {
        return false;
    }
}
