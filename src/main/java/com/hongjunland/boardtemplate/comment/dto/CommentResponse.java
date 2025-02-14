package com.hongjunland.boardtemplate.comment.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentResponse(
        Long id,
        Long postId,
        String author,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}