package com.project.bookcycle.controller;

import com.project.bookcycle.dto.OrderDTO;
import com.project.bookcycle.exceptions.DataNotFoundException;
import com.project.bookcycle.model.Order;
import com.project.bookcycle.model.OrderStatus;
import com.project.bookcycle.response.*;
import com.project.bookcycle.service.IOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {
    private final IOrderService orderService;
    @PostMapping("")
    public ResponseEntity<?> createOrder(
            @Valid @RequestBody OrderDTO orderDTO,
            BindingResult result
    ){
        try {
            if(result.hasErrors()){
                List<String> messageError = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(messageError);
            }
            Order order = orderService.createOrder(orderDTO);
            OrderResponse orderResponse = OrderResponse.builder()
                    .id(order.getId())
                    .userId(order.getUser().getId())
                    .productId(order.getProduct().getId())
                    .status(order.getStatus())
                    .shippingAddress(order.getShippingAddress())
                    .ec("0")
                    .build();
            return ResponseEntity.ok().body(orderResponse);
        }catch (Exception e){
            return ResponseEntity.ok(e.getMessage());
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(
            @Valid @PathVariable("id") Long id
    ){
        try{
            Order order = orderService.getOrderById(id);
            OrderResponse orderResponse = OrderResponse.builder()
                    .id(order.getId())
                    .userId(order.getUser().getId())
                    .productId(order.getProduct().getId())
                    .status(order.getStatus())
                    .shippingAddress(order.getShippingAddress())
                    .numberOfProduct(order.getNumberOfProducts())
                    .totalMoney(order.getTotalMoney())
                    .paymentMethod(order.getPaymentMethod())
                    .ec("0")
                    .build();
            return ResponseEntity.ok(orderResponse);
        }catch(Exception e){
            return ResponseEntity.ok(e.getMessage());
        }
    }


    @GetMapping("")
    public ResponseEntity<?> getAllOrder(){
        try {
            List<OrderResponse> orderResponses = orderService.findGetOrders();
            return ResponseEntity.ok(OrderListResponse.builder()
                    .orderResponseList(orderResponses)
                    .ec("0").build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(OrderListResponse.builder()
                    .ec("-1").build() + e.getMessage());
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getOrderByUser(
            @PathVariable("id") Long id
    ){
        try {
            List<OrderResponse> orderResponses = orderService.findOrderByUser(id);
            return ResponseEntity.ok(OrderListResponse.builder()
                    .orderResponseList(orderResponses)
                    .ec("0").build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(OrderListResponse.builder()
                    .ec("-1").build() + e.getMessage());
        }
    }

    @GetMapping("/user_notpaid/{id}")
    public ResponseEntity<?> getOrderByUserNotPaid(
            @PathVariable("id") Long id
    ){
        try {
            List<OrderResponse> orderResponses = orderService.findByUserAndStatusNotPaid(id);
            return ResponseEntity.ok(OrderListResponse.builder()
                    .orderResponseList(orderResponses)
                    .ec("0").build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(OrderListResponse.builder()
                    .ec("-1").build() + e.getMessage());
        }
    }

    @GetMapping("/user_paid/{id}")
    public ResponseEntity<?> getOrderByUserPaid(
            @PathVariable("id") Long id
    ){
        try {
            List<OrderResponse> orderResponses = orderService.findByUserAndStatusPaid(id);
            return ResponseEntity.ok(OrderListResponse.builder()
                    .orderResponseList(orderResponses)
                    .ec("0").build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(OrderListResponse.builder()
                    .ec("-1").build() + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(
            @Valid @PathVariable long id,
            @Valid @RequestBody OrderDTO orderDTO, //order được cập nhật dựa trên id.
            BindingResult result
    ){
        try {
            if(result.hasErrors()){
                List<String> messageError = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(messageError);
            }
            Order order = orderService.updateOrder(id, orderDTO);
            OrderResponse orderResponse = OrderResponse.builder()
                    .id(order.getId())
                    .userId(order.getUser().getId())
                    .productId(order.getProduct().getId())
                    .status(order.getStatus())
                    .shippingAddress(order.getShippingAddress())
                    .numberOfProduct(order.getNumberOfProducts())
                    .totalMoney(order.getTotalMoney())
                    .paymentMethod(order.getPaymentMethod())
                    .ec("0")
                    .build();
            return ResponseEntity.ok(orderResponse);
        }catch (Exception e){
            return ResponseEntity.ok(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(
            @Valid @PathVariable long id
    ) throws DataNotFoundException {
        // Xóa mềm => cập nhật trường active => false.
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Delete successfully");
    }

    @PostMapping("/statistical/{id}")
    public ResponseEntity<?> resStatisticalOfUser(
            @Valid @PathVariable long id
    ){
        try {
            float totalMoney = 0;
            List<OrderResponse> orderResponses = orderService.findByUserAndStatusPaid(id);
            for(OrderResponse orderResponse : orderResponses){
                totalMoney+=orderResponse.getTotalMoney();
            }
            int totalOrderSuccessOfUser = orderService.findByUserAndStatusPaid(id).size();
            int totalOrderCanceledOfUser = orderService.findByUserAndStatusCanceled(id).size();
            int totalOrderUser = orderService.findOrderByUser(id).size();
            return ResponseEntity.ok().body(SoLieuThongKeResponse.builder()
                    .tongGiaoDichThanhCongOfUser(totalOrderSuccessOfUser)
                    .tongGiaoDichBiHuyOfUser(totalOrderCanceledOfUser)
                    .tongTatCaGiaoDichOfUser(totalOrderUser)
                    .tongTienChiTieuOfUser(totalMoney)
                    .EC("0")
                    .build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(SoLieuThongKeResponse.builder()
                    .EC("-1")
                    .build());
        }
    }

    @PostMapping("/statistical")
    public ResponseEntity<?> resStatistical(
    ){
        try {
            int totalOrderSuccess = orderService.findByStatusPaid().size();
            int totalOrderCanceled = orderService.findByStatusCanceled().size();
            int totalOrder = orderService.findGetOrders().size();
            return ResponseEntity.ok().body(SoLieuThongKeResponse.builder()
                    .tongGiaoDichThanhCong(totalOrderSuccess)
                    .tongGiaoDichBiHuy(totalOrderCanceled)
                    .tongTatCaGiaoDich(totalOrder)
                    .EC("0")
                .build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(SoLieuThongKeResponse.builder()
                    .EC("-1")
                    .build());
        }
    }
}
