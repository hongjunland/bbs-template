package com.hongjunland.bbstemplate.post.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hongjunland.bbstemplate.post.domain.Comment;

import java.util.List;

public interface CommentJpaRepository extends JpaRepository<Comment, Long>, CommentJpaRepositoryCustom {
    List<Comment> findByPostId(Long postId);

    int countCommentByPostId(Long postId);

    int countByParentId(Long parentId);

}
