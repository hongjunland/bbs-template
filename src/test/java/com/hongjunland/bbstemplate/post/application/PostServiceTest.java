package com.hongjunland.bbstemplate.post.application;

import com.hongjunland.bbstemplate.post.domain.Post;
import com.hongjunland.bbstemplate.post.dto.PostSummaryResponse;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hongjunland.bbstemplate.board.domain.Board;
import com.hongjunland.bbstemplate.board.infrastructure.BoardJpaRepository;
import com.hongjunland.bbstemplate.post.dto.PostRequest;
import com.hongjunland.bbstemplate.post.dto.PostResponse;
import com.hongjunland.bbstemplate.post.infrastructure.post.PostJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostJpaRepository postJpaRepository;

    @Mock
    private BoardJpaRepository boardJpaRepository;

    @InjectMocks
    private PostService postService;

    private Board board;
    private Post post;
    private Long userId;

    @BeforeEach
    void setup() {
        board = Board.builder()
                .id(1L)
                .name("공지사항")
                .description("공지사항 게시판")
                .build();

        post = Post.builder()
                .id(1L)
                .board(board)
                .title("첫 번째 게시글")
                .content("내용입니다.")
                .author("홍길동")
                .build();
        userId = 1L;
    }

    @Test
    void 게시글_생성_테스트() {
        // given
        PostRequest request = PostRequest.builder()
                .boardId(1L)
                .title("첫 번째 게시글")
                .content("내용입니다.")
                .author("홍길동")
                .build();

        when(boardJpaRepository.findById(request.boardId())).thenReturn(Optional.of(board));
        when(postJpaRepository.save(any(Post.class))).thenReturn(post);

        // when
        PostResponse response = postService.createPost(1L, request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.title()).isEqualTo(request.title());
        assertThat(response.content()).isEqualTo(request.content());
        assertThat(response.author()).isEqualTo(request.author());

        verify(postJpaRepository, times(1)).save(any(Post.class));
    }

    @Test
    void 존재하지_않는_게시판에_게시글_작성_테스트() {
        // given
        PostRequest request = PostRequest.builder()
                .boardId(999L)
                .title("제목")
                .content("내용")
                .author("홍길동")
                .build();
        when(boardJpaRepository.findById(request.boardId())).thenReturn(Optional.empty());

        // when & then
        assertThrows(EntityNotFoundException.class, () -> postService.createPost(999L, request));
    }

    @Test
    void 게시글_목록_조회_테스트() {
        // given
        when(boardJpaRepository.existsById(1L)).thenReturn(true);
        when(postJpaRepository.findByBoardId(1L)).thenReturn(List.of(post));

        // when
        Page<PostSummaryResponse> responses = postService.getPostsByBoardId(1L, userId, Pageable.unpaged());

        // then
        assertThat(responses).hasSize(1);
//        assertThat(responses.get(0).title()).isEqualTo(post.getTitle());
//        assertThat(responses.get(0).contentSnippet()).isEqualTo(post.getContent());
    }
    @Test
    void 존재하지_않은_게시판_게시글_목록_조회_테스트() {
        // given
        when(boardJpaRepository.existsById(1L)).thenReturn(false);

        // when & then
        assertThrows(EntityNotFoundException.class, () -> postService.getPostsByBoardId(1L, userId, Pageable.unpaged()));
    }


    @Test
    void 게시글_단일_조회_테스트() {
        // given
        Long postId = 1L;
        when(postJpaRepository.findById(postId)).thenReturn(Optional.of(post));

        // when
        PostResponse response = postService.getPostById(postId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(post.getId());
        assertThat(response.title()).isEqualTo(post.getTitle());
        assertThat(response.content()).isEqualTo(post.getContent());
    }

    @Test
    void 존재하지_않는_게시글_조회_테스트() {
        // given
        Long postId = 999L;
        when(postJpaRepository.findById(postId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(EntityNotFoundException.class, () -> postService.getPostById(postId));
    }

    @Test
    void 게시글_수정_테스트() {
        // given
        PostRequest request = PostRequest.builder()
                .boardId(1L)
                .title("수정된 제목")
                .content("수정된 내용")
                .author("홍길동")
                .build();
        when(postJpaRepository.findById(1L)).thenReturn(Optional.of(post));

        // when
        PostResponse response = postService.updatePost(1L, request);

        // then
        assertThat(response.title()).isEqualTo(request.title());
        assertThat(response.content()).isEqualTo(request.content());
    }

    @Test
    void 존재하지_않는_게시글_수정_테스트() {
        // given
        PostRequest request = PostRequest.builder()
                .boardId(1L)
                .title("수정된 제목")
                .content("수정된 내용")
                .author("홍길동")
                .build();

        when(postJpaRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(EntityNotFoundException.class, () -> postService.updatePost(999L, request));
    }

    @Test
    void 게시글_삭제_테스트() {
        // given
        when(postJpaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(postJpaRepository).deleteById(1L);

        // when
        postService.deletePost(1L);

        // then
        verify(postJpaRepository, times(1)).deleteById(1L);
    }

    @Test
    void 존재하지_않는_게시글_삭제_테스트() {
        // given
        when(postJpaRepository.existsById(999L)).thenReturn(false);

        // when & then
        assertThrows(EntityNotFoundException.class, () -> postService.deletePost(999L));
    }

}
