package com.project.bookcycle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data // .toString: Biểu diễn object
@Getter
@Setter
@NoArgsConstructor // Khởi tạo Constructor mặc định không có tham số.
@AllArgsConstructor // Khởi tạo tất cả các loại Constructor
@Builder
public class NotifyDTO {
    @JsonProperty("user_id")
    private long userId;

    @JsonProperty("order_id")
    private long orderId;

    private String status;

    @NotEmpty(message = "Content cannot null")
    private String content;
}
