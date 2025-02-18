package com.hongjunland.bbstemplate.post.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReplyCommentListResponse(Long id,
                                       Long postId,
                                       Long parentId,
                                       long likeCount,
                                       long replyCount,
                                       boolean isLiked,
                                       String author,
                                       String content,
                                       LocalDateTime createdAt,
                                       LocalDateTime updatedAt

) {
}
