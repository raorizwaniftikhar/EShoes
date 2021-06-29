package com.java.shoes.service;

import java.util.List;

import com.java.shoes.domain.Order;
import com.java.shoes.domain.Payment;
import com.java.shoes.domain.Shipping;
import com.java.shoes.domain.ShoppingCart;
import com.java.shoes.domain.User;

public interface OrderService {

	Order createOrder(ShoppingCart shoppingCart, Shipping shippingAddress, Payment payment, User user);
	
	List<Order> findByUser(User user);
	
	Order findOrderWithDetails(Long id);
}
