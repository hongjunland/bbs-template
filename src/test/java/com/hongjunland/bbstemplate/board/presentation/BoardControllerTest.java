package com.hongjunland.bbstemplate.board.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hongjunland.bbstemplate.board.application.BoardService;
import com.hongjunland.bbstemplate.board.dto.BoardRequest;
import com.hongjunland.bbstemplate.board.dto.BoardResponse;
import com.hongjunland.bbstemplate.board.presentation.BoardController;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BoardController.class)
@ExtendWith(SpringExtension.class)
public class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BoardService boardService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Test
    public void 게시판_생성_성공_테스트() throws Exception {
        // given
        BoardRequest request = BoardRequest.builder()
                .name("공지사항")
                .description("공지사항 게시판")
                .build();

        Long response = 1L;

        when(boardService.createBoard(any(BoardRequest.class))).thenReturn(response);

        // when & then
        mockMvc.perform(post("/api/v1/boards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("요청이 성공적으로 처리되었습니다."))
                .andExpect(jsonPath("$.data").value(response));
    }

    @Test
    public void 게시판_생성_실패_테스트_이름_누락() throws Exception {
        // given
        BoardRequest request = BoardRequest.builder()
                .name("") // 빈 값
                .description("공지사항 게시판")
                .build();

        // when & then
        mockMvc.perform(post("/api/v1/boards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    public void 게시판_목록_조회_테스트() throws Exception {
        // given
        List<BoardResponse> responses = List.of(
                BoardResponse.builder().boardId(1L).name("공지사항").description("공지사항 게시판").build(),
                BoardResponse.builder().boardId(2L).name("자유게시판").description("자유롭게 대화하는 게시판").build()
        );

        when(boardService.getAllBoards()).thenReturn(responses);

        // when & then
        mockMvc.perform(get("/api/v1/boards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("요청이 성공적으로 처리되었습니다."))
                .andExpect(jsonPath("$.data.size()").value(responses.size()))
                .andExpect(jsonPath("$.data[0].boardId").value(responses.get(0).boardId()))
                .andExpect(jsonPath("$.data[1].boardId").value(responses.get(1).boardId()));
    }

    @Test
    public void 게시판_단일_조회_테스트() throws Exception {
        // given
        Long boardId = 999L;
        BoardResponse response = BoardResponse.builder()
                .boardId(boardId)
                .name("공지사항")
                .description("공지사항 게시판")
                .build();

        when(boardService.getBoardById(boardId)).thenReturn(response);

        // when & then
        mockMvc.perform(get("/api/v1/boards/{boardId}", boardId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("요청이 성공적으로 처리되었습니다."))
                .andExpect(jsonPath("$.data.boardId").value(response.boardId()))
                .andExpect(jsonPath("$.data.name").value(response.name()))
                .andExpect(jsonPath("$.data.description").value(response.description()));
    }

    @Test
    public void 존재하지_않는_게시판_조회_테스트() throws Exception {
        // given
        Long boardId = 999L;
        when(boardService.getBoardById(boardId)).thenThrow(new EntityNotFoundException("해당 게시판이 존재하지 않습니다."));

        // when & then
        // when & then
        mockMvc.perform(get("/api/v1/boards/{boardId}", boardId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("해당 게시판이 존재하지 않습니다."));
    }

    @Test
    public void 게시판_수정_테스트() throws Exception {
        // given
        Long boardId = 1L;
        BoardRequest request = BoardRequest.builder()
                .name("수정된 공지사항")
                .description("수정된 공지사항 게시판")
                .build();

        BoardResponse response = BoardResponse.builder()
                .boardId(boardId)
                .name(request.name())
                .description(request.description())
                .build();

        when(boardService.updateBoard(eq(boardId), any(BoardRequest.class))).thenReturn(response);

        // when & then
        mockMvc.perform(put("/api/v1/boards/{boardId}", boardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("요청이 성공적으로 처리되었습니다."))
                .andExpect(jsonPath("$.data.boardId").value(response.boardId()))
                .andExpect(jsonPath("$.data.name").value(response.name()))
                .andExpect(jsonPath("$.data.description").value(response.description()));
    }

    @Test
    public void 존재하지않는_게시판_수정_테스트() throws Exception {
        // given
        Long boardId = 1L;
        BoardRequest request = BoardRequest.builder()
                .name("수정된 공지사항")
                .description("수정된 공지사항 게시판")
                .build();

        doThrow(new EntityNotFoundException("해당 게시판이 존재하지 않습니다."))
                .when(boardService).updateBoard(boardId, request);

        // when & then
        mockMvc.perform(put("/api/v1/boards/{boardId}", boardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("해당 게시판이 존재하지 않습니다."));
    }

    @Test
    public void 게시판_삭제_테스트() throws Exception {
        // given
        Long boardId = 1L;
        doNothing().when(boardService).deleteBoard(boardId);

        // when & then
        mockMvc.perform(delete("/api/v1/boards/{boardId}", boardId))
                .andExpect(status().isNoContent()); // 204 No Content 반환
    }

    @Test
    public void 존재하지_않는_게시판_삭제_테스트() throws Exception {
        // given
        Long boardId = 999L; // 존재하지 않는 ID
        doThrow(new EntityNotFoundException("해당 게시판이 존재하지 않습니다."))
                .when(boardService).deleteBoard(boardId);

        // when & then
        mockMvc.perform(delete("/api/v1/boards/{boardId}", boardId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("해당 게시판이 존재하지 않습니다."));
    }

}
