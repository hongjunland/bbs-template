package com.hongjunland.bbstemplate.post.domain;

import com.hongjunland.bbstemplate.common.domain.BaseTimeEntity;

import com.hongjunland.bbstemplate.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Getter
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    private String author;

    private String content;

    // 대댓글 목록
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> childComments = new ArrayList<>();

    public void update(String content) {
        this.content = content;
    }

    void assignPost(Post post){
        this.post = post;
    }

    /**
     * 대댓글 추가 도메인 행위
     */
    public void addReply(Comment reply) {
        childComments.add(reply);
        reply.assignParentComment(this);
    }

    // 내부 전용: 부모 댓글 설정 (세터 대신 사용)
    void assignParentComment(Comment parentComment) {
        this.parent = parentComment;
    }

}

