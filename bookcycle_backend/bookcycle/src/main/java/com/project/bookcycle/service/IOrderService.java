package com.project.bookcycle.service;

import com.project.bookcycle.dto.OrderDTO;
import com.project.bookcycle.exceptions.DataNotFoundException;
import com.project.bookcycle.model.Order;
import com.project.bookcycle.model.Product;
import com.project.bookcycle.repository.OrderRepository;
import com.project.bookcycle.response.OrderResponse;
import com.project.bookcycle.response.ProductResponse;

import java.util.List;

public interface IOrderService {
    Order createOrder(OrderDTO orderDTO);
    Order getOrderById(long id) throws DataNotFoundException;
    Order updateOrder(long id, OrderDTO orderDTO) throws DataNotFoundException;
    void deleteOrder(long id) throws DataNotFoundException;
    List<OrderResponse> findOrderByUser(long userId);
    List<OrderResponse> findGetOrders();
}
