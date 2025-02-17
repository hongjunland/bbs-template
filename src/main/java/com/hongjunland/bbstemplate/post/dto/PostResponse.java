package com.hongjunland.bbstemplate.post.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PostResponse(
        Long id,
        Long boardId,
        String boardName,
        String title,
        String content,
        String author,
        int likeCount,
        int commentCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

}
