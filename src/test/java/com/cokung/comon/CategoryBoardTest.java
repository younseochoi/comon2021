package com.cokung.comon;

import com.cokung.comon.domain.entity.Board;
import com.cokung.comon.domain.entity.Category;
import com.cokung.comon.domain.repository.BoardRepository;
import com.cokung.comon.domain.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@SpringBootTest
public class CategoryBoardTest {

    @Autowired
    BoardRepository boardRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @Test
    public void 카테고리_만들기_테스트() {
        Category category1 = new Category(null, "카테고리1");
        categoryRepository.save(category1);
        Category category2 = new Category(null, "카테고리2");
        categoryRepository.save(category2);
        Category category3 = new Category(null, "카테고리3");
        categoryRepository.save(category3);
        Category category4 = new Category(null, "카테고리4");
        categoryRepository.save(category4);
    }

    @Test
    public void 게시글_만들기_테스트() {
        Category category = categoryRepository.findById(10L).get();
        Board board = new Board().builder()
                .title("카테고리 테스트")
                .content("이거 테스트입니다.")
                .author("도훈")
                .createdDate(LocalDateTime.now())
                .category(category)
                .build();
        Board resultBoard = boardRepository.save(board);
    }

    @Test
    public void 카테고리_삭제_테스트() {
        categoryRepository.deleteAll();
    }

}
