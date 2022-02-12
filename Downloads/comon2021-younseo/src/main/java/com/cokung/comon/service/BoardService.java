package com.cokung.comon.service;

import com.cokung.comon.domain.entity.Board;
import com.cokung.comon.domain.entity.Category;
import com.cokung.comon.domain.repository.BoardRepository;
import com.cokung.comon.domain.repository.CategoryRepository;
import com.cokung.comon.dto.BoardDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BoardService {
    private BoardRepository boardRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Transactional
    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    @Transactional
    public BoardDto findById(Long id, Long category) {
        Optional<Board> postOptional = boardRepository.findById(id, category);
        // 찾는 게시글이 있으면
        if(postOptional.isPresent()) {
            Board board = postOptional.get();
            // BoardDto로 반환
            return BoardDto.builder()
                    .title(board.getTitle())
                    .content(board.getContent())
                    .author(board.getAuthor())
                    .createdDate((Date) board.getCreatedDate())
                    .modifiedDate(board.getModifiedDate())
                    .readCount(board.getReadCount())
//                    .categoryId(board.getCategoryId())
                    .build();
        } else {
            // 없으면 null 반환
            return null;
        }
    }

    @Transactional
    public Long insertBoard(BoardDto boardDto) {
        return boardRepository.save(boardDto.toEntity()).getId();
    }

    @Transactional
    public boolean updateBoard(Long id, BoardDto boardDto) {
        // 찾는 게시물 있으면
        if(boardRepository.findById(id).isPresent()) {
            boardDto.setId(id);
            this.insertBoard(boardDto);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public boolean deleteBoard(Long id) {
        if(boardRepository.findById(id).isPresent()) {
            boardRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public List<Board> findByCategory(Long category) {
        return boardRepository.findByCategory(category);
    }

}
