package com.hongjunland.bbstemplate.post.infrastructure.post;

import com.hongjunland.bbstemplate.post.dto.PostSummaryResponse;

import java.util.List;

public interface PostJpaRepositoryCustom {
    List<PostSummaryResponse> findPostSummaryList(Long boardId, Long userId);
}
