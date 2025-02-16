package com.hongjunland.bbstemplate.comment.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentResponse(
        Long id,
        Long postId,
        Long parentId,
        int likeCount,
        int replyCount,
        String author,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}