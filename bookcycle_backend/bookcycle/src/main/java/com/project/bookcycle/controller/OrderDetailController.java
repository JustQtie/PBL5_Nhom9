package com.project.bookcycle.controller;

import com.project.bookcycle.dto.OrderDetailDTO;
import com.project.bookcycle.exceptions.DataNotFoundException;
import com.project.bookcycle.response.OrderDetailResponse;
import com.project.bookcycle.service.IOrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order_details")
@RequiredArgsConstructor
public class OrderDetailController {
    private final IOrderDetailService orderDetailService;
    @PostMapping
    public ResponseEntity<?> createOrderDetail(
            @Valid @RequestBody OrderDetailDTO orderDetailDTO,
            BindingResult result
    ){
        if(result.hasErrors()){
            List<String> messageError = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(messageError);
        }

        try {
            return ResponseEntity.ok(OrderDetailResponse.convertToOrderDetail(orderDetailService.createOrderDetail(orderDetailDTO)));
        } catch (DataNotFoundException e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(
            @Valid @PathVariable("id") long id
    ){
        try {
            return ResponseEntity.ok(OrderDetailResponse.convertToOrderDetail(orderDetailService.getOrderDetailById(id)));
        } catch (DataNotFoundException e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }

    //Lấy danh sách các order_details của một order nào đó.
    @GetMapping("/order/{order_id}")
    public ResponseEntity<?> getOrderDetailsByOrderId(
            @Valid @PathVariable("order_id") long orderId
    ){
        try {
            List<OrderDetailResponse> orderDetailResponses = orderDetailService.getOrderDetailByOrderId(orderId)
                    .stream().map(OrderDetailResponse::convertToOrderDetail).toList();
            return ResponseEntity.ok(orderDetailResponses);
        } catch (DataNotFoundException e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetails(
            @Valid @PathVariable("id") long id,
            @Valid @RequestBody OrderDetailDTO newOrderDetails,
            BindingResult result
    ) throws DataNotFoundException {
        if(result.hasErrors()){
            List<String> messageError = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(messageError);
        }
        return ResponseEntity.ok("update success: " + OrderDetailResponse.convertToOrderDetail(orderDetailService.updateOrderDetail(id, newOrderDetails)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(
            @Valid @PathVariable("id") long id
    ) throws DataNotFoundException {
        orderDetailService.deleteOrderDetail(id);
        return ResponseEntity.ok("Deleted success!");
        //return ResponseEntity.noContent().build();
    }
}
