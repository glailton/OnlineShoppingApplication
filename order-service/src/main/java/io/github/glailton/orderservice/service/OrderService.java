package io.github.glailton.orderservice.service;

import io.github.glailton.orderservice.model.Order;
import io.github.glailton.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final StreamBridge streamBridge;

    public void save(Order order) {
        orderRepository.save(order);

        log.info("Sending Order Details to Notification Service");
        streamBridge.send("notificationEventSupplier-out-0", order.getId());
    }
}
