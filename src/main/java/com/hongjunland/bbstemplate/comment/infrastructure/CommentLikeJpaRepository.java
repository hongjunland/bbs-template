package com.hongjunland.bbstemplate.comment.infrastructure;

import com.hongjunland.bbstemplate.comment.domain.CommentJpaEntity;
import com.hongjunland.bbstemplate.comment.domain.CommentLikeJpaEntity;
import com.hongjunland.bbstemplate.user.domain.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeJpaRepository extends JpaRepository<CommentLikeJpaEntity, Long> {
    boolean existsByCommentAndUser(CommentJpaEntity comment, UserJpaEntity user);
    void deleteByCommentAndUser(CommentJpaEntity comment, UserJpaEntity user);
    int countByCommentId(Long commentId);  // ✅ 좋아요 개수 조회

}
