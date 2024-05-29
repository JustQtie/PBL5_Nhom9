package com.project.bookcycle.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.bookcycle.model.Order;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Min;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@Builder
public class OrderResponse {

    private Long id;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("product_id")
    private long productId;

    @JsonProperty("number_of_product")
    private int numberOfProduct;

    private String status;

    @JsonProperty("total_money")
    private float totalMoney;

    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("EC")
    private String ec;

    public static OrderResponse convertFromOrder(Order order){
        OrderResponse orderResponse = OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .productId(order.getProduct().getId())
                .numberOfProduct(order.getNumberOfProducts())
                .shippingAddress(order.getShippingAddress())
                .status(order.getStatus())
                .build();
        if(order.getTotalMoney() != null && order.getPaymentMethod() != null){
            orderResponse.setTotalMoney(order.getTotalMoney());
            orderResponse.setPaymentMethod(order.getPaymentMethod());
        }
        if(order.getTotalMoney() == null){
            orderResponse.setTotalMoney(0L);
        }
        if(order.getPaymentMethod() == null){
            orderResponse.setPaymentMethod("");
        }
        return orderResponse;
    }
}
