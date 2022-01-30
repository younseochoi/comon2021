package com.cokung.comon.domain.repository;

import com.cokung.comon.domain.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query(value = "SELECT * FROM board WHERE category_id = ?",nativeQuery = true)
    List<Board> findByCategory(Long category);

    @Query(value = "SELECT * FROM board WHERE id = ? and category_id = ?",nativeQuery = true)
    Optional<Board> findById(Long id, Long category);
}
