package com.project.bookcycle.repository;

import com.project.bookcycle.model.NotifyEntity;
import com.project.bookcycle.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotifyRepository extends JpaRepository<NotifyEntity, Long> {
    List<NotifyEntity> findByUserId(Long userId);
}
