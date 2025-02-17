package com.hongjunland.bbstemplate.comment.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hongjunland.bbstemplate.post.application.CommentService;
import com.hongjunland.bbstemplate.post.dto.CommentRequest;
import com.hongjunland.bbstemplate.post.dto.CommentResponse;
import com.hongjunland.bbstemplate.post.presentation.CommentController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
@ExtendWith(MockitoExtension.class)
//@ExtendWith(SpringExtension.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    private CommentResponse commentResponse;

    @BeforeEach
    void setup() {
        commentResponse = CommentResponse.builder()
                .id(1L)
                .postId(1L)
                .author("테스트 사용자")
                .content("테스트 댓글")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * ✅ 댓글 생성 테스트
     */
    @Test
    void 댓글_생성_성공() throws Exception {
        // given
        CommentRequest request = CommentRequest
                .builder()
                .content("새로운 댓글 내용")
                .build();

        when(commentService.createComment(eq(1L), any(CommentRequest.class)))
                .thenReturn(commentResponse);

        // when & then
        mockMvc.perform(post("/api/v1/posts/{postId}/comments", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(commentResponse.id()))
                .andExpect(jsonPath("$.data.postId").value(commentResponse.postId()))
                .andExpect(jsonPath("$.data.author").value(commentResponse.author()))
                .andExpect(jsonPath("$.data.content").value(commentResponse.content()));
    }

//    /**
//     * ❌ 댓글 생성 실패 - 요청 데이터 누락
//     */
//    @Test
//    void 댓글_생성_실패_잘못된_요청() throws Exception {
//        // given
//        CommentRequest request = new CommentRequest(""); // 빈 문자열
//
//        // when & then
//        mockMvc.perform(post("/api/v1/posts/{postId}/comments", 1L)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isBadRequest());
//    }
//
//    /**
//     * ✅ 댓글 목록 조회 테스트
//     */
//    @Test
//    void 댓글_목록_조회() throws Exception {
//        // given
//        List<CommentResponse> comments = List.of(commentResponse);
//        when(commentService.getCommentsByPostId(1L)).thenReturn(comments);
//
//        // when & then
//        mockMvc.perform(get("/api/v1/posts/{postId}/comments", 1L))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data[0].id").value(commentResponse.getId()))
//                .andExpect(jsonPath("$.data[0].postId").value(commentResponse.getPostId()))
//                .andExpect(jsonPath("$.data[0].author").value(commentResponse.getAuthor()))
//                .andExpect(jsonPath("$.data[0].content").value(commentResponse.getContent()));
//    }
//
//    /**
//     * ✅ 댓글 단일 조회 테스트
//     */
//    @Test
//    void 댓글_단일_조회() throws Exception {
//        // given
//        when(commentService.getCommentById(1L)).thenReturn(commentResponse);
//
//        // when & then
//        mockMvc.perform(get("/api/v1/comments/{commentId}", 1L))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data.id").value(commentResponse.getId()))
//                .andExpect(jsonPath("$.data.postId").value(commentResponse.getPostId()))
//                .andExpect(jsonPath("$.data.author").value(commentResponse.getAuthor()))
//                .andExpect(jsonPath("$.data.content").value(commentResponse.getContent()));
//    }
//
//    /**
//     * ❌ 존재하지 않는 댓글 조회
//     */
//    @Test
//    void 존재하지_않는_댓글_조회() throws Exception {
//        // given
//        when(commentService.getCommentById(999L))
//                .thenThrow(new EntityNotFoundException("댓글을 찾을 수 없습니다."));
//
//        // when & then
//        mockMvc.perform(get("/api/v1/comments/{commentId}", 999L))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.success").value(false))
//                .andExpect(jsonPath("$.message").value("댓글을 찾을 수 없습니다."));
//    }
//
//    /**
//     * ✅ 댓글 수정 테스트
//     */
//    @Test
//    void 댓글_수정_성공() throws Exception {
//        // given
//        CommentRequest request = new CommentRequest("수정된 댓글 내용");
//        CommentResponse updatedResponse = CommentResponse.builder()
//                .id(1L)
//                .postId(1L)
//                .author("테스트 사용자")
//                .content(request.content())
//                .createdAt(commentResponse.getCreatedAt())
//                .updatedAt(LocalDateTime.now()) // 수정됨
//                .build();
//
//        when(commentService.updateComment(eq(1L), eq(1L), any(CommentRequest.class)))
//                .thenReturn(updatedResponse);
//
//        // when & then
//        mockMvc.perform(put("/api/v1/posts/{postId}/comments/{commentId}", 1L, 1L)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data.content").value(request.content()));
//    }
//
//    /**
//     * ❌ 존재하지 않는 댓글 수정
//     */
//    @Test
//    void 존재하지_않는_댓글_수정() throws Exception {
//        // given
//        CommentRequest request = new CommentRequest("수정된 댓글 내용");
//
//        when(commentService.updateComment(eq(1L), eq(999L), any(CommentRequest.class)))
//                .thenThrow(new EntityNotFoundException("댓글을 찾을 수 없습니다."));
//
//        // when & then
//        mockMvc.perform(put("/api/v1/posts/{postId}/comments/{commentId}", 1L, 999L)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.success").value(false))
//                .andExpect(jsonPath("$.message").value("댓글을 찾을 수 없습니다."));
//    }
//
//    /**
//     * ✅ 댓글 삭제 테스트
//     */
//    @Test
//    void 댓글_삭제_성공() throws Exception {
//        // given
//        doNothing().when(commentService).deleteComment(1L, 1L);
//
//        // when & then
//        mockMvc.perform(delete("/api/v1/posts/{postId}/comments/{commentId}", 1L, 1L))
//                .andExpect(status().isNoContent());
//    }
}