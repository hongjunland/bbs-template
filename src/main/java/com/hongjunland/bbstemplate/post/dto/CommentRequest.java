package com.hongjunland.bbstemplate.post.dto;

import lombok.Builder;

@Builder
public record CommentRequest(
        Long parentId,
        String author,
        String content) {
}
