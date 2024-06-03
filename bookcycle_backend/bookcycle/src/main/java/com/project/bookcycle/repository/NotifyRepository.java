package com.project.bookcycle.repository;

import com.project.bookcycle.model.NotifyEntity;
import com.project.bookcycle.model.Order;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.jmx.export.annotation.ManagedOperation;

import java.util.List;

@Transactional
public interface NotifyRepository extends JpaRepository<NotifyEntity, Long> {
    List<NotifyEntity> findByUserId(Long userId);

    @Modifying
    @Query("DELETE FROM NotifyEntity noti WHERE noti.order.id = :id")
    void deleteNotifyEntitiesByOrder(@Param("id") Long id);
}
