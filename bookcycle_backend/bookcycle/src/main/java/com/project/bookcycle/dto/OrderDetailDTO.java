package com.project.bookcycle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailDTO {
    @JsonProperty("order_id")
    @Min(value = 1, message = "Order's id must be > 0")
    private long orderId;

    @Min(value = 1, message = "Product's id must be > 0")
    @JsonProperty("product_id")
    private long productId;

    @Min(value = 1, message = "number of product must be >= 1")
    @JsonProperty("number_of_product")
    private int numberOfProduct;

    @Min(value = 0, message = "total money must be >= 0")
    @JsonProperty("total_money")
    private float totalMoney;
}
