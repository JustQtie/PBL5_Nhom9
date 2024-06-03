package com.project.bookcycle.repository;

import com.project.bookcycle.model.Order;
import com.project.bookcycle.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface OrderRepository extends JpaRepository<Order, Long> {
    //Tìm các đơn hàng của 1 user nào đó
    List<Order> findByUserId(Long userId);

    @Query("SELECT o FROM Order o WHERE o.user.id = :id")
    List<Order> findOrderByUser(@Param("id") Long userId);

    @Query("SELECT o FROM Order o WHERE o.user.id = :id AND o.status <> :status AND o.active = true")
    List<Order> findOrderByUserAndStatusNotPaid(@Param("id") Long userId, @Param("status") String status);

    @Query("SELECT o FROM Order o WHERE o.user.id = :id AND o.status = :status AND o.active = true")
    List<Order> findOrderByUserAndStatusPaid(@Param("id") Long userId, @Param("status") String status);

    List<Order> findAll();

    List<Order> findByProduct_Id(Long productId);

    @Modifying
    @Query("DELETE FROM Order o WHERE o.product.id = :id")
    void deleteOrderByProduct(@Param("id") Long productId);
}
