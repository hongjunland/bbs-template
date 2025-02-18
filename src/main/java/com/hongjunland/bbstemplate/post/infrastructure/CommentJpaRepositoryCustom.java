package com.hongjunland.bbstemplate.post.infrastructure;

import com.hongjunland.bbstemplate.common.response.CursorPage;
import com.hongjunland.bbstemplate.post.dto.ReplyCommentListResponse;
import com.hongjunland.bbstemplate.post.dto.RootCommentListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface CommentJpaRepositoryCustom{
    Page<RootCommentListResponse> findRootCommentsByPostId(Long postId, Long userId, Pageable pageable);
    CursorPage<ReplyCommentListResponse> findReplyListByParentCommentId(Long commentId, Long userId, LocalDateTime cursor, int offset);
}
