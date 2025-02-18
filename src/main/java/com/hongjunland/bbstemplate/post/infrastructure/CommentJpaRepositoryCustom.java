package com.hongjunland.bbstemplate.post.infrastructure;

import com.hongjunland.bbstemplate.post.dto.RootCommentListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentJpaRepositoryCustom{
    Page<RootCommentListResponse> findRootCommentsByPostId(Long postId, Long userId, Pageable pageable);
}
