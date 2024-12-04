package com.nguyenvannhat.library.models;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "comments")
@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "comment")
    private String comment;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id")
    private User userComment;


    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "post_id")
    private Post post;
}
