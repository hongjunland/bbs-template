package com.hongjunland.bbstemplate.post.dto;

import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.hongjunland.bbstemplate.post.domain.PostJpaEntity;
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
