package com.booklibrary.dao;

import com.booklibrary.entity.Book;
import com.booklibrary.exception.ValidationException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookDAOTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Book> bookQuery;

    @Mock
    private TypedQuery<Long> countQuery;

    @InjectMocks
    private BookDAO bookDAO;

    private Book testBook;

    @BeforeEach
    public void setUp() {
        testBook = new Book("Test Book", "Test Author", 2020, "1234567890123");
        testBook.setId(1L);
    }

    @Test
    public void testSave_ValidBook_ShouldSaveSuccessfully() {
        // Given
        when(entityManager.merge(any(Book.class))).thenReturn(testBook);

        // When
        Book savedBook = bookDAO.save(testBook);

        // Then
        assertNotNull(savedBook);
        assertEquals("Test Book", savedBook.getTitle());
        assertEquals("Test Author", savedBook.getAuthor());
        assertEquals(2020, savedBook.getPublicationYear());
        assertEquals("1234567890123", savedBook.getIsbn());
        
        verify(entityManager).persist(testBook);
    }

    @Test
    public void testSave_BookWithNullTitle_ShouldThrowValidationException() {
        // Given
        testBook.setTitle(null);

        // When & Then
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            bookDAO.save(testBook);
        });
        assertEquals("Book title is required", exception.getMessage());
        
        verify(entityManager, never()).persist(any(Book.class));
    }

    @Test
    public void testSave_BookWithEmptyTitle_ShouldThrowValidationException() {
        // Given
        testBook.setTitle("");

        // When & Then
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            bookDAO.save(testBook);
        });
        assertEquals("Book title is required", exception.getMessage());
        
        verify(entityManager, never()).persist(any(Book.class));
    }

    @Test
    public void testSave_BookWithNullAuthor_ShouldThrowValidationException() {
        // Given
        testBook.setAuthor(null);

        // When & Then
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            bookDAO.save(testBook);
        });
        assertEquals("Book author is required", exception.getMessage());
        
        verify(entityManager, never()).persist(any(Book.class));
    }

    @Test
    public void testSave_BookWithNullIsbn_ShouldThrowValidationException() {
        // Given
        testBook.setIsbn(null);

        // When & Then
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            bookDAO.save(testBook);
        });
        assertEquals("Book ISBN is required", exception.getMessage());
        
        verify(entityManager, never()).persist(any(Book.class));
    }

    @Test
    public void testSave_BookWithNegativeYear_ShouldThrowValidationException() {
        // Given
        testBook.setPublicationYear(-100);

        // When & Then
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            bookDAO.save(testBook);
        });
        assertEquals("Publication year cannot be negative", exception.getMessage());
        
        verify(entityManager, never()).persist(any(Book.class));
    }

    @Test
    public void testSave_BookWithInvalidYear_ShouldThrowValidationException() {
        // Given
        testBook.setPublicationYear(2101);

        // When & Then
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            bookDAO.save(testBook);
        });
        assertEquals("Publication year seems invalid", exception.getMessage());
        
        verify(entityManager, never()).persist(any(Book.class));
    }

    @Test
    public void testFindById_ExistingBook_ShouldReturnBook() {
        // Given
        when(entityManager.find(Book.class, 1L)).thenReturn(testBook);

        // When
        Optional<Book> foundBook = bookDAO.findById(1L);

        // Then
        assertTrue(foundBook.isPresent());
        assertEquals(testBook.getId(), foundBook.get().getId());
        assertEquals("Test Book", foundBook.get().getTitle());
        
        verify(entityManager).find(Book.class, 1L);
    }

    @Test
    public void testFindById_NonExistingBook_ShouldReturnEmpty() {
        // Given
        when(entityManager.find(Book.class, 999L)).thenReturn(null);

        // When
        Optional<Book> foundBook = bookDAO.findById(999L);

        // Then
        assertFalse(foundBook.isPresent());
        
        verify(entityManager).find(Book.class, 999L);
    }

    @Test
    public void testFindById_NullId_ShouldThrowValidationException() {
        // When & Then
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            bookDAO.findById(null);
        });
        assertEquals("Book ID cannot be null", exception.getMessage());
        
        verify(entityManager, never()).find(any(), any());
    }

    @Test
    public void testFindAll_EmptyDatabase_ShouldReturnEmptyList() {
        // Given
        when(entityManager.createQuery(anyString(), eq(Book.class))).thenReturn(bookQuery);
        when(bookQuery.getResultList()).thenReturn(Arrays.asList());

        // When
        List<Book> books = bookDAO.findAll();

        // Then
        assertNotNull(books);
        assertTrue(books.isEmpty());
        
        verify(entityManager).createQuery("SELECT b FROM Book b ORDER BY b.title", Book.class);
        verify(bookQuery).getResultList();
    }

    @Test
    public void testFindAll_WithBooks_ShouldReturnAllBooks() {
        // Given
        List<Book> bookList = Arrays.asList(testBook);
        when(entityManager.createQuery(anyString(), eq(Book.class))).thenReturn(bookQuery);
        when(bookQuery.getResultList()).thenReturn(bookList);

        // When
        List<Book> books = bookDAO.findAll();

        // Then
        assertNotNull(books);
        assertEquals(1, books.size());
        assertEquals("Test Book", books.get(0).getTitle());
        
        verify(entityManager).createQuery("SELECT b FROM Book b ORDER BY b.title", Book.class);
        verify(bookQuery).getResultList();
    }

    @Test
    public void testUpdate_ExistingBook_ShouldUpdateSuccessfully() {
        // Given
        when(entityManager.merge(any(Book.class))).thenReturn(testBook);

        // When
        Book updatedBook = bookDAO.update(testBook);

        // Then
        assertNotNull(updatedBook);
        assertEquals("Test Book", updatedBook.getTitle());
        assertEquals("Test Author", updatedBook.getAuthor());
        
        verify(entityManager).merge(testBook);
    }

    @Test
    public void testUpdate_BookWithInvalidData_ShouldThrowValidationException() {
        // Given
        testBook.setTitle("");

        // When & Then
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            bookDAO.update(testBook);
        });
        assertEquals("Book title is required", exception.getMessage());
        
        verify(entityManager, never()).merge(any(Book.class));
    }

    @Test
    public void testDeleteById_ExistingBook_ShouldDeleteSuccessfully() {
        // Given
        when(entityManager.find(Book.class, 1L)).thenReturn(testBook);
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(countQuery);
        when(countQuery.setParameter(anyString(), any())).thenReturn(countQuery);
        when(countQuery.executeUpdate()).thenReturn(1);

        // When
        boolean deleted = bookDAO.deleteById(1L);

        // Then
        assertTrue(deleted);
        
        verify(entityManager).find(Book.class, 1L);
        verify(entityManager).createQuery("DELETE FROM Book b WHERE b.id = :id", Long.class);
        verify(countQuery).setParameter("id", 1L);
        verify(countQuery).executeUpdate();
    }

    @Test
    public void testDeleteById_NonExistingBook_ShouldReturnFalse() {
        // Given
        when(entityManager.find(Book.class, 999L)).thenReturn(null);

        // When
        boolean deleted = bookDAO.deleteById(999L);

        // Then
        assertFalse(deleted);
        
        verify(entityManager).find(Book.class, 999L);
        verify(entityManager, never()).createQuery(anyString(), eq(Long.class));
    }

    @Test
    public void testExistsByIsbn_ExistingIsbn_ShouldReturnTrue() {
        // Given
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(countQuery);
        when(countQuery.setParameter(anyString(), any())).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(1L);

        // When
        boolean exists = bookDAO.existsByIsbn("1234567890123");

        // Then
        assertTrue(exists);
        
        verify(entityManager).createQuery("SELECT COUNT(b) FROM Book b WHERE b.isbn = :isbn", Long.class);
        verify(countQuery).setParameter("isbn", "1234567890123");
        verify(countQuery).getSingleResult();
    }

    @Test
    public void testExistsByIsbn_NonExistingIsbn_ShouldReturnFalse() {
        // Given
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(countQuery);
        when(countQuery.setParameter(anyString(), any())).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(0L);

        // When
        boolean exists = bookDAO.existsByIsbn("9999999999999");

        // Then
        assertFalse(exists);
        
        verify(entityManager).createQuery("SELECT COUNT(b) FROM Book b WHERE b.isbn = :isbn", Long.class);
        verify(countQuery).setParameter("isbn", "9999999999999");
        verify(countQuery).getSingleResult();
    }
} 