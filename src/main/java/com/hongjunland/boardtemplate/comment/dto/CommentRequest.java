package com.hongjunland.boardtemplate.comment.dto;

import lombok.Builder;

@Builder
public record CommentRequest(String author,
                             String content) {
}
