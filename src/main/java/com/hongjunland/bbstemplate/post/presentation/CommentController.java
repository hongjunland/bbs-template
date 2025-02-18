package com.hongjunland.bbstemplate.post.presentation;

import com.hongjunland.bbstemplate.post.application.CommentService;
import com.hongjunland.bbstemplate.post.dto.CommentRequest;
import com.hongjunland.bbstemplate.post.dto.CommentResponse;
import com.hongjunland.bbstemplate.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    /**
     * ✅ 게시글의 루트 댓글 조회
     */
    @GetMapping("/posts/{postId}/comments")
    public BaseResponse<?> getCommentsByPostId(@PathVariable Long postId, Long userId, Pageable pageable) {
        return BaseResponse.success(commentService.getCommentsByPostId(postId, userId, pageable));
    }

    /**
     * ✅ 특정 댓글의 대댓글 조회
     */
    @GetMapping("/comments/{commentId}/replies")
    public BaseResponse<List<CommentResponse>> getReplies(@PathVariable Long commentId) {
        return BaseResponse.success(commentService.getReplies(commentId));
    }

    /**
     * ✅ 댓글 생성
     */
    @PostMapping("/posts/{postId}/comments")
    public BaseResponse<CommentResponse> createComment(
            @PathVariable Long postId,
            @RequestBody CommentRequest request) {
        return BaseResponse.success(commentService.createComment(postId, request));
    }

    /**
     * ✅ 댓글 수정
     */
    @PutMapping("/comments/{commentId}")
    public BaseResponse<CommentResponse> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentRequest request) {
        return BaseResponse.success(commentService.updateComment(commentId, request));
    }

    /**
     * ✅ 댓글 삭제
     */
    @DeleteMapping("/comments/{commentId}")
    public BaseResponse<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return BaseResponse.success(null);
    }

    /**
     * ✅ 댓글 좋아요/취소 (토글)
     */
    @PostMapping("/comments/{commentId}/like")
    public BaseResponse<String> toggleLike(@PathVariable Long commentId, @RequestParam Long userId) {
        boolean liked = commentService.toggleCommentLike(commentId, userId);
        return liked ? BaseResponse.success("좋아요가 추가되었습니다.") :
                BaseResponse.success("좋아요가 취소되었습니다.");
    }
}
