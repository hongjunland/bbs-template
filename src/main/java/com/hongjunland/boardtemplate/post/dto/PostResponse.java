package com.hongjunland.boardtemplate.post.dto;

import com.hongjunland.boardtemplate.post.domain.PostJpaEntity;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Builder
public record PostResponse(
        Long id,
        Long boardId,
        String boardName,
        String title,
        String content,
        String author,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

}
