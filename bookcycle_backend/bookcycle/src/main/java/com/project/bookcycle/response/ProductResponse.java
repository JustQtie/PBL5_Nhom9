package com.project.bookcycle.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.bookcycle.model.Product;
import com.project.bookcycle.model.User;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse extends BaseResponse{
    private Long id;
    private String name;
    private String author;
    private Float price;
    private String description;
    private int quantity;
    private String status;
    private User user;
    private String thumbnail;
    @JsonProperty("EC")
    private String ec;

    @JsonProperty("category_id")
    private long categoryId;

    public static ProductResponse convertToProductResponse(Product product){
        ProductResponse productResponse = ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .author(product.getAuthor())
                .price(product.getPrice())
                .description(product.getDescription())
                .quantity(product.getQuantity())
                .status(product.getStatus())
                .user(product.getUser())
                .thumbnail(product.getThumbnail())
                .categoryId(product.getCategory().getId())
                .build();
        productResponse.setCreatedAt(product.getCreateAt());
        productResponse.setUpdatedAt(product.getUpdateAt());
        return productResponse;
    }
}
