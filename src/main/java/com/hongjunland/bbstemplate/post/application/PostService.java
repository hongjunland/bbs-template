package com.hongjunland.bbstemplate.post.application;

import com.hongjunland.bbstemplate.post.dto.PostSummaryResponse;
import com.hongjunland.bbstemplate.post.infrastructure.CommentJpaRepository;
import com.hongjunland.bbstemplate.post.domain.Post;
import com.hongjunland.bbstemplate.post.domain.PostLike;
import com.hongjunland.bbstemplate.post.infrastructure.PostLikeJpaRepository;
import com.hongjunland.bbstemplate.user.domain.User;
import com.hongjunland.bbstemplate.user.infrastructure.UserJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hongjunland.bbstemplate.board.domain.Board;
import com.hongjunland.bbstemplate.board.infrastructure.BoardJpaRepository;
import com.hongjunland.bbstemplate.post.dto.PostRequest;
import com.hongjunland.bbstemplate.post.dto.PostResponse;
import com.hongjunland.bbstemplate.post.infrastructure.PostJpaRepository;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostJpaRepository postJpaRepository;
    private final BoardJpaRepository boardJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final PostLikeJpaRepository postLikeJpaRepository;
    private final CommentJpaRepository commentJpaRepository;

    @Transactional
    public Long createPost(Long boardId, PostRequest request) {
        Board board = boardJpaRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시판이 존재하지 않습니다."));

        Post post = postJpaRepository.save(Post.builder()
                .board(board)
                .title(request.title())
                .content(request.content())
                .author(request.author())
                .build()
        );
        return post.getId();
    }

    @Transactional(readOnly = true)
    public Page<PostSummaryResponse> getPostsByBoardId(Long boardId, Long userId, Pageable pageable) {
        if(!postJpaRepository.existsById(boardId)){
            throw new EntityNotFoundException("해당 게시판이 존재하지 않습니다.");
        }
        return postJpaRepository.findPostSummaryList(boardId, userId, pageable);
    }

    @Transactional(readOnly = true)
    public PostResponse getPostById(Long postId) {
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        return toPostResponse(post);
    }

    @Transactional
    public PostResponse updatePost(Long postId, PostRequest request) {
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        post.update(request.title(), request.content());
        return toPostResponse(post);
    }

    @Transactional
    public void deletePost(Long postId) {
        if (!postJpaRepository.existsById(postId)) {
            throw new EntityNotFoundException("해당 게시글이 존재하지 않습니다.");
        }
        postJpaRepository.deleteById(postId);
    }


    /**
     * ✅ 게시글 좋아요 기능 (토글 방식)
     */
    @Transactional
    public boolean togglePostLike(Long postId, Long userId) {
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        User user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        boolean alreadyLiked = postLikeJpaRepository.existsByPostAndUser(post, user);

        if (alreadyLiked) {
            postLikeJpaRepository.deleteByPostAndUser(post, user);
            return false; // ✅ 좋아요 취소됨
        } else {
            postLikeJpaRepository.save(PostLike.builder()
                    .post(post)
                    .user(user)
                    .build());
            return true; // ✅ 좋아요 추가됨
        }
    }

    /**
     * 도메인 객체(Post)를 DTO(PostResponse)로 변환하는 매핑 메서드
     */
    private PostResponse toPostResponse(Post post) {
        long likeCount = postLikeJpaRepository.countByPost(post);
        long commentCount = commentJpaRepository.countCommentByPostId(post.getId());
        return PostResponse.builder()
                .id(post.getId())
                .boardId(post.getBoard().getId())
                .boardName(post.getBoard().getName())
                .author(post.getAuthor())
                .title(post.getTitle())
                .content(post.getContent())
                .likeCount(likeCount)
                .commentCount(commentCount)
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
