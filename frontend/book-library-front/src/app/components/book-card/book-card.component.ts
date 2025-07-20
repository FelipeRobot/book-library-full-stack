import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Book } from '../../models/book.model';

@Component({
  selector: 'app-book-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './book-card.component.html',
  styleUrls: ['./book-card.component.css']
})
export class BookCardComponent {
  @Input() book!: Book;
  @Output() editBook = new EventEmitter<Book>();
  @Output() deleteBook = new EventEmitter<number>();

  onEdit(): void {
    this.editBook.emit(this.book);
  }

  onDelete(): void {
    this.deleteBook.emit(this.book.id);
  }
} 