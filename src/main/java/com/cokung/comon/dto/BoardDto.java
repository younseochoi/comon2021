package com.cokung.comon.dto;

import com.cokung.comon.domain.entity.Board;
import com.cokung.comon.domain.entity.Category;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class BoardDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(length = 10, nullable = false)
    private String author;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column
    private LocalDateTime modifiedDate;

    @Column
    private Long readCount;

    @Column
    private Long categoryId;

    public Board toEntity(Category category) {
        return Board.builder()
                .boardId(id)
                .title(title)
                .content(content)
                .author(author)
                .createdDate(createdDate)
                .modifiedDate(modifiedDate)
                .readCount(readCount)
                .category(category)
                .build();
    }
}
