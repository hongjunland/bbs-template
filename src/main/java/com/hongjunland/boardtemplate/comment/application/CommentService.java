package com.hongjunland.boardtemplate.comment.application;

import com.hongjunland.boardtemplate.comment.domain.CommentJpaEntity;
import com.hongjunland.boardtemplate.comment.dto.CommentRequest;
import com.hongjunland.boardtemplate.comment.dto.CommentResponse;
import com.hongjunland.boardtemplate.comment.infrastructure.CommentJpaRepository;
import com.hongjunland.boardtemplate.post.domain.PostJpaEntity;
import com.hongjunland.boardtemplate.post.infrastructure.PostJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentJpaRepository commentJpaRepository;
    private final PostJpaRepository postJpaRepository;

    @Transactional
    public CommentResponse createComment(Long postId, CommentRequest request) {
        PostJpaEntity post = postJpaRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));

        CommentJpaEntity comment = commentJpaRepository.save(CommentJpaEntity.builder()
                .post(post)
                .author(request.author())
                .content(request.content())
                .build());

        return CommentResponse.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .author(comment.getAuthor())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

    public List<CommentResponse> getCommentsByPostId(Long postId) {
        return commentJpaRepository.findByPostId(postId).stream()
                .map((entity) -> CommentResponse.builder()
                        .id(entity.getId())
                        .postId(entity.getPost().getId())
                        .author(entity.getAuthor())
                        .content(entity.getContent())
                        .createdAt(entity.getCreatedAt())
                        .updatedAt(entity.getUpdatedAt())
                        .build())
                .toList();
    }

//    /**
//     * ✅ 특정 게시글의 댓글 목록 조회
//     */
//    @Transactional(readOnly = true)
//    public List<CommentResponse> getCommentsByPostId(Long postId) {
//        return commentJpaRepository.findByPostId(postId).stream()
//                .map(this::convertToDto)
//                .toList();
//    }
//
//    /**
//     * ✅ 댓글 수정
//     */
//    @Transactional
//    public CommentResponse updateComment(Long postId, Long commentId, CommentRequest request) {
//        CommentJpaEntity comment = commentJpaRepository.findById(commentId)
//                .filter(c -> c.getPost().getId().equals(postId))
//                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));
//
//        comment.update(request.content());
//        return convertToDto(comment);
//    }
//
//    /**
//     * ✅ 댓글 삭제
//     */
//    @Transactional
//    public void deleteComment(Long postId, Long commentId) {
//        CommentJpaEntity comment = commentJpaRepository.findById(commentId)
//                .filter(c -> c.getPost().getId().equals(postId))
//                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));
//
//        commentJpaRepository.delete(comment);
//    }
//
//    /**
//     * ✅ Entity → DTO 변환
//     */
//    private CommentResponse convertToDto(CommentJpaEntity comment) {
//        return CommentResponse.builder()
//                .id(comment.getId())
//                .postId(comment.getPost().getId())
//                .author(comment.getAuthor())
//                .content(comment.getContent())
//                .createdAt(comment.getCreatedAt())
//                .updatedAt(comment.getUpdatedAt())
//                .build();
//    }
}

