package com.project.bookcycle.service;

import com.project.bookcycle.dto.NotifyDTO;
import com.project.bookcycle.exceptions.DataNotFoundException;
import com.project.bookcycle.model.NotifyEntity;
import com.project.bookcycle.model.User;
import com.project.bookcycle.repository.NotifyRepository;
import com.project.bookcycle.repository.UserRepository;
import com.project.bookcycle.response.NotifyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotifyService implements INotifyService{
    private final NotifyRepository notifyRepository;
    private final UserRepository userRepository;
    @Override
    public List<NotifyResponse> findByUserId(long userId) {
        return notifyRepository.findByUserId(userId)
                .stream()
                .map(NotifyResponse::convertFromNotify)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteNotifyEntity(long id) {
        notifyRepository.deleteById(id);
    }

    @Override
    public NotifyEntity createNotify(NotifyDTO notifyDTO) throws DataNotFoundException {

        User user = userRepository.findById(notifyDTO.getUserId())
                .orElseThrow(()-> new DataNotFoundException("Cannot find user with id " + notifyDTO.getUserId()));
        NotifyEntity notifyResponse = NotifyEntity
                .builder()
                .user(user)
                .content(notifyDTO.getContent())
                .build();
        return notifyRepository.save(notifyResponse);
    }
}
