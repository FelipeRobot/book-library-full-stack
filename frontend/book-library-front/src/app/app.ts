import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { BookListComponent } from './components/book-list/book-list.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, HttpClientModule, BookListComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('Biblioteca de Libros');
}
