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
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    private String author;

    private String content;

    public void update(String content) {
        this.content = content;
    }

    void assignPost(Post post){
        this.post = post;
    }

}

