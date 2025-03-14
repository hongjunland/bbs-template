package com.hongjunland.bbstemplate.post.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PostResponse(
        Long postId,
        Long boardId,
        String boardName,
        String title,
        String content,
        String author,
        long likeCount,
        long commentCount,
        boolean isLiked,
        List<Attachment> attachments,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    @Builder
    public record Attachment(Long attachmentId, String attachmentName, String attachmentUrl, String fileType) {

    }
}
