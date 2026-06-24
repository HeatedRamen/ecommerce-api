package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.Order;
import org.yearup.models.ShoppingCart;
import org.yearup.repository.OrderRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ShoppingCartService shoppingCartService;

    public OrderService(OrderRepository orderRepository, ShoppingCartService shoppingCartService){
        this.orderRepository = orderRepository;
        this.shoppingCartService = shoppingCartService;
    }

    public Order makeOrder(int userId){

        ShoppingCart shoppingCart = shoppingCartService.getByUserId(userId);

        Order newOrder = new Order();

        shoppingCart = shoppingCartService.clear(userId)
        return orderRepository.save(newOrder);
    }

}
