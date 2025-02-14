package com.hongjunland.boardtemplate.comment.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hongjunland.boardtemplate.common.domain.BaseTimeEntity;
import com.hongjunland.boardtemplate.post.domain.PostJpaEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CommentJpaEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    @JsonIgnore
    private PostJpaEntity post;

    private String author;
    private String content;

    public void update(String content) {
        this.content = content;
    }
}

