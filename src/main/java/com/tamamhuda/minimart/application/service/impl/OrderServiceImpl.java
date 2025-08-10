package com.tamamhuda.minimart.application.service.impl;

import com.tamamhuda.minimart.application.dto.OrderDto;
import com.tamamhuda.minimart.application.dto.OrderRequestDto;
import com.tamamhuda.minimart.application.mapper.OrderMapper;
import com.tamamhuda.minimart.application.mapper.PaymentMapper;
import com.tamamhuda.minimart.application.service.OrderService;
import com.tamamhuda.minimart.domain.entity.*;
import com.tamamhuda.minimart.domain.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
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
    private final UserServiceImpl userService;
    private final PaymentMapper paymentMapper;


    private Order createOrder(User user, List<OrderItem> orderItems, BigDecimal totalAmount) {
        Order order = new Order();

        order.setTotalPrice(totalAmount);

        for (OrderItem orderItem : orderItems) {
            order.addItem(orderItem);
        }

        user.addOrder(order);

        return orderRepository.save(order);
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
    public OrderDto checkout(User user, OrderRequestDto request) {
           User managedUser = userService.getUserByUsername(user.getUsername());
           List<UUID> cartItemsIds = request.getCartItemIds();
           List<CartItem> cartItems = cartService.getCartItemsByIds(cartItemsIds);

           List<OrderItem> orderItems = mapCartItemToOrderItems(cartItems);

           BigDecimal totalAmount = calculateTotalAmount(orderItems);

           Order order = createOrder(managedUser, orderItems, totalAmount);

           cartService.removeCartItems(cartItems);

           return orderMapper.toDto(order);

    }

    @Override
    public Order findById(UUID orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
    }

    @Transactional
    public List<OrderDto> getAllUserOrders(User user) {
            User existingUser = userService.getUserByUsername(user.getUsername());
            List<Order> orders = existingUser.getOrders();
            return orderMapper.toDto(orders);
    }


    @Override
    public OrderDto getOrderById(UUID orderId) {
        Order order = findById(orderId);
        return orderMapper.toDto(order);
    }
}
