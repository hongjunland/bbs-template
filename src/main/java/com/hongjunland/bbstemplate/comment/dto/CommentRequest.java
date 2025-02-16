package com.hongjunland.bbstemplate.comment.dto;

import lombok.Builder;

@Builder
public record CommentRequest(
        Long parentId,
        String author,
        String content) {
}
