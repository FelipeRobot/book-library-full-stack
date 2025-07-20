import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BookService } from '../../services/book.service';
import { BookCardComponent } from '../book-card/book-card.component';
import { Book } from '../../models/book.model';

@Component({
  selector: 'app-book-list',
  standalone: true,
  imports: [CommonModule, BookCardComponent],
  templateUrl: './book-list.component.html',
  styleUrls: ['./book-list.component.css']
})
export class BookListComponent implements OnInit {
  books: Book[] = [];
  loading = false;
  error = '';

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
    // TODO: Implementar modal de edición
    console.log('Editar libro:', book);
    alert(`Editar libro: ${book.title}`);
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
    // TODO: Implementar modal de creación
    console.log('Agregar nuevo libro');
    alert('Funcionalidad de agregar libro próximamente');
  }
} 