package com.project.bookcycle.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass // chỉ định rằng một lớp là một "lớp siêu ánh xạ" cho các lớp thực thể cụ thể khác. Các thuộc tính và
// phương thức trong lớp siêu ánh xạ có thể được kế thừa bởi các lớp con được ánh xạ sang cơ sở dữ liệu. Tuy nhiên, lớp được đánh dấu với @MappedSuperclass không tạo ra một bảng trong cơ sở dữ liệu.
public class BaseEntity {
    @Column(name = "created_at")
    private LocalDateTime createAt;

    @Column(name = "updated_at")
    private LocalDateTime updateAt;

    @PrePersist
    protected void onCreate() {
        createAt = LocalDateTime.now();
        updateAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateAt = LocalDateTime.now();
    }
}
