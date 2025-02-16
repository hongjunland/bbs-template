package com.hongjunland.bbstemplate.board.dto;

import lombok.Builder;

@Builder
public record BoardResponse(Long boardId, String name, String description) {
}
