import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BookService } from '../../services/book.service';
import { BookCardComponent } from '../book-card/book-card.component';
import { Book } from '../../models/book.model';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-book-list',
  standalone: true,
  imports: [CommonModule, BookCardComponent, FormsModule],
  templateUrl: './book-list.component.html',
  styleUrls: ['./book-list.component.css']
})
export class BookListComponent implements OnInit {
  books: Book[] = [];
  loading = false;
  error = '';
  showCreateForm = false;
  newBook: Book = { title: '', author: '', isbn: '', publicationYear: undefined };
  creating = false;
  editingBook: Book | null = null;
  editBookData: Book = { title: '', author: '', isbn: '', publicationYear: undefined };
  updating = false;

  constructor(private bookService: BookService) {}

  ngOnInit(): void {
    this.loadBooks();
  }

  loadBooks(): void {
    this.loading = true;
    this.error = '';
    
    this.bookService.getBooks().subscribe({
      next: (books) => {
        this.books = books;
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Error al cargar los libros: ' + error.message;
        this.loading = false;
      }
    });
  }

  onEditBook(book: Book): void {
    this.editingBook = book;
    this.editBookData = { ...book };
    this.error = '';
  }

  onCancelEdit(): void {
    this.editingBook = null;
    this.editBookData = { title: '', author: '', isbn: '', publicationYear: undefined };
  }

  onSubmitEdit(): void {
    if (!this.editBookData.title || !this.editBookData.author || !this.editBookData.isbn) {
      this.error = 'Todos los campos son obligatorios excepto el año.';
      return;
    }
    if (!this.editingBook?.id) return;
    this.updating = true;
    this.bookService.updateBook(this.editingBook.id, this.editBookData).subscribe({
      next: (updated) => {
        this.books = this.books.map(book => book.id === updated.id ? updated : book);
        this.editingBook = null;
        this.updating = false;
        this.editBookData = { title: '', author: '', isbn: '', publicationYear: undefined };
      },
      error: (error) => {
        this.error = 'Error al actualizar el libro: ' + error.message;
        this.updating = false;
      }
    });
  }

  onDeleteBook(bookId: number | undefined): void {
    if (!bookId) return;
    
    if (confirm('¿Estás seguro de que quieres eliminar este libro?')) {
      this.bookService.deleteBook(bookId).subscribe({
        next: () => {
          this.books = this.books.filter(book => book.id !== bookId);
        },
        error: (error) => {
          this.error = 'Error al eliminar el libro: ' + error.message;
        }
      });
    }
  }

  onAddNewBook(): void {
    this.showCreateForm = true;
    this.newBook = { title: '', author: '', isbn: '', publicationYear: undefined };
    this.error = '';
  }

  onCancelCreate(): void {
    this.showCreateForm = false;
    this.newBook = { title: '', author: '', isbn: '', publicationYear: undefined };
  }

  onSubmitCreate(): void {
    if (!this.newBook.title || !this.newBook.author || !this.newBook.isbn) {
      this.error = 'Todos los campos son obligatorios excepto el año.';
      return;
    }
    this.creating = true;
    this.bookService.createBook(this.newBook).subscribe({
      next: (created) => {
        this.books = [...this.books, created];
        this.showCreateForm = false;
        this.creating = false;
        this.newBook = { title: '', author: '', isbn: '', publicationYear: undefined };
      },
      error: (error) => {
        this.error = 'Error al crear el libro: ' + error.message;
        this.creating = false;
      }
    });
  }
} 