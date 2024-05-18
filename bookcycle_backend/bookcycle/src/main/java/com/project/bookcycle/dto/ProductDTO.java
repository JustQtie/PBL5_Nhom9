package com.project.bookcycle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 200, message="Title must be between 3 and 200 character")
    private String name;

    @Size(min = 10, max = 100, message="Title must be between 10 and 100 character")
    private String author;

    @Min(value = 0, message = "Price must be greater than or equal to 0")
    @Max(value = 10000000, message = "Price must be less than or equal to 10,000,000")
    private Float price;
    private String thumbnail;

    @NotBlank(message = "Status is required")
    private String status;

    private String description;

    @Min(value = 0, message = "Quantity must be greater than or equal to 0")
    private int quantity;

    @JsonProperty("user_id")
    private long userId;

    @JsonProperty("category_id") //Thuộc tính được chuyển đổi thành category_id khi chuyển về dữ liệu Json.
    private long categoryId;
}
