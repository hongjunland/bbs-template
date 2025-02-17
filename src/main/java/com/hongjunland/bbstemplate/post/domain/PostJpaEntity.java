package com.hongjunland.bbstemplate.post.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import com.hongjunland.bbstemplate.board.domain.Board;
import com.hongjunland.bbstemplate.common.domain.BaseTimeEntity;

@Entity
@Getter
@Table(name = "posts")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostJpaEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    private String title;
    private String content;
    private String author;

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
