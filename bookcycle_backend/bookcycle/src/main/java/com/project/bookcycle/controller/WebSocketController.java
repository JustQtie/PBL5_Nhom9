package com.project.bookcycle.controller;

import com.project.bookcycle.exceptions.DataNotFoundException;
import com.project.bookcycle.model.Order;
import com.project.bookcycle.model.Product;
import com.project.bookcycle.model.User;
import com.project.bookcycle.response.OrderResponse;
import com.project.bookcycle.service.IProductService;
import com.project.bookcycle.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WebSocketController {
    private final IUserService userService;
    private final IProductService productService;
    private final SimpMessagingTemplate messagingTemplate;
    @MessageMapping("/order")
    public void handleOrderNotification(OrderResponse order) throws DataNotFoundException {
        User user = userService.getUser(order.getUserId());
        Product product = productService.getProduct(order.getProductId());
        String message = "Người dùng với đầu số điện thoại: '" + user.getPhoneNumber() + "' đã đặt đơn hàng với tiêu đề sách giáo trình cũ là '" + product.getName() + "'. Hãy nhấn vào thông báo này để xác nhận đơn hàng!";
        messagingTemplate.convertAndSend("/user/" + user.getId() + "/queue/notifications", message);
    }
}
