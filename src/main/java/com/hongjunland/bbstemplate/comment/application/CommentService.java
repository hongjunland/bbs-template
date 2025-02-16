package com.hongjunland.bbstemplate.comment.application;

import com.hongjunland.bbstemplate.comment.domain.CommentLikeJpaEntity;
import com.hongjunland.bbstemplate.comment.infrastructure.CommentLikeJpaRepository;
import com.hongjunland.bbstemplate.user.domain.UserJpaEntity;
import com.hongjunland.bbstemplate.user.infrastructure.UserJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hongjunland.bbstemplate.comment.domain.CommentJpaEntity;
import com.hongjunland.bbstemplate.comment.dto.CommentRequest;
import com.hongjunland.bbstemplate.comment.dto.CommentResponse;
import com.hongjunland.bbstemplate.comment.infrastructure.CommentJpaRepository;
import com.hongjunland.bbstemplate.post.domain.PostJpaEntity;
import com.hongjunland.bbstemplate.post.infrastructure.PostJpaRepository;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentJpaRepository commentJpaRepository;
    private final PostJpaRepository postJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final CommentLikeJpaRepository commentLikeJpaRepository;
    @Transactional
    public CommentResponse createComment(Long postId, CommentRequest request) {
        PostJpaEntity post = postJpaRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));

        CommentJpaEntity parent = request.parentId() != null
                ? commentJpaRepository.findById(request.parentId())
                .orElseThrow(() -> new EntityNotFoundException("부모 댓글이 존재하지 않습니다."))
                : null;

        CommentJpaEntity comment = commentJpaRepository.save(CommentJpaEntity.builder()
                .post(post)
                .parent(parent)
                .author(request.author())
                .content(request.content())
                .likeCount(0) // 초기 좋아요 수 0
                .build());

        return CommentResponse.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .parentId(parent != null ? parent.getId() : null)
                .author(comment.getAuthor())
                .content(comment.getContent())
                .likeCount(comment.getLikeCount())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByPostId(Long postId) {
        if (!postJpaRepository.existsById(postId)) {
            throw new EntityNotFoundException("게시글이 존재하지 않습니다.");
        }

        return commentJpaRepository.findByPostIdAndParentIsNull(postId).stream()
                .map(comment -> CommentResponse.builder()
                        .id(comment.getId())
                        .postId(comment.getPost().getId())
                        .parentId(null) // 루트 댓글이므로 부모 없음
                        .author(comment.getAuthor())
                        .content(comment.getContent())
                        .replyCount(commentJpaRepository.countByParentId(comment.getId())) // 대댓글수
                        .likeCount(commentLikeJpaRepository.countByCommentId(comment.getId()))
                        .createdAt(comment.getCreatedAt())
                        .updatedAt(comment.getUpdatedAt())
                        .build())
                .toList();
    }

    @Transactional
    public CommentResponse updateComment(Long commentId, CommentRequest request) {
        CommentJpaEntity comment = commentJpaRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));

        comment.update(request.content());
        return CommentResponse.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .author(comment.getAuthor())
                .content(comment.getContent())
                .likeCount(comment.getLikeCount())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt()) // JPA Auditing 반영
                .build();
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getReplies(Long commentId) {
        CommentJpaEntity parent = commentJpaRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글이 존재하지 않습니다."));

        return commentJpaRepository.findByParent(parent).stream()
                .map(reply -> CommentResponse.builder()
                        .id(reply.getId())
                        .postId(reply.getPost().getId())
                        .parentId(parent.getId())
                        .author(reply.getAuthor())
                        .content(reply.getContent())
                        .replyCount(commentJpaRepository.countByParentId(reply.getId()))
                        .likeCount(reply.getLikeCount())
                        .createdAt(reply.getCreatedAt())
                        .updatedAt(reply.getUpdatedAt())
                        .build())
                .toList();
    }

    @Transactional
    public void deleteComment(Long commentId) {
        CommentJpaEntity comment = commentJpaRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));

        commentJpaRepository.delete(comment);
    }

    /**
     * ✅ 댓글 좋아요 기능 (토글 방식)
     */
    @Transactional
    public boolean toggleCommentLike(Long commentId, Long userId) {
        CommentJpaEntity comment = commentJpaRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));

        UserJpaEntity user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        boolean alreadyLiked = commentLikeJpaRepository.existsByCommentAndUser(comment, user);

        if (alreadyLiked) {
            commentLikeJpaRepository.deleteByCommentAndUser(comment, user);
            return false; // ✅ 좋아요 취소됨
        } else {
            commentLikeJpaRepository.save(CommentLikeJpaEntity.builder()
                    .comment(comment)
                    .user(user)
                    .build());
            return true; // ✅ 좋아요 추가됨
        }
    }

}

