package com.cokung.comon.service;

import com.cokung.comon.domain.entity.Board;
import com.cokung.comon.domain.entity.Category;
import com.cokung.comon.domain.repository.BoardRepository;
import com.cokung.comon.domain.repository.CategoryRepository;
import com.cokung.comon.dto.BoardDto;
import com.cokung.comon.response.exception.CustomException;
import com.cokung.comon.response.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BoardService {
    private BoardRepository boardRepository;
    private CategoryRepository categoryRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository, CategoryRepository categoryRepository) {
        this.boardRepository = boardRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public List<BoardDto> findAll() {
        List<BoardDto> boardDtos = new ArrayList<>();
        boardRepository.findAll().forEach(board -> boardDtos.add(board.toDto()));
        return boardDtos;
    }

    @Transactional
    public BoardDto findById(Long boardId, Long categoryId) {
        Category targetCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
        return boardRepository.findById(boardId, targetCategory.getCategoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND))
                .toDto();
    }

    @Transactional
    public List<Board> findByCategory(Long categoryId) {
        Category targetCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
        return boardRepository.findByCategory(targetCategory.getCategoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
    }

    @Transactional
    public Long insertBoard(BoardDto boardDto) {
        Category targetCategory = categoryRepository.findById(boardDto.getCategoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
        return boardRepository.save(boardDto.toEntity(targetCategory)).getBoardId();
    }

    @Transactional
    public BoardDto updateBoard(Long boardId, BoardDto boardDto) {
        Category targetCategory = categoryRepository.findById(boardDto.getCategoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
        boardDto.setBoardId(boardId);
        return boardRepository.save(boardDto.toEntity(targetCategory)).toDto();
    }

    @Transactional
    public BoardDto deleteBoard(Long id) {
        Board targetBoard = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        boardRepository.deleteById(targetBoard.getBoardId());
        return targetBoard.toDto();
    }
}
