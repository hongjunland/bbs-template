package com.hongjunland.bbstemplate.comment.infrastructure;

import com.hongjunland.bbstemplate.user.domain.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hongjunland.bbstemplate.comment.domain.CommentJpaEntity;
import org.springframework.data.jpa.repository.Query;

import java.util.Arrays;
import java.util.List;

public interface CommentJpaRepository extends JpaRepository<CommentJpaEntity, Long> {
    List<CommentJpaEntity> findByPostId(Long postId);

    List<CommentJpaEntity> findByPostIdAndParentIsNull(Long postId);

    List<CommentJpaEntity> findByParent(CommentJpaEntity parent);

    int countCommentJpaEntityByPostId(Long postId);

    int countByParentId(Long parentId);

}
