package com.cokung.comon.domain.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Entity
@Builder
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(length = 10, nullable = false)
    private String author;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdDate;

    @LastModifiedDate
    @Column
    private LocalDateTime modifiedDate;

    @Column
    private Long readCount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    @NotNull
    private Category category;
}
