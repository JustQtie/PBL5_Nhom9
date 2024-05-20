package com.project.bookcycle.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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
}
