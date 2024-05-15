package com.project.bookcycle.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.bookcycle.model.User;
import lombok.*;

import java.sql.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse extends BaseResponse{
    private Long id;
    private String fullname;
    @JsonProperty("phone_number")
    private String phoneNumber;
    private String address;
    private boolean active;
    @JsonProperty("date_of_birth")
    private Date dateOfBirth;
    private boolean gender;
    private String thumbnail;
    @JsonProperty("EC")
    private int ec;

    public static UserResponse convertToUserResponse(User user){
        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .fullname(user.getFullname())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .active(user.isActive())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.isGender())
                .build();
        userResponse.setCreatedAt(user.getCreateAt());
        userResponse.setUpdatedAt(user.getUpdateAt());
        return  userResponse;
    }
}
