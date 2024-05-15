package com.project.bookcycle.service;

import com.project.bookcycle.dto.OrderDetailDTO;
import com.project.bookcycle.exceptions.DataNotFoundException;
import com.project.bookcycle.model.OrderDetail;

import java.util.List;

public interface IOrderDetailService {
    OrderDetail createOrderDetail(OrderDetailDTO orderDTO) throws DataNotFoundException;
    OrderDetail getOrderDetailById(long id) throws DataNotFoundException;
    List<OrderDetail> getOrderDetailByOrderId(long orderId) throws DataNotFoundException;
    OrderDetail updateOrderDetail(long id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException;
    void deleteOrderDetail(long id) throws DataNotFoundException;
}
