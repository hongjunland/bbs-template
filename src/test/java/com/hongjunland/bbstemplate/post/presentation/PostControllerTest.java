package com.hongjunland.bbstemplate.post.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hongjunland.bbstemplate.post.application.PostService;
import com.hongjunland.bbstemplate.post.dto.PostRequest;
import com.hongjunland.bbstemplate.post.dto.PostResponse;

import com.hongjunland.bbstemplate.post.dto.PostSummaryResponse;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
@ExtendWith(SpringExtension.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostService postService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Long userId = 1L;

    @Test
    void 게시글_생성_API_테스트() throws Exception {
        // given
        Long boardId = 1L;
        PostRequest request = PostRequest.builder()
                .title("새로운 게시글")
                .content("내용입니다.")
                .author("홍길동")
                .build();

        PostResponse response = PostResponse.builder()
                .id(1L)
                .boardId(boardId)
                .boardName("공지사항")
                .title(request.title())
                .content(request.content())
                .author(request.author())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(postService.createPost(eq(boardId), any(PostRequest.class))).thenReturn(response);

        // when & then
        mockMvc.perform(post("/api/v1/boards/{boardId}/posts", boardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(response.id()))
                .andExpect(jsonPath("$.data.title").value(response.title()));
    }

    @Test
    void 게시글_목록_조회_API_테스트() throws Exception {
        // given
        Long boardId = 1L;
        List<PostSummaryResponse> responses = List.of(
                PostSummaryResponse.builder()
                        .id(1L)
                        .title("첫 번째 게시글").contentSnippet("내용입니다.").author("홍길동")
                        .updatedAt(LocalDateTime.now())
                        .build(),
                PostSummaryResponse.builder()
                        .id(2L)
                        .title("두 번째 게시글").contentSnippet("또 다른 내용").author("이순신")
                        .updatedAt(LocalDateTime.now())
                        .build()
        );

        when(postService.getPostsByBoardId(boardId, userId)).thenReturn(responses);

        // when & then
        mockMvc.perform(get("/api/v1/boards/{boardId}/posts", boardId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.size()").value(responses.size()))
                .andExpect(jsonPath("$.data[0].title").value(responses.get(0).title()))
                .andExpect(jsonPath("$.data[1].title").value(responses.get(1).title()));
    }

    @Test
    void 존재하지_않는_게시글_목록_조회_API_테스트() throws Exception {
        // given
        Long boardId = 1L;

        when(postService.getPostsByBoardId(boardId, userId)).thenThrow(new EntityNotFoundException("존재하지 않는 게시판"));
        // when & then
        mockMvc.perform(get("/api/v1/boards/{boardId}/posts", boardId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("존재하지 않는 게시판"));
    }

    @Test
    void 게시글_단일_조회_API_테스트() throws Exception {
        // given
        Long boardId = 1L;
        Long postId = 1L;
        PostResponse response = PostResponse.builder()
                .id(postId)
                .boardId(boardId)
                .boardName("공지사항")
                .title("첫 번째 게시글")
                .content("내용입니다.")
                .author("홍길동")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(postService.getPostById(postId)).thenReturn(response);

        // when & then
        mockMvc.perform(get("/api/v1/posts/{postId}", postId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(response.id()))
                .andExpect(jsonPath("$.data.title").value(response.title()));
    }

    @Test
    void 존재하지_않는_게시글_조회_API_테스트() throws Exception {
        // given
        Long postId = 999L;
        when(postService.getPostById(postId)).thenThrow(new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        // when & then
        mockMvc.perform(get("/api/v1/posts/{postId}", postId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("게시글을 찾을 수 없습니다."));
    }

    @Test
    void 게시글_수정_API_테스트() throws Exception {
        // given
        Long boardId = 1L;
        Long postId = 1L;
        PostRequest request = PostRequest.builder()
                .title("수정된 제목")
                .content("수정된 내용")
                .build();

        PostResponse response = PostResponse.builder()
                .id(postId)
                .boardId(boardId)
                .boardName("공지사항")
                .title(request.title())
                .content(request.content())
                .author("홍길동")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(postService.updatePost(eq(postId), any(PostRequest.class))).thenReturn(response);

        // when & then
        mockMvc.perform(put("/api/v1/posts/{postId}", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value(request.title()));
    }

    @Test
    void 존재하지_않는_게시글_수정_API_테스트() throws Exception {
        // given
        Long postId = 999L;
        PostRequest request = PostRequest.builder()
                .title("수정된 제목")
                .content("수정된 내용")
                .build();
        when(postService.updatePost(postId, request)).thenThrow(new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        // when & then
        mockMvc.perform(put("/api/v1/posts/{postId}", postId, request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("게시글을 찾을 수 없습니다."));
    }

    @Test
    void 게시글_삭제_API_테스트() throws Exception {
        // given
        Long postId = 1L;
        doNothing().when(postService).deletePost(postId);

        // when & then
        mockMvc.perform(delete("/api/v1/posts/{postId}", postId))
                .andExpect(status().isNoContent());
    }

    @Test
    void 존재하지_않는_게시글_삭제_API_테스트() throws Exception {
        // given
        Long postId = 999L;
        doThrow(new EntityNotFoundException("게시글을 찾을 수 없습니다."))
                .when(postService).deletePost(postId);

        // when & then
        mockMvc.perform(delete("/api/v1/posts/{postId}", postId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("게시글을 찾을 수 없습니다."));
    }
}