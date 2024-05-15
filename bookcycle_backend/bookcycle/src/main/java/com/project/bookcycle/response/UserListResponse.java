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
public class UserListResponse {
    private List<UserResponse> userResponseList;
    @JsonProperty("EC")
    private int EC;
}
