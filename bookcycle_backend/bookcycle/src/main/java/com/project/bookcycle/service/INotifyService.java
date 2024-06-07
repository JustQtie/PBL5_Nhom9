package com.project.bookcycle.service;

import com.project.bookcycle.dto.NotifyDTO;
import com.project.bookcycle.exceptions.DataNotFoundException;
import com.project.bookcycle.model.NotifyEntity;
import com.project.bookcycle.response.NotifyResponse;

import java.util.List;

public interface INotifyService {
    List<NotifyResponse> findByUserId(long userId);
    void deleteNotifyEntity(long id);
    NotifyEntity createNotify(NotifyDTO notifyDTO) throws DataNotFoundException;
    void deleteNotifyByOrder(Long id);
}
