package com.project.bookcycle.service;

import com.project.bookcycle.model.NotifyEntity;

import java.util.List;

public interface INotifyService {
    List<NotifyEntity> findByUserId(long userId);
    void deleteNotifyEntity(long id);
}
