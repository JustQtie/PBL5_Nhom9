package com.project.bookcycle.service;

import com.project.bookcycle.dto.OrderDTO;
import com.project.bookcycle.exceptions.DataNotFoundException;
import com.project.bookcycle.model.Order;
import com.project.bookcycle.model.OrderStatus;
import com.project.bookcycle.model.Product;
import com.project.bookcycle.model.User;
import com.project.bookcycle.repository.OrderRepository;
import com.project.bookcycle.repository.ProductRepository;
import com.project.bookcycle.repository.UserRepository;
import com.project.bookcycle.response.OrderResponse;
import com.project.bookcycle.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    @Override
    public Order createOrder(OrderDTO orderDTO) {
        try {
            User user = userRepository.findById(orderDTO.getUserId())
                    .orElseThrow(()-> new DataNotFoundException("Cannot find user with id " + orderDTO.getUserId()));
            Product product = productRepository.findById(orderDTO.getProductId())
                    .orElseThrow(()-> new DataNotFoundException("Cannot find product with id " + orderDTO.getProductId()));
            // Convert orderDTO => Order
            // dùng thư viện ModelMapper để ánh xạ
            Order order = Order.builder()
                    .product(product)
                    .user(user)
                    .status(orderDTO.getStatus())
                    .shippingAddress(user.getAddress())
                    .active(true)
                    .build();
            orderRepository.save(order);
            return order;
        } catch (DataNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Order getOrderById(long id) throws DataNotFoundException {
        return orderRepository.findById(id)
                .orElseThrow(()->new DataNotFoundException("Cannot find order with id "+ id));
    }


    @Override
    public Order updateOrder(long id, OrderDTO orderDTO) throws DataNotFoundException {
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(()->new DataNotFoundException("Cannot find user with user's id " + orderDTO.getUserId()));
        Product product = productRepository.findById(orderDTO.getProductId())
                .orElseThrow(()->new DataNotFoundException("Cannot find product with product's id " + orderDTO.getProductId()));
        Order existingOrder = getOrderById(id);
        existingOrder.setProduct(product);
        existingOrder.setUser(user);
        existingOrder.setStatus(orderDTO.getStatus());
        existingOrder.setShippingAddress(user.getAddress());
        existingOrder.setNumberOfProducts(orderDTO.getNumberOfProduct());
        existingOrder.setTotalMoney(orderDTO.getTotalMoney());
        existingOrder.setPaymentMethod(orderDTO.getPaymentMethod());
        existingOrder.setActive(true);

        orderRepository.save(existingOrder);
        return existingOrder;
    }

    @Override
    public void deleteOrder(long id) throws DataNotFoundException {
        Order order = orderRepository.findById(id)
                .orElseThrow(()->new DataNotFoundException("Cannot find order's with id "+ id));
        order.setActive(false);
        orderRepository.save(order);
    }

    @Override
    public List<OrderResponse> findOrderByUser(long userId) {
        return orderRepository.findOrderByUser(userId)
                .stream()
                .map(OrderResponse::convertFromOrder)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> findGetOrders() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::convertFromOrder)
                .collect(Collectors.toList());
    }

}
