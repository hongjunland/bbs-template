package com.hongjunland.bbstemplate.post.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hongjunland.bbstemplate.post.domain.Comment;

import java.util.List;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);

    List<Comment> findByPostIdAndParentIsNull(Long postId);

    List<Comment> findByParent(Comment parent);

    int countCommentByPostId(Long postId);

    int countByParentId(Long parentId);

}
