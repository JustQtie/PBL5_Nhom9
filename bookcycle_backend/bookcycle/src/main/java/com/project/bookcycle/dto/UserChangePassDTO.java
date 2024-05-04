package com.project.bookcycle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserChangePassDTO {
    @JsonProperty("old_password")
    @NotBlank(message = "Old password is required")
    private String oldPass;

    @JsonProperty("new_password")
    @NotBlank(message = "New password is required")
    private String newPass;
}
