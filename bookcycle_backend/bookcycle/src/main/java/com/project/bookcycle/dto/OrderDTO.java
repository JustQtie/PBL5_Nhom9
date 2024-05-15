package com.project.bookcycle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    @Min(value = 1, message = "User's id must be > 0")
    @JsonProperty("user_id")
    private long userId;

    @JsonProperty("order_date")
    private LocalDate orderDate;

    @JsonProperty("total_money")
    @Min(value = 0, message = "Total money must be >= 0")
    private float totalMoney;

    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("shipping_Method")
    private String shippingMethod;

    @JsonProperty("payment_method")
    private String paymentMethod;
}
