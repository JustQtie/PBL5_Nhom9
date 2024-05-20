package com.project.bookcycle.service;

import com.project.bookcycle.model.NotifyEntity;
import com.project.bookcycle.repository.NotifyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotifyService implements INotifyService{
    private final NotifyRepository notifyRepository;
    @Override
    public List<NotifyEntity> findByUserId(long userId) {
        return notifyRepository.findByUserId(userId);
    }

    @Override
    public void deleteNotifyEntity(long id) {
        notifyRepository.deleteById(id);
    }
}
