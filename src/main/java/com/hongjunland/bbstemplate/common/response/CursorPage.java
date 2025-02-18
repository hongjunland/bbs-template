package com.hongjunland.bbstemplate.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class CursorPage<T> {
    private List<T> content;  // 실제 데이터 목록
    private LocalDateTime nextCursor;  // 다음 페이지 조회를 위한 cursor
}
