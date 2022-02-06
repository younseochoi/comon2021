package com.cokung.comon.controller;

import com.cokung.comon.response.exception.DefaultResponse;
import com.cokung.comon.response.exception.ResponseMessage;
import com.cokung.comon.response.exception.StatusCode;
import com.cokung.comon.domain.entity.Board;
import com.cokung.comon.dto.BoardDto;
import com.cokung.comon.response.success.SuccessCode;
import com.cokung.comon.response.success.SuccessResponse;
import com.cokung.comon.service.BoardService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boards")
public class BoardController {
    private BoardService boardService;

    @Autowired
    BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @ApiOperation(value = "게시글 읽기", notes = "게시글을 읽습니다.\n파라미터가 없으면 모든 게시글, category 만 있으면 해당 category 의 게시글, category, id 모두 있으면 모두 일치하는 게시글")
    @GetMapping
    public ResponseEntity getBoard(@RequestParam(required = false) Long category, @RequestParam(required = false) Long id) {
        Object responseBoardDto = null;
        if(category != null) {
            if(id == null) responseBoardDto = boardService.findByCategory(category);
            else responseBoardDto = boardService.findById(id, category);
        }
        responseBoardDto = boardService.findAll();
        return SuccessResponse.toResponseEntity(SuccessCode.BOARD_FOUND_SUCCESS, responseBoardDto);
    }

    @ApiOperation(value = "게시글 생성", notes = "게시글을 생성합니다.")
    @PostMapping
    public ResponseEntity insertBoard(@RequestBody BoardDto boardDto) {
        return SuccessResponse.toResponseEntity(SuccessCode.BOARD_CREATED, boardService.insertBoard(boardDto));
    }

    @ApiOperation(value = "게시글 수정", notes = "게시글을 수정합니다.")
    @PutMapping
    public ResponseEntity updateBoard(@RequestParam(required = true) Long id, @RequestBody BoardDto boardDto) {
        return SuccessResponse.toResponseEntity(SuccessCode.BOARD_UPDATE_SUCCESS, boardService.updateBoard(id, boardDto));
    }

    @ApiOperation(value = "게시글 삭제", notes = "게시글을 삭제합니다.")
    @DeleteMapping
    public ResponseEntity deleteBoard(@RequestParam(required = true) Long id) {
        return SuccessResponse.toResponseEntity(SuccessCode.BOARD_DELETE_SUCCESS, boardService.deleteBoard(id));
    }
}