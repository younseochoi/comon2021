package com.cokung.comon.domain.entity;

import com.cokung.comon.dto.BoardDto;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Entity
@Builder
public class Board {
    @Id
    @Column(name = "board_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    @NotNull
    private Category category;

    public BoardDto toDto() {
        return BoardDto.builder()
                .boardId(boardId)
                .title(title)
                .content(content)
                .author(author)
                .createdDate(createdDate)
                .modifiedDate(modifiedDate)
                .readCount(readCount)
                .categoryId(category.getCategoryId())
                .build();
    }
}
