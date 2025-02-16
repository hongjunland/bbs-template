package com.hongjunland.bbstemplate.post.application;

import com.hongjunland.bbstemplate.comment.infrastructure.CommentJpaRepository;
import com.hongjunland.bbstemplate.comment.infrastructure.CommentLikeJpaRepository;
import com.hongjunland.bbstemplate.post.domain.PostLikeJpaEntity;
import com.hongjunland.bbstemplate.post.infrastructure.PostLikeJpaRepository;
import com.hongjunland.bbstemplate.user.domain.UserJpaEntity;
import com.hongjunland.bbstemplate.user.infrastructure.UserJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hongjunland.bbstemplate.board.domain.BoardJpaEntity;
import com.hongjunland.bbstemplate.board.infrastructure.BoardJpaRepository;
import com.hongjunland.bbstemplate.post.domain.PostJpaEntity;
import com.hongjunland.bbstemplate.post.dto.PostRequest;
import com.hongjunland.bbstemplate.post.dto.PostResponse;
import com.hongjunland.bbstemplate.post.infrastructure.PostJpaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostJpaRepository postJpaRepository;
    private final BoardJpaRepository boardJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final PostLikeJpaRepository postLikeJpaRepository;
    private final CommentJpaRepository commentJpaRepository;

    @Transactional
    public PostResponse createPost(Long boardId, PostRequest request) {
        BoardJpaEntity board = boardJpaRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시판이 존재하지 않습니다."));

        PostJpaEntity post = postJpaRepository.save(PostJpaEntity.builder()
                .board(board)
                .title(request.title())
                .content(request.content())
                .author(request.author())
                .build()
        );
        return PostResponse.builder()
                .id(post.getId())
                .boardId(post.getBoard().getId())
                .boardName(post.getBoard().getName())
                .author(post.getAuthor())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByBoardId(Long boardId) {
        if(!boardJpaRepository.existsById(boardId)){
            throw new EntityNotFoundException("해당 게시판이 존재하지 않습니다");
        }
        return postJpaRepository.findByBoardId(boardId)
                .stream()
                .map((post) ->
                        PostResponse.builder()
                                .id(post.getId())
                                .boardId(post.getBoard().getId())
                                .boardName(post.getBoard().getName())
                                .author(post.getAuthor())
                                .title(post.getTitle())
                                .content(post.getContent())
                                .commentCount(commentJpaRepository.countCommentJpaEntityByPostId(post.getId()))
                                .likeCount(postLikeJpaRepository.countByPost(post))  // ✅ 좋아요 개수 추가
                                .createdAt(post.getCreatedAt())
                                .updatedAt(post.getUpdatedAt())
                                .build()
                )
                .toList();
    }

    @Transactional(readOnly = true)
    public PostResponse getPostById(Long postId) {
        PostJpaEntity post = postJpaRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        int likeCount = postLikeJpaRepository.countByPost(post);  // ✅ 좋아요 개수 가져오기
        int commentCount = commentJpaRepository.countCommentJpaEntityByPostId(post.getId());  // ✅ 댓글 개수 가져오기
        return PostResponse.builder()
                .id(post.getId())
                .boardId(post.getBoard().getId())
                .boardName(post.getBoard().getName())
                .author(post.getAuthor())
                .title(post.getTitle())
                .likeCount(likeCount)
                .commentCount(commentCount)
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    @Transactional
    public PostResponse updatePost(Long postId, PostRequest request) {
        PostJpaEntity post = postJpaRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        post.update(request.title(), request.content());

        return PostResponse.builder()
                .id(post.getId())
                .boardId(post.getBoard().getId())
                .boardName(post.getBoard().getName())
                .author(post.getAuthor())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
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
        PostJpaEntity post = postJpaRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        UserJpaEntity user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        boolean alreadyLiked = postLikeJpaRepository.existsByPostAndUser(post, user);

        if (alreadyLiked) {
            postLikeJpaRepository.deleteByPostAndUser(post, user);
            return false; // ✅ 좋아요 취소됨
        } else {
            postLikeJpaRepository.save(PostLikeJpaEntity.builder()
                    .post(post)
                    .user(user)
                    .build());
            return true; // ✅ 좋아요 추가됨
        }
    }
}
