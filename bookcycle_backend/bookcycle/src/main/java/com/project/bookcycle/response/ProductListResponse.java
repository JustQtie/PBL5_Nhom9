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
public class ProductListResponse {
    private List<ProductResponse> productResponseList;
    @JsonProperty("EC")
    private String ec;
}
