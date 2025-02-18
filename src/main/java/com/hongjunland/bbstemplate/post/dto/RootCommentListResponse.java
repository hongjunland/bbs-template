package com.hongjunland.bbstemplate.post.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RootCommentListResponse(Long id,
                                      Long postId,
                                      Long parentId,
                                      long likeCount,
                                      long replyCount,
                                      boolean isLiked,
                                      String author,
                                      String content,
                                      LocalDateTime updatedAt) {
}
