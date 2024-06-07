package com.project.bookcycle.repository;

import com.project.bookcycle.model.Role;
import com.project.bookcycle.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByPhoneNumber(String phoneNumber);
    Optional<User> findByPhoneNumber(String phoneNumber);
    //SELECT * FROM users WHERE phoneNumber=?
    @Query("SELECT u.role FROM User u WHERE u.phoneNumber = :phone_number")
    Role findRoleByPhoneNumber(@Param("phone_number") String phoneNumber);

    @Query("SELECT u FROM User u WHERE u.phoneNumber = :phone_number")
    User findByPhone(@Param("phone_number") String phoneNumber);

    @Query("select u from User u WHERE u.role.id = :role_id")
    List<User> getAllByRoleId(@Param("role_id") Long roleId);

    @Modifying
    @Query("UPDATE User u SET u.active = :isActive WHERE u.id = :userId")
    void updateIsActiveById(@Param("isActive") boolean isActive, @Param("userId") Long userId);

    @Query("select u from User u WHERE u.id <> :id AND u.id <> 1")
    List<User> getAllNotUser(@Param("id") Long id);
}
