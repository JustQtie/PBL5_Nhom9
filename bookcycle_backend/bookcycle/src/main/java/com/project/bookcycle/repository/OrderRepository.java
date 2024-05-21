package com.project.bookcycle.repository;

import com.project.bookcycle.model.Order;
import com.project.bookcycle.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface OrderRepository extends JpaRepository<Order, Long> {
    //Tìm các đơn hàng của 1 user nào đó
    List<Order> findByUserId(Long userId);

    @Query("SELECT o.product FROM Order o WHERE o.status = 'saving'")
    List<Product> findSavingBooks();
}
