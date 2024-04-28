package com.project.bookcycle.model;

import com.fasterxml.jackson.databind.DatabindException;
import com.project.bookcycle.service.UserService;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder //Tự động tạo đối tượng User sử dụng builder pattern
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", length = 255)
    private String username;

    @Column(name = "password", length = 100, nullable = false)
    private String password;

    @Column(name = "fullname", length = 100)
    private String fullname;

    @Column(name = "phone_number", length = 10, nullable = false)
    private String phoneNumber;

    @Column(name = "address", length = 200, nullable = false)
    private String address;

    @Column(name = "is_active")
    private boolean active;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "gender")
    private boolean gender;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    //trả về danh sách các quyền (roles) của người dùng dưới dạng một Collection các đối tượng GrantedAuthority.
    // Đối tượng GrantedAuthority đại diện cho một quyền (role) cụ thể mà người dùng được phân quyền trong hệ thống.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Trong một ứng dụng sử dụng Spring Security, quy tắc chung là vai trò (role) của người dùng được định nghĩa trong một chuỗi có dạng "ROLE_rolename".
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("ROLE_"+getRole().getName().toUpperCase()));
        return authorityList;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return phoneNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
