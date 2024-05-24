package com.project.bookcycle.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.bookcycle.model.NotifyEntity;
import com.project.bookcycle.model.Order;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotifyResponse {
    private long id;

    @JsonProperty("user_id")
    private long userId;

    private String content;

    @JsonProperty("EC")
    private String ec;

    public static NotifyResponse convertFromNotify(NotifyEntity notifyEntity){
        return NotifyResponse.builder()
                .id(notifyEntity.getId())
                .userId(notifyEntity.getUser().getId())
                .content(notifyEntity.getContent())
                .build();
    }
}
