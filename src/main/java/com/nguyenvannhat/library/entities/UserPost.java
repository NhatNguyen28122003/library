package com.nguyenvannhat.library.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_post")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long postId;
}
