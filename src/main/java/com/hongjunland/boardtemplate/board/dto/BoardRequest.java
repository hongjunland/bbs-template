package com.hongjunland.boardtemplate.board.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
@Builder
public record BoardRequest(@NotBlank String name, @NotBlank String description) {
}
