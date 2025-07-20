package com.booklibrary.dao;

import com.booklibrary.entity.Book;
import com.booklibrary.exception.NotFoundException;
import com.booklibrary.exception.ValidationException;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Stateless(name = "BookDAO")
public class BookDAO {
    
    @PersistenceContext(unitName = "BookLibraryPU")
    private EntityManager entityManager;
    
    /**
     * Guardar un nuevo libro
     */
    public Book save(Book book) {
        validateBook(book);
        entityManager.persist(book);
        return book;
    }
    
    /**
     * Actualizar un libro existente
     */
    public Book update(Book book) {
        validateBook(book);
        return entityManager.merge(book);
    }
    
    /**
     * Eliminar un libro por ID
     */
    public boolean deleteById(Long id) {
        Book book = entityManager.find(Book.class, id);
        if (book != null) {
            entityManager.remove(book);
            return true;
        }
        return false;
    }
    
    /**
     * Buscar un libro por ID
     */
    public Optional<Book> findById(Long id) {
        if (id == null) {
            throw new ValidationException("Book ID cannot be null");
        }
        Book book = entityManager.find(Book.class, id);
        return Optional.ofNullable(book);
    }
    
    /**
     * Buscar un libro por ISBN
     */
    public Optional<Book> findByIsbn(String isbn) {
        TypedQuery<Book> query = entityManager.createQuery(
            "SELECT b FROM Book b WHERE b.isbn = :isbn", Book.class);
        query.setParameter("isbn", isbn);
        
        try {
            Book book = query.getSingleResult();
            return Optional.of(book);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    /**
     * Obtener todos los libros
     */
    public List<Book> findAll() {
        TypedQuery<Book> query = entityManager.createQuery(
            "SELECT b FROM Book b ORDER BY b.title", Book.class);
        return query.getResultList();
    }
    
    /**
     * Buscar libros por autor
     */
    public List<Book> findByAuthor(String author) {
        TypedQuery<Book> query = entityManager.createQuery(
            "SELECT b FROM Book b WHERE LOWER(b.author) LIKE LOWER(:author) ORDER BY b.title", Book.class);
        query.setParameter("author", "%" + author + "%");
        return query.getResultList();
    }
    
    /**
     * Buscar libros por título
     */
    public List<Book> findByTitle(String title) {
        TypedQuery<Book> query = entityManager.createQuery(
            "SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(:title) ORDER BY b.title", Book.class);
        query.setParameter("title", "%" + title + "%");
        return query.getResultList();
    }
    
    /**
     * Buscar libros por año de publicación
     */
    public List<Book> findByPublicationYear(Integer year) {
        TypedQuery<Book> query = entityManager.createQuery(
            "SELECT b FROM Book b WHERE b.publicationYear = :year ORDER BY b.title", Book.class);
        query.setParameter("year", year);
        return query.getResultList();
    }
    
    /**
     * Contar el total de libros
     */
    public long count() {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(b) FROM Book b", Long.class);
        return query.getSingleResult();
    }
    
    /**
     * Verificar si existe un libro con el ISBN dado
     */
    public boolean existsByIsbn(String isbn) {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(b) FROM Book b WHERE b.isbn = :isbn", Long.class);
        query.setParameter("isbn", isbn);
        return query.getSingleResult() > 0;
    }
    
    /**
     * Validar un libro antes de guardarlo o actualizarlo
     */
    private void validateBook(Book book) {
        if (book == null) {
            throw new ValidationException("Book cannot be null");
        }
        
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new ValidationException("Book title is required");
        }
        
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            throw new ValidationException("Book author is required");
        }
        
        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            throw new ValidationException("Book ISBN is required");
        }
        
        if (book.getPublicationYear() != null && book.getPublicationYear() < 0) {
            throw new ValidationException("Publication year cannot be negative");
        }
        
        if (book.getPublicationYear() != null && book.getPublicationYear() > 2100) {
            throw new ValidationException("Publication year seems invalid");
        }
    }
} 