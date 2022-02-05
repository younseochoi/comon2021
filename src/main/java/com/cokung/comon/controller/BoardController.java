package com.cokung.comon.controller;

import com.cokung.comon.response.exception.DefaultResponse;
import com.cokung.comon.response.exception.ResponseMessage;
import com.cokung.comon.response.exception.StatusCode;
import com.cokung.comon.domain.entity.Board;
import com.cokung.comon.dto.BoardDto;
import com.cokung.comon.response.success.SuccessCode;
import com.cokung.comon.response.success.SuccessResponse;
import com.cokung.comon.service.BoardService;
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

    @PostMapping
    public ResponseEntity insertBoard(@RequestBody BoardDto boardDto) {
        return SuccessResponse.toResponseEntity(SuccessCode.BOARD_CREATED, boardService.insertBoard(boardDto));
    }

    @PutMapping
    public ResponseEntity updateBoard(@RequestParam(required = true) Long id, @RequestBody BoardDto boardDto) {
        return SuccessResponse.toResponseEntity(SuccessCode.BOARD_UPDATE_SUCCESS, boardService.updateBoard(id, boardDto));
    }

    @DeleteMapping
    public ResponseEntity deleteBoard(@RequestParam(required = true) Long id) {
        return SuccessResponse.toResponseEntity(SuccessCode.BOARD_DELETE_SUCCESS, boardService.deleteBoard(id));
    }
}