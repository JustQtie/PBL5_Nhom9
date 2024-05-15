package com.project.bookcycle.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data // .toString: Biểu diễn object
@Getter
@Setter
@NoArgsConstructor // Khởi tạo Constructor mặc định không có tham số.
@AllArgsConstructor // Khởi tạo tất cả các loại Constructor
@Builder
public class CategoryDTO {
    @NotEmpty(message = "Category's name cannot null")
    private String name;
}
