package com.library.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Book")
@Data
public class Book {
    @Id
    @Column(length = 20)
    private String isbn;

    @Column(nullable = false)
    private String name;

    @Column(length = 100)
    private String author;

    @Column(columnDefinition = "TEXT")
    private String introduction;
}
