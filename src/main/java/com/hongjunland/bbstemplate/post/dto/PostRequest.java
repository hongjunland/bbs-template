package com.hongjunland.bbstemplate.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record PostRequest(@NotBlank Long boardId,
                          @NotBlank String title,
                          @NotBlank String content,
                          @NotBlank String author) {
}
