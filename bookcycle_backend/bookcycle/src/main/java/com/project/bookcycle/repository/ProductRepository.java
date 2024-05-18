package com.project.bookcycle.repository;

import com.project.bookcycle.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByName(String name);

    List<Product> findAll();

    List<Product> findByUserId(Long userId);

    @Modifying
    @Query("UPDATE Product p SET p.thumbnail = :thumbnailUrl where p.id = :id")
    void updateThumbnail(@Param("id") long productId, @Param("thumbnailUrl") String thumbnail);

    @Query("select p from Product p WHERE (:categoryId IS NULL or :categoryId = 0 OR p.category.id = :categoryId) " +
            "AND (:keyword IS NULL or :keyword = '' OR p.name LIKE %:keyword% OR p.description LIKE %:keyword%)")
    List<Product> searchProducts(
            @Param("keyword") String keyword,
            @Param("categoryId") Long categoryId
    );
}
