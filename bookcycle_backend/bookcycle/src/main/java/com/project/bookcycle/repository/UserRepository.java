package com.project.bookcycle.repository;

import com.project.bookcycle.model.Role;
import com.project.bookcycle.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByPhoneNumber(String phoneNumber);
    Optional<User> findByPhoneNumber(String phoneNumber);
    //SELECT * FROM users WHERE phoneNumber=?
    @Query("SELECT u.role FROM User u WHERE u.phoneNumber = :phone_number")
    Role findRoleByPhoneNumber(@Param("phone_number") String phoneNumber);
}
