package com.hongjunland.bbstemplate.post.infrastructure.post;

import com.hongjunland.bbstemplate.post.dto.PostSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostJpaRepositoryCustom {
    Page<PostSummaryResponse> findPostSummaryList(Long boardId, Long userId, Pageable pageable);
}
