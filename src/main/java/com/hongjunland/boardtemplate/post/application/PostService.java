package com.hongjunland.boardtemplate.post.application;

import com.hongjunland.boardtemplate.board.domain.BoardJpaEntity;
import com.hongjunland.boardtemplate.board.infrastructure.BoardJpaRepository;
import com.hongjunland.boardtemplate.post.domain.PostJpaEntity;
import com.hongjunland.boardtemplate.post.dto.PostRequest;
import com.hongjunland.boardtemplate.post.dto.PostResponse;
import com.hongjunland.boardtemplate.post.infrastructure.PostJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostJpaRepository postJpaRepository;
    private final BoardJpaRepository boardJpaRepository;

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
                .map((entity) ->
                        PostResponse.builder()
                                .id(entity.getId())
                                .boardId(entity.getBoard().getId())
                                .boardName(entity.getBoard().getName())
                                .author(entity.getAuthor())
                                .title(entity.getTitle())
                                .content(entity.getContent())
                                .createdAt(entity.getCreatedAt())
                                .updatedAt(entity.getUpdatedAt())
                                .build()
                )
                .toList();
    }

    @Transactional(readOnly = true)
    public PostResponse getPostById(Long postId) {
        PostJpaEntity post = postJpaRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
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


}
