package com.hongjunland.boardtemplate.board.application;

import com.hongjunland.boardtemplate.board.domain.BoardJpaEntity;
import com.hongjunland.boardtemplate.board.dto.BoardRequest;
import com.hongjunland.boardtemplate.board.dto.BoardResponse;
import com.hongjunland.boardtemplate.board.infrastructure.BoardJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {
    @Mock
    private BoardJpaRepository boardJpaRepository;

    @InjectMocks
    private BoardService boardService;

    @Test
    public void 게시판_생성_테스트() {
        // given
        String name = "공지사항";
        String description = "공지사항 게시판";
        BoardRequest request = new BoardRequest(name, description);

        when(boardJpaRepository.save(any(BoardJpaEntity.class)))
                .thenReturn(
                        BoardJpaEntity.builder()
                                .id(1L)
                                .name(request.name())
                                .description(request.description())
                                .build()
                );

        // when
        BoardResponse response = boardService.createBoard(request);

        // then
        assertNotNull(response);
        assertEquals(request.name(), response.name());
    }

    @Test
    public void 게시판_단일_조회_테스트() {
        // given
        Long boardId = 1L;
        String name = "공지사항";
        String description = "공지사항 게시판";
        BoardJpaEntity boardJpaEntity =
                BoardJpaEntity.builder()
                        .id(boardId)
                        .name(name)
                        .description(description)
                        .build();

        when(boardJpaRepository.findById(boardId)).thenReturn(Optional.of(boardJpaEntity));

        // when
        BoardResponse response = boardService.getBoardById(boardId);

        // then
        assertNotNull(response);
        assertEquals(name, response.name());
    }

    @Test
    public void 존재하지_않는_게시판_단일_조회_테스트() {
        // given
        Long boardId = 999L;
        when(boardJpaRepository.findById(boardId)).thenReturn(Optional.empty());

        // when & then
        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> boardService.getBoardById(boardId)
        );

        assertEquals("해당 게시판이 존재하지 않습니다.", thrown.getMessage());
    }


    @Test
    public void 모든_게시판_조회_테스트() {
        // given
        List<BoardJpaEntity> boards = List.of(
                BoardJpaEntity.builder()
                        .id(1L)
                        .name("공지사항")
                        .description("공지사항 게시판")
                        .build(),
                BoardJpaEntity.builder()
                        .id(2L)
                        .name("자유게시판")
                        .description("자유롭게 대화하는 게시판")
                        .build()
        );

        when(boardJpaRepository.findAll()).thenReturn(boards);

        // when
        List<BoardResponse> responses = boardService.getAllBoards();

        // then
        assertEquals(2, responses.size());
        assertEquals(1L, responses.get(0).boardId());
        assertEquals("공지사항", responses.get(0).name());
        assertEquals("공지사항 게시판", responses.get(0).description());
    }

    @Test
    public void 게시판_수정_테스트() {
        // given
        Long boardId = 1L;
        BoardRequest request = BoardRequest.builder()
                .name("변경된 공지사항")
                .description("변경된 공지사항 게시판")
                .build();

        BoardJpaEntity existingBoard = BoardJpaEntity.builder()
                .id(boardId)
                .name("공지사항")
                .description("공지사항 게시판")
                .build();

        when(boardJpaRepository.findById(boardId)).thenReturn(Optional.of(existingBoard));

        // when
        BoardResponse response = boardService.updateBoard(boardId, request);

        // then
        assertNotNull(response);
        assertEquals(boardId, response.boardId()); // 수정된 boardId 검증
        assertEquals(request.name(), response.name());
        assertEquals(request.description(), response.description());
    }

    @Test
    public void 존재하지_않는_게시판_수정_테스트() {
        // given
        Long boardId = 999L;
        BoardRequest request = BoardRequest.builder()
                .name("변경된 공지사항")
                .description("변경된 공지사항 게시판")
                .build();

        when(boardJpaRepository.findById(boardId)).thenReturn(Optional.empty());

        // when & then
        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> boardService.updateBoard(boardId, request)
        );

        assertEquals("게시판을 찾을 수 없습니다.", thrown.getMessage());
    }





    @Test
    public void 게시판_삭제_테스트() {
        // given
        Long boardId = 1L;

        when(boardJpaRepository.existsById(boardId)).thenReturn(true);
        doNothing().when(boardJpaRepository).deleteById(boardId);

        // when
        boardService.deleteBoard(boardId);

        // then
        verify(boardJpaRepository, times(1)).deleteById(boardId);
    }

    @Test
    public void 존재하지_않는_게시판_삭제_테스트() {
        // given
        Long boardId = 1L;
        when(boardJpaRepository.existsById(boardId)).thenReturn(false);

        // when & then
        assertThrows(EntityNotFoundException.class, () -> boardService.deleteBoard(boardId));
    }

}
