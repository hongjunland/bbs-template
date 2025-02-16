package com.hongjunland.bbstemplate.board.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hongjunland.bbstemplate.board.application.BoardService;
import com.hongjunland.bbstemplate.board.dto.BoardRequest;
import com.hongjunland.bbstemplate.common.response.BaseResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards")
public class BoardController {
    private final BoardService boardService;
    @PostMapping
    public BaseResponse<?> createBoard(@RequestBody @Valid BoardRequest boardRequest){
        return BaseResponse.success(boardService.createBoard(boardRequest));
    }
    @GetMapping
    public BaseResponse<?> getAllBoards(){
        return BaseResponse.success(boardService.getAllBoards());
    }

    @GetMapping("/{boardId}")
    public BaseResponse<?> getBoardById(@PathVariable Long boardId){
        return BaseResponse.success(boardService.getBoardById(boardId));
    }
    @PutMapping("/{boardId}")
    public BaseResponse<?> updateBoardById(@PathVariable Long boardId, @RequestBody BoardRequest boardRequest){
        return BaseResponse.success(boardService.updateBoard(boardId, boardRequest));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{boardId}")
    public BaseResponse<?> deleteBoard(@PathVariable Long boardId){
        boardService.deleteBoard(boardId);
        return BaseResponse.success(null);
    }
}
