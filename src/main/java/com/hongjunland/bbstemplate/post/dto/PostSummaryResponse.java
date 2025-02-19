package com.hongjunland.bbstemplate.post.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PostSummaryResponse(
        Long id,
        Long boardId,
        String title,
        String author,
        String contentSnippet,
        long likeCount,
        boolean isLiked,
        long commentCount,
        LocalDateTime updatedAt

) {
}
