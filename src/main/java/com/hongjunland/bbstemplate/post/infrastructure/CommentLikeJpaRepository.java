package com.hongjunland.bbstemplate.post.infrastructure;

import com.hongjunland.bbstemplate.post.domain.Comment;
import com.hongjunland.bbstemplate.post.domain.CommentLike;
import com.hongjunland.bbstemplate.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeJpaRepository extends JpaRepository<CommentLike, Long> {
    int countByCommentId(Long commentId);  // ✅ 좋아요 개수 조회

    boolean existsByCommentAndUser(Comment comment, User user);

    void deleteCommentLikeByCommentIdAndUserId(Long commentId, Long userId);
}
