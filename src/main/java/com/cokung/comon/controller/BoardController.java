package com.cokung.comon.controller;

import com.cokung.comon.DefaultResponse;
import com.cokung.comon.ResponseMessage;
import com.cokung.comon.StatusCode;
import com.cokung.comon.domain.entity.Board;
import com.cokung.comon.dto.BoardDto;
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
        if(category != null && id == null) {
            List<Board> boardDtos = boardService.findAll(); // TODO: 2022/01/05  findByCategory() 메소드 정의 해야 함. DTO 반환하게 해야 함.
            return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessage.READ_POST, boardDtos), HttpStatus.OK);
        } else if(category != null && id != null) {
            BoardDto boardDto = boardService.findById(id); // TODO: 2022/01/05 findById로 Board 찾고 category아이디 같은지 검사하는 코드 Service에 추가해야함.
            return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessage.READ_POST, boardDto), HttpStatus.OK);
        }else {
            return new ResponseEntity(DefaultResponse.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_POST), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity insertBoard(@RequestBody BoardDto boardDto) {
        Long id = boardService.insertBoard(boardDto);
        return new ResponseEntity(DefaultResponse.res(StatusCode.CREATED, ResponseMessage.POST_SUCCESS, id), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity updateBoard(@RequestParam(required = true) Long id, @RequestBody BoardDto boardDto) {
        if (boardService.updateBoard(id, boardDto)) {
            return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessage.UPDATE_POST, id), HttpStatus.OK);
        } else {
            return new ResponseEntity(DefaultResponse.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_POST), HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping
    public ResponseEntity deleteBoard(@RequestParam(required = true) Long id) {
        if(boardService.deleteBoard(id)) {
            return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessage.DELETE_POST, id), HttpStatus.OK);
        } else {
            return new ResponseEntity(DefaultResponse.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_POST), HttpStatus.OK);
        }
    }
}