package com.hongjunland.boardtemplate.comment.infrastructure;

import com.hongjunland.boardtemplate.comment.domain.CommentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentJpaRepository extends JpaRepository<CommentJpaEntity, Long> {
    List<CommentJpaEntity> findByPostId(Long postId);
}
