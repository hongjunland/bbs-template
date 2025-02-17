package com.hongjunland.bbstemplate.post.domain;

import jakarta.persistence.*;
import lombok.*;

import com.hongjunland.bbstemplate.board.domain.Board;
import com.hongjunland.bbstemplate.common.domain.BaseTimeEntity;
import org.hibernate.engine.internal.Collections;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "posts")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    private String title;
    private String content;
    private String author;

    // Post 애그리게이트 내부에서 관리하는 자식(하위) 구성 요소: 댓글
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();


    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    /**
     * 댓글 추가 도메인 행위
     */
    public void addComment(Comment comment) {
        comments.add(comment);
        comment.assignPost(this);
    }

    /**
     * 댓글 삭제 도메인 행위.
     */
    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.assignPost(null);
    }

}
