package com.project.bookcycle.controller;

import com.project.bookcycle.dto.NotifyDTO;
import com.project.bookcycle.dto.OrderDTO;
import com.project.bookcycle.dto.ProductDTO;
import com.project.bookcycle.exceptions.DataNotFoundException;
import com.project.bookcycle.model.*;
import com.project.bookcycle.response.NotifyResponse;
import com.project.bookcycle.response.OrderResponse;
import com.project.bookcycle.service.INotifyService;
import com.project.bookcycle.service.IOrderService;
import com.project.bookcycle.service.IProductService;
import com.project.bookcycle.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WebSocketController {
    private final IUserService userService;
    private final IProductService productService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final INotifyService notifyService;
    private final IOrderService orderService;

    @MessageMapping("/notify")
    public void handleOrderNotification(OrderResponse order) throws DataNotFoundException {
        User user = userService.getUser(order.getUserId());
        Product product = productService.getProduct(order.getProductId());
        String message = "Người dùng với đầu số điện thoại: '" + user.getPhoneNumber() + "' đã đặt đơn hàng với tiêu đề sách giáo trình cũ là '" + product.getName() + "'. Hãy nhấn vào thông báo này để xác nhận đơn hàng!";
        System.out.println(message);
        NotifyDTO notifyDTO = NotifyDTO.builder()
                .userId(product.getUser().getId())
                .orderId(order.getId())
                .content(message)
                .status(NotifyStatus.NOT_RESPONDED)
                .build();
        notifyService.createNotify(notifyDTO);
        simpMessagingTemplate.convertAndSend("/topic/" + product.getUser().getId(), notifyDTO);
    }

    @MessageMapping("/res_notify")
    public void handleResponseNotify(NotifyResponse notifyResponse) throws DataNotFoundException {
        if(notifyResponse.getStatus().equals(NotifyStatus.AGREE)){
            Order order = orderService.getOrderById(notifyResponse.getOrderId());
            User user = userService.getUser(order.getUser().getId());
            Product product = productService.getProduct(order.getProduct().getId());
            order.setStatus(OrderStatus.CONFIRMED);
            OrderDTO orderDTO = OrderDTO.builder()
                    .userId(order.getUser().getId())
                    .productId(order.getProduct().getId())
                    .status(order.getStatus())
                    .numberOfProduct(order.getNumberOfProducts())
                    .totalMoney(order.getTotalMoney())
                    .shippingAddress(order.getShippingAddress())
                    .paymentMethod(order.getPaymentMethod())
                    .build();
            orderService.updateOrder(order.getId(), orderDTO);
            NotifyDTO notifyDTO = NotifyDTO.builder()
                    .userId(user.getId())
                    .orderId(order.getId())
                    .content("Người bán với tiêu đề sách là " + product.getName() + " đã xác nhận đơn hàng của bạn. Hãy nhấn vào đơn hàng và thanh toán!")
                    .status(NotifyStatus.AGREE)
                    .build();
            notifyService.createNotify(notifyDTO);
            notifyResponse.setContent(notifyDTO.getContent());
            notifyResponse.setStatus(notifyDTO.getStatus());
            System.out.println("*****" + notifyDTO.getContent());
            simpMessagingTemplate.convertAndSend("/topic/" + user.getId(), notifyResponse);
        }else if(notifyResponse.getStatus().equals(NotifyStatus.CANCEL)){
            Order order = orderService.getOrderById(notifyResponse.getOrderId());
            User user = userService.getUser(order.getUser().getId());
            Product product = productService.getProduct(order.getProduct().getId());
            product.setQuantity(product.getQuantity() + order.getNumberOfProducts());
            ProductDTO productDTO = ProductDTO.builder()
                    .author(product.getAuthor())
                    .categoryId(product.getCategory().getId())
                    .name(product.getName())
                    .description(product.getDescription())
                    .price(product.getPrice())
                    .quantity(product.getQuantity())
                    .status(product.getStatus())
                    .build();
            productService.updateProduct(product.getId(), productDTO);
            order.setStatus(OrderStatus.CANCELED);
            OrderDTO orderDTO = OrderDTO.builder()
                    .userId(order.getUser().getId())
                    .productId(order.getProduct().getId())
                    .status(order.getStatus())
                    .numberOfProduct(order.getNumberOfProducts())
                    .totalMoney(order.getTotalMoney())
                    .shippingAddress(order.getShippingAddress())
                    .paymentMethod(order.getPaymentMethod())
                    .build();
            orderService.updateOrder(order.getId(), orderDTO);
            NotifyDTO notifyDTO = NotifyDTO.builder()
                    .userId(user.getId())
                    .orderId(order.getId())
                    .content("Người bán với tiêu đề sách là " + product.getName() + " đã không chấp nhận đơn hàng của bạn. Đơn hàng của bạn đã bị hủy")
                    .status(NotifyStatus.CANCEL)
                    .build();
            notifyService.createNotify(notifyDTO);
            notifyResponse.setContent(notifyDTO.getContent());
            notifyResponse.setStatus(notifyDTO.getStatus());
            System.out.println("*****" + notifyDTO.getContent());
            simpMessagingTemplate.convertAndSend("/topic/" + user.getId(), notifyResponse);
        }
    }

    @MessageMapping("/success_order_notify")
    public void handleSuccessOrderNotify(OrderResponse order) throws DataNotFoundException {
        User user = userService.getUser(order.getUserId());
        Product product = productService.getProduct(order.getProductId());
        String message = "Người dùng với đầu số điện thoại: '" + user.getPhoneNumber() + "' đã thanh toán thành công sách giá trình '" + product.getName() + "'. Hãy nhấn vào thông báo này để xác nhận!";
        NotifyDTO notifyDTO = NotifyDTO.builder()
                .userId(product.getUser().getId())
                .orderId(order.getId())
                .content(message)
                .status(NotifyStatus.NOT_RESPONDED)
                .build();
        notifyService.createNotify(notifyDTO);
        simpMessagingTemplate.convertAndSend("/topic/" + product.getUser().getId(), notifyDTO);
    }

}
