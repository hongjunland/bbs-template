package com.hongjunland.bbstemplate.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CommentRequest(
        @NotBlank String author,
        @NotBlank String content) {
}
