package com.hongjunland.boardtemplate.comment.application;

import com.hongjunland.boardtemplate.comment.domain.CommentJpaEntity;
import com.hongjunland.boardtemplate.comment.dto.CommentRequest;
import com.hongjunland.boardtemplate.comment.dto.CommentResponse;
import com.hongjunland.boardtemplate.comment.infrastructure.CommentJpaRepository;
import com.hongjunland.boardtemplate.post.domain.PostJpaEntity;
import com.hongjunland.boardtemplate.post.infrastructure.PostJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentJpaRepository commentJpaRepository;

    @Mock
    private PostJpaRepository postJpaRepository;

    @InjectMocks
    private CommentService commentService;

    private PostJpaEntity post;
    private CommentJpaEntity comment;

    @BeforeEach
    void setup() {
        post = PostJpaEntity.builder()
                .id(1L)
                .title("게시글 제목")
                .content("게시글 내용")
                .author("작성자")
                .build();

        comment = CommentJpaEntity.builder()
                .id(1L)
                .post(post)
                .author("댓글 작성자")
                .content("댓글 내용")
                .build();
    }

    /**
     * ✅ 댓글 생성 테스트
     */
    @Test
    void 댓글_생성_테스트() {
        // given
        CommentRequest request = CommentRequest.builder()
                .author("댓글 작성자")
                .content("댓글 내용")
                .build();

        when(postJpaRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(commentJpaRepository.save(any(CommentJpaEntity.class))).thenReturn(comment);

        // when
        CommentResponse response = commentService.createComment(post.getId(), request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.author()).isEqualTo(request.author());
        assertThat(response.content()).isEqualTo(request.content());
    }

    @Test
    void 존재하지_않는_게시글에_댓글_추가() {
        // given
        CommentRequest request = CommentRequest.builder()
                .author("댓글 작성자")
                .content("댓글 내용")
                .build();

        when(postJpaRepository.findById(post.getId())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> commentService.createComment(post.getId(), request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("게시글이 존재하지 않습니다.");
    }
    /**
     * ✅ 특정 게시글의 댓글 목록 조회 테스트
     */
    @Test
    void 게시글_댓글_조회_성공() {
        // given
        when(commentJpaRepository.findByPostId(post.getId())).thenReturn(List.of(comment));

        // when
        List<CommentResponse> responses = commentService.getCommentsByPostId(post.getId());

        // then
        assertThat(responses).isNotEmpty();
        assertThat(responses.get(0).id()).isEqualTo(comment.getId());
    }

}
