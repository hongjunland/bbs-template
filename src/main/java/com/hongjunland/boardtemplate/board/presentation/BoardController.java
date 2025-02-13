package com.hongjunland.boardtemplate.board.presentation;

import com.hongjunland.boardtemplate.board.application.BoardService;
import com.hongjunland.boardtemplate.board.dto.BoardRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards")
public class BoardController {
    private final BoardService boardService;
    @PostMapping
    public ResponseEntity<?> createBoard(@RequestBody @Valid BoardRequest boardRequest){
        return ResponseEntity.ok(boardService.createBoard(boardRequest));
    }
    @GetMapping
    public ResponseEntity<?> getAllBoards(){
        return ResponseEntity.ok(boardService.getAllBoards());
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<?> getBoardById(@PathVariable Long boardId){
        return ResponseEntity.ok(boardService.getBoardById(boardId));
    }
    @PutMapping("/{boardId}")
    public ResponseEntity<?> updateBoardById(@PathVariable Long boardId, @RequestBody BoardRequest boardRequest){
        return ResponseEntity.ok(boardService.updateBoard(boardId, boardRequest));
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long boardId){
        boardService.deleteBoard(boardId);
        return ResponseEntity.noContent().build();
    }
}
