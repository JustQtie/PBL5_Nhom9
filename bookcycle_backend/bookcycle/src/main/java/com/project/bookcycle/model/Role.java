package com.project.bookcycle.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "role")
@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 100)
    private String name;
}
