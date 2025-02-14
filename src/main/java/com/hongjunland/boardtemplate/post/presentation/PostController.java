package com.hongjunland.boardtemplate.post.presentation;

import com.hongjunland.boardtemplate.common.response.BaseResponse;
import com.hongjunland.boardtemplate.post.application.PostService;
import com.hongjunland.boardtemplate.post.dto.PostRequest;
import com.hongjunland.boardtemplate.post.dto.PostResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards/{boardId}/posts")
public class PostController {
    private final PostService postService;

    @PostMapping
    public BaseResponse<?> createPost(
            @PathVariable Long boardId,
            @RequestBody @Valid PostRequest request) {
        PostResponse response = postService.createPost(boardId, request);
        return BaseResponse.success(response);
    }

    @GetMapping
    public BaseResponse<?> getAllPosts(@PathVariable Long boardId) {
        return BaseResponse.success(postService.getPostsByBoardId(boardId));
    }

    @GetMapping("/{postId}")
    public BaseResponse<?> getPostById(@PathVariable Long boardId, @PathVariable Long postId) {
        return BaseResponse.success(postService.getPostById(postId));
    }

    @PutMapping("/{postId}")
    public BaseResponse<?> updatePost(
            @PathVariable Long boardId,
            @PathVariable Long postId,
            @RequestBody @Valid PostRequest request) {
        PostResponse response = postService.updatePost(postId, request);
        return BaseResponse.success(response);
    }

    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public BaseResponse<?> deletePost(
            @PathVariable Long boardId,
            @PathVariable Long postId) {
        postService.deletePost(postId);
        return BaseResponse.success(null);
    }
}
