package com.project.bookcycle.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@Builder
public class OrderListResponse {
    private List<OrderResponse> orderResponseList;
    @JsonProperty("EC")
    private String ec;
}
