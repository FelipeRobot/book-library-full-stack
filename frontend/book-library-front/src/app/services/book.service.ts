import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Book } from '../models/book.model';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class BookService {
  private apiUrl = 'http://localhost:8081/servlet/api/books';

  constructor(private http: HttpClient) { }

  getBooks(): Observable<Book[]> {
    return this.http.get<{ data: Book[] }>(this.apiUrl).pipe(
      map(response => response.data)
    );
  }

  getBook(id: number): Observable<Book> {
    return this.http.get<Book>(`${this.apiUrl}/${id}`);
  }

  createBook(book: Book): Observable<Book> {
    return this.http.post<{ data: Book }>(this.apiUrl, book).pipe(
      map(response => response.data)
    );
  }

  updateBook(id: number, book: Book): Observable<Book> {
    return this.http.put<{ data: Book }>(`${this.apiUrl}/${id}`, book).pipe(
      map(response => response.data)
    );
  }

  deleteBook(id: number): Observable<any> {
    return this.http.delete<{ message: string; status: string; id: number }>(`${this.apiUrl}/${id}`);
  }
} 