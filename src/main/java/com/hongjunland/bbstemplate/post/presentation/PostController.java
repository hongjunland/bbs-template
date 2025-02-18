package com.hongjunland.bbstemplate.post.presentation;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.hongjunland.bbstemplate.common.response.BaseResponse;
import com.hongjunland.bbstemplate.post.application.PostService;
import com.hongjunland.bbstemplate.post.dto.PostRequest;
import com.hongjunland.bbstemplate.post.dto.PostResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class PostController {
    private final PostService postService;

    @PostMapping("/boards/{boardId}/posts")
    public BaseResponse<?> createPost(
            @PathVariable Long boardId,
            @RequestBody @Valid PostRequest request) {
        PostResponse response = postService.createPost(boardId, request);
        return BaseResponse.success(response);
    }

    @GetMapping("/boards/{boardId}/posts")
    public BaseResponse<?> getPostSummaryListByBoardId(@PathVariable Long boardId, Long userId, Pageable pageable) {
        return BaseResponse.success(postService.getPostsByBoardId(boardId, userId, pageable));
    }

    @GetMapping("/posts/{postId}")
    public BaseResponse<?> getPostById(@PathVariable Long postId) {
        return BaseResponse.success(postService.getPostById(postId));
    }

    @PutMapping("/posts/{postId}")
    public BaseResponse<?> updatePost(
            @PathVariable Long postId,
            @RequestBody @Valid PostRequest request) {
        PostResponse response = postService.updatePost(postId, request);
        return BaseResponse.success(response);
    }

    @DeleteMapping("/posts/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public BaseResponse<?> deletePost(
            @PathVariable Long postId) {
        postService.deletePost(postId);
        return BaseResponse.success(null);
    }

    /**
     * ✅ 게시글 좋아요/취소 (토글)
     */
    @PostMapping("/posts/{postId}/like")
    public BaseResponse<String> toggleLike(@PathVariable Long postId, @RequestParam Long userId) {
        boolean liked = postService.togglePostLike(postId, userId);
        return liked ? BaseResponse.success("좋아요가 추가되었습니다.") :
                BaseResponse.success("좋아요가 취소되었습니다.");
    }
}
