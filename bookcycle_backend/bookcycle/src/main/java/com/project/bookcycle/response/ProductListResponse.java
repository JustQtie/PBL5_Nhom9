package com.project.bookcycle.response;

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
}
