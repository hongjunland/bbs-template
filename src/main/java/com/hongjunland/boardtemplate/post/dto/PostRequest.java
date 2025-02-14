package com.hongjunland.boardtemplate.post.dto;

import lombok.Builder;

@Builder
public record PostRequest(Long boardId,
                          String title,
                          String content,
                          String author) {
}
