package com.booklibrary.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "books")
public class Book {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "title", nullable = false, length = 255)
    private String title;
    
    @Column(name = "author", nullable = false, length = 255)
    private String author;
    
    @Column(name = "year")
    private Integer publicationYear;
    
    @Column(name = "isbn", unique = true, length = 20)
    private String isbn;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructor por defecto
    public Book() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Constructor con parámetros
    public Book(String title, String author, Integer publicationYear, String isbn) {
        this();
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.isbn = isbn;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
        this.updatedAt = LocalDateTime.now();
    }
    
    public Integer getPublicationYear() {
        return publicationYear;
    }
    
    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getIsbn() {
        return isbn;
    }
    
    public void setIsbn(String isbn) {
        this.isbn = isbn;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publicationYear=" + publicationYear +
                ", isbn='" + isbn + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Book book = (Book) o;
        
        return id != null ? id.equals(book.id) : book.id == null;
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
} 