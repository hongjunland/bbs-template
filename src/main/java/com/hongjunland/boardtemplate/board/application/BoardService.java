package com.hongjunland.boardtemplate.board.application;

import com.hongjunland.boardtemplate.board.domain.BoardJpaEntity;
import com.hongjunland.boardtemplate.board.dto.BoardRequest;
import com.hongjunland.boardtemplate.board.dto.BoardResponse;
import com.hongjunland.boardtemplate.board.infrastructure.BoardJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardJpaRepository boardJpaRepository;

    @Transactional
    public BoardResponse createBoard(BoardRequest request) {
        BoardJpaEntity board = boardJpaRepository.save(
                BoardJpaEntity.builder()
                        .name(request.name())
                        .description(request.description())
                        .build()
        );
        return new BoardResponse(board.getId(), board.getName(), board.getDescription());
    }

    public BoardResponse getBoardById(Long boardId) {
        BoardJpaEntity boardJpaEntity = boardJpaRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시판이 존재하지 않습니다."));
        BoardResponse boardResponse = BoardResponse.builder()
                .boardId(boardJpaEntity.getId())
                .name(boardJpaEntity.getName())
                .description(boardJpaEntity.getDescription())
                .build();
        return boardResponse;
    }

    public List<BoardResponse> getAllBoards() {
        List<BoardJpaEntity> boardJpaEntityList = boardJpaRepository.findAll();
        List<BoardResponse> responseList = boardJpaEntityList.stream()
                .map((entity) ->
                        BoardResponse.builder()
                                .boardId(entity.getId())
                                .name(entity.getName())
                                .description(entity.getDescription())
                                .build()
                )
                .toList();
        return responseList;
    }
    @Transactional
    public void deleteBoard(Long boardId) {
        if (!boardJpaRepository.existsById(boardId)) {
            throw new EntityNotFoundException("해당 게시판이 존재하지 않습니다.");
        }
        boardJpaRepository.deleteById(boardId);
    }

    @Transactional
    public BoardResponse updateBoard(Long boardId, BoardRequest request) {
        BoardJpaEntity board = boardJpaRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시판이 존재하지 않습니다."));
        board.update(request.name(), request.description());
        return new BoardResponse(board.getId(), board.getName(), board.getDescription());
    }
}
