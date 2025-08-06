package com.tamamhuda.minimart.application.service.impl;

import com.tamamhuda.minimart.application.dto.InvoiceDto;
import com.tamamhuda.minimart.application.dto.OrderDto;
import com.tamamhuda.minimart.application.dto.OrderRequestDto;
import com.tamamhuda.minimart.application.dto.PaymentDto;
import com.tamamhuda.minimart.application.mapper.OrderMapper;
import com.tamamhuda.minimart.application.service.OrderService;
import com.tamamhuda.minimart.domain.entity.*;
import com.tamamhuda.minimart.domain.enums.InvoiceStatus;
import com.tamamhuda.minimart.domain.enums.PaymentStatus;
import com.tamamhuda.minimart.domain.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CartServiceImpl cartService;
    private final PaymentServiceImpl paymentService;
    private final InvoiceServiceImpl invoiceService;
    private final UserServiceImpl userService;


    private Order createOrder(User user, List<OrderItem> orderItems, Payment payment, Invoice invoice, BigDecimal totalAmount) {
        Order order = new Order();

        order.AttachInvoice(invoice);
        order.AttachPayment(payment);
        order.setTotalPrice(totalAmount);

        for (OrderItem orderItem : orderItems) {
            order.AddItem(orderItem);
        }

        user.addOrder(order);

        return orderRepository.save(order);
    }

    private Order findOrderById(UUID orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
    }

    private Payment createPayment(BigDecimal totalAmount) {
        PaymentDto paymentDto = PaymentDto.builder()
                .totalAmount(totalAmount)
                .status(PaymentStatus.PENDING.name())
                .build();

        return paymentService.create(paymentDto);
    }

    private Invoice createInvoice(User user, BigDecimal totalAmount) {
        InvoiceDto invoiceDto = InvoiceDto.builder()
                .customerName(user.getFullName())
                .customerEmail(user.getEmail())
                .totalAmount(totalAmount)
                .status(InvoiceStatus.UNPAID.name())
                .issuedDate(Instant.now())
                .build();

        return invoiceService.create(invoiceDto);

    }


    private BigDecimal calculateTotalAmount(List<OrderItem> orderItems) {

        return orderItems.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<OrderItem> mapCartItemToOrderItems(List<CartItem> cartItems) {
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();

            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Product with id " + product.getId() + " stock not enough"
                );
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));

            orderItems.add(orderItem);
        }

        return orderItems;
    }


    @Override
    @Transactional
    public ResponseEntity<OrderDto> checkout(User user, OrderRequestDto request) {
           User managedUser = userService.getUserByUsername(user.getUsername());
           List<UUID> cartItemsIds = request.getCartItemIds();
           List<CartItem> cartItems = cartService.getCartItemsByIds(cartItemsIds);

           List<OrderItem> orderItems = mapCartItemToOrderItems(cartItems);

           BigDecimal totalAmount = calculateTotalAmount(orderItems);

           Payment payment = createPayment(totalAmount);

           Invoice invoice = createInvoice(user, totalAmount);

           Order order = createOrder(managedUser, orderItems, payment, invoice, totalAmount);

           order.getOrderItems()
                           .forEach(orderItem ->
                               log.info(String.valueOf(orderItem.getId()))
                           );
           cartService.removeCartItems(cartItems);

           return ResponseEntity.status(HttpStatus.CREATED).body(orderMapper.toDto(order));

    }

    @Transactional
    public ResponseEntity<List<OrderDto>> getAllUserOrders(User user) {
            User existingUser = userService.getUserByUsername(user.getUsername());
            List<Order> orders = existingUser.getOrders();

            return ResponseEntity.status(HttpStatus.OK).body(orderMapper.toDto(orders));
    }

    @Override
    public ResponseEntity<OrderDto> getOrderById(UUID orderId) {
        Order order = findOrderById(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(orderMapper.toDto(order));
    }
}
