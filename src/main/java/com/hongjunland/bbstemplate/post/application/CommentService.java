package com.hongjunland.bbstemplate.post.application;

import com.hongjunland.bbstemplate.post.domain.CommentLike;
import com.hongjunland.bbstemplate.post.infrastructure.CommentLikeJpaRepository;
import com.hongjunland.bbstemplate.post.domain.Post;
import com.hongjunland.bbstemplate.user.domain.User;
import com.hongjunland.bbstemplate.user.infrastructure.UserJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hongjunland.bbstemplate.post.domain.Comment;
import com.hongjunland.bbstemplate.post.dto.CommentRequest;
import com.hongjunland.bbstemplate.post.dto.CommentResponse;
import com.hongjunland.bbstemplate.post.infrastructure.CommentJpaRepository;
import com.hongjunland.bbstemplate.post.infrastructure.PostJpaRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentJpaRepository commentJpaRepository;
    private final PostJpaRepository postJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final CommentLikeJpaRepository commentLikeJpaRepository;

    /**
     * 댓글(또는 대댓글) 생성
     */
    @Transactional
    public CommentResponse createComment(Long postId, CommentRequest request) {
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));

        // 부모 댓글이 존재하면 대댓글, 아니면 일반 댓글 생성
        Comment parent = null;
        if (request.parentId() != null) {
            parent = commentJpaRepository.findById(request.parentId())
                    .orElseThrow(() -> new EntityNotFoundException("부모 댓글이 존재하지 않습니다."));
        }

        Comment comment = Comment.builder()
                .post(post)
                .parent(parent)
                .author(request.author())
                .content(request.content())
                .build();
        post.addComment(comment);
        commentJpaRepository.save(comment);
        return toCommentResponse(comment);
    }

    /**
     * 특정 게시글의 루트 댓글 목록 조회
     */
    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByPostId(Long postId) {
        if (!postJpaRepository.existsById(postId)) {
            throw new EntityNotFoundException("게시글이 존재하지 않습니다.");
        }

        return commentJpaRepository.findByPostIdAndParentIsNull(postId)
                .stream()
                .map(this::toCommentResponse)
                .toList();
    }

    /**
     * 댓글 수정
     */
    @Transactional
    public CommentResponse updateComment(Long commentId, CommentRequest request) {
        Comment comment = commentJpaRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));

        comment.update(request.content());
        return toCommentResponse(comment);
    }

    /**
     * 특정 댓글의 대댓글(답글) 조회
     */
    @Transactional(readOnly = true)
    public List<CommentResponse> getReplies(Long commentId) {
        Comment parent = commentJpaRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글이 존재하지 않습니다."));

        return commentJpaRepository.findByParent(parent)
                .stream()
                .map(reply -> CommentResponse.builder()
                        .id(reply.getId())
                        .postId(reply.getPost().getId())
                        .parentId(parent.getId())
                        .author(reply.getAuthor())
                        .content(reply.getContent())
                        .replyCount(commentJpaRepository.countByParentId(reply.getId()))
                        .likeCount(commentLikeJpaRepository.countByCommentId(reply.getId()))
                        .createdAt(reply.getCreatedAt())
                        .updatedAt(reply.getUpdatedAt())
                        .build())
                .toList();
    }

    /**
     * 댓글 삭제
     */
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentJpaRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));
        comment.getPost().removeComment(comment); // ✅ Post에서 댓글 제거
    }

    /**
     * 댓글 좋아요 토글 기능 (이미 좋아요한 경우 취소, 아니면 추가)
     */
    @Transactional
    public boolean toggleCommentLike(Long commentId, Long userId) {
        User user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
        Comment comment = commentJpaRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));

        Optional<CommentLike> existingLike = commentLikeJpaRepository.findByCommentAndUser(comment, user);

        if (existingLike.isPresent()) {
            commentLikeJpaRepository.delete(existingLike.get()); // 좋아요 취소
            return false;
        } else {
            commentLikeJpaRepository.save(CommentLike.builder()
                    .comment(comment)
                    .user(user)
                    .build()); // 좋아요 추가
            return true;
        }
    }

    /**
     * 도메인 객체(Comment)를 DTO(CommentResponse)로 변환하는 매핑 메서드
     */
    private CommentResponse toCommentResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .author(comment.getAuthor())
                .content(comment.getContent())
                .replyCount(commentJpaRepository.countByParentId(comment.getId()))
                .likeCount(commentLikeJpaRepository.countByCommentId(comment.getId()))
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}

