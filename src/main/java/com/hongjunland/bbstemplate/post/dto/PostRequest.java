package com.hongjunland.bbstemplate.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.List;

@Builder
public record PostRequest(Long boardId,
                          @NotBlank String title,
                          @NotBlank String content,
                          @NotBlank String author,
                          List<Long> attachmentFileIds
) {
}
