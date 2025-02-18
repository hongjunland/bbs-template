package com.hongjunland.bbstemplate.comment.application;

import com.hongjunland.bbstemplate.post.application.CommentService;
import com.hongjunland.bbstemplate.post.domain.Post;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hongjunland.bbstemplate.post.domain.Comment;
import com.hongjunland.bbstemplate.post.dto.CommentRequest;
import com.hongjunland.bbstemplate.post.dto.CommentResponse;
import com.hongjunland.bbstemplate.post.infrastructure.post.CommentJpaRepository;
import com.hongjunland.bbstemplate.post.infrastructure.post.PostJpaRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentJpaRepository commentJpaRepository;

    @Mock
    private PostJpaRepository postJpaRepository;

    @InjectMocks
    private CommentService commentService;

    @Mock
    private EntityManager entityManager;

    private Post post;
    private Comment comment;

    @BeforeEach
    void setup() {
        post = Post.builder()
                .id(1L)
                .title("게시글 제목")
                .content("게시글 내용")
                .author("작성자")
                .build();

        comment = Comment.builder()
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
        when(commentJpaRepository.save(any(Comment.class))).thenReturn(comment);

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
    void 게시글에_댓글_목록_조회() {
        // given
        when(postJpaRepository.existsById(post.getId())).thenReturn(true);
        when(commentJpaRepository.findByPostId(post.getId())).thenReturn(List.of(comment));

        // when
        List<CommentResponse> responses = commentService.getCommentsByPostId(post.getId());

        // then
        assertThat(responses).isNotEmpty();
        assertThat(responses.get(0).id()).isEqualTo(comment.getId());
    }

    @Test
    void 존재하지_않는_게시글에_댓글_조회() {
        // given
        when(postJpaRepository.existsById(post.getId())).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> commentService.getCommentsByPostId(post.getId()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("게시글이 존재하지 않습니다.");
    }

    @Test
    void 댓글_수정() {
        // given
        CommentRequest request = CommentRequest.builder()
                .author("댓글 작성자")
                .content("수정된 내용")
                .build();
        Comment updateComment = Comment.builder()
                .id(1L)
                .post(post)
                .author("댓글 작성자")
                .content("댓글 내용")
                .build();
        when(commentJpaRepository.findById(updateComment.getId())).thenReturn(Optional.of(updateComment));
        updateComment.update("수정된 내용");
        // when
        CommentResponse responses = commentService.updateComment(comment.getId(), request);

        // then
        assertThat(responses.id()).isEqualTo(updateComment.getId());
        assertThat(responses.author()).isEqualTo("댓글 작성자");
        assertThat(responses.content()).isEqualTo("수정된 내용");
    }

    @Test
    void 존재하지_않는_댓글_수정() {
        // given
        CommentRequest request = CommentRequest.builder()
                .author("댓글 작성자")
                .content("수정된 내용")
                .build();
        Comment updateComment = Comment.builder()
                .id(1L)
                .post(post)
                .author("댓글 작성자")
                .content("댓글 내용")
                .build();
        when(commentJpaRepository.findById(updateComment.getId())).thenThrow(new EntityNotFoundException("게시글이 존재하지 않습니다."));

        // when & then
        assertThatThrownBy(() -> commentService.updateComment(comment.getId(), request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("게시글이 존재하지 않습니다.");

    }

    @Test
    void 댓글_삭제() {
        // given
        CommentRequest request = CommentRequest.builder()
                .author("댓글 작성자")
                .content("수정된 내용")
                .build();
        when(commentJpaRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
        // when
        commentService.deleteComment(comment.getId());

    }

    @Test
    void 존재하지_않는_댓글_삭제() {
        // given
        when(commentJpaRepository.findById(comment.getId())).thenThrow(new EntityNotFoundException("댓글을 찾을 수 없습니다."));
        // when & then
        assertThatThrownBy(() -> commentService.deleteComment(comment.getId()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("댓글을 찾을 수 없습니다.");
    }

}
