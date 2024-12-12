package com.nguyenvannhat.library.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "role_functions")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RoleFunction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "role_id")
    private Long roleId;

    @Column(name ="function_id")
    private Long functionId;
}
