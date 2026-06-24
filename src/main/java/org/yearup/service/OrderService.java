package org.yearup.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.*;
import org.yearup.repository.OrderLineItemRepository;
import org.yearup.repository.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ShoppingCartService shoppingCartService;
    private final OrderLineItemRepository orderLineItemRepository;
    private final ProfileService profileService;

    public OrderService(OrderRepository orderRepository, ShoppingCartService shoppingCartService,
                        OrderLineItemRepository orderLineItemRepository, ProfileService profileService) {
        this.orderRepository = orderRepository;
        this.shoppingCartService = shoppingCartService;
        this.orderLineItemRepository = orderLineItemRepository;
        this.profileService = profileService;

    }

    public Order checkout(int userId) {

        ShoppingCart shoppingCart = shoppingCartService.getByUserId(userId);

        if (shoppingCart.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is nothing to checkout");
        }

        Order newOrder = buildOrder(userId);
        addLineItems(newOrder, shoppingCart);

        shoppingCartService.clear(userId);

        return newOrder;
    }

    // Helper method to add in all order details
    private Order buildOrder(int userId) {

        Order order = new Order();

        order.setUserId(userId);
        order.setDate(LocalDateTime.now());

        Profile userProfile = profileService.getProfileById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Profile not found"));;

        order.setAddress(userProfile.getAddress());
        order.setCity(userProfile.getCity());
        order.setState(userProfile.getState());
        order.setZip(userProfile.getZip());
        order.setShippingAmount(BigDecimal.valueOf(15));

        orderRepository.save(order);
        return order;
    }

    // Helper method to add in the order line items
    private void addLineItems(Order order, ShoppingCart shoppingCart) {
        for (ShoppingCartItem cartItem : shoppingCart.getItems().values()) {

            OrderLineItem lineItem = new OrderLineItem();

            lineItem.setOrderId(order.getOrderId());
            lineItem.setProductId(cartItem.getProduct().getProductId());
            lineItem.setQuantity(cartItem.getQuantity());
            lineItem.setSalesPrice(BigDecimal.valueOf(cartItem.getProduct().getPrice()));
            lineItem.setDiscount(BigDecimal.valueOf(cartItem.getDiscountPercent()));

            orderLineItemRepository.save(lineItem);

        }
    }
}
