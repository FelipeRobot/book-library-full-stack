package com.booklibrary.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.booklibrary.dao.BookDAO;
import com.booklibrary.entity.Book;
import com.booklibrary.exception.NotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {

    private BookDAO bookDAO;
    
    private BookDAO getBookDAO() {
        if (bookDAO == null) {
            try {
                InitialContext context = new InitialContext();
                bookDAO = (BookDAO) context.lookup("java:app/com.booklibrary-ejbs-1.0/BookDAO");
            } catch (NamingException e) {
                throw new RuntimeException("Error looking up BookDAO", e);
            }
        }
        return bookDAO;
    }

    @GET
    @Path("/{id}")
    public Response getBook(@PathParam("id") Long id) {
        Optional<Book> bookOpt = getBookDAO().findById(id);
        if (bookOpt.isEmpty()) {
            throw new NotFoundException("Book with ID " + id + " not found");
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("data", bookOpt.get());
        response.put("status", "success");
        
        return Response.ok(response).build();
    }

    @GET
    public Response getAllBooks() {
        List<Book> books = getBookDAO().findAll();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Lista de libros");
        response.put("status", "success");
        response.put("data", books);
        
        return Response.ok(response).build();
    }

    @POST
    public Response createBook(Book book) {
        Book savedBook = getBookDAO().save(book);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Libro creado exitosamente");
        response.put("status", "success");
        response.put("data", savedBook);
        
        return Response.status(Response.Status.CREATED)
                     .entity(response)
                     .build();
    }

    @PUT
    @Path("/{id}")
    public Response updateBook(@PathParam("id") Long id, Book book) {
        Optional<Book> existingBookOpt = getBookDAO().findById(id);
        if (existingBookOpt.isEmpty()) {
            throw new NotFoundException("Book with ID " + id + " not found");
        }
        
        book.setId(id);
        Book updatedBook = getBookDAO().update(book);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Libro actualizado exitosamente");
        response.put("status", "success");
        response.put("data", updatedBook);
        
        return Response.ok(response).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") Long id) {
        Optional<Book> existingBookOpt = getBookDAO().findById(id);
        if (existingBookOpt.isEmpty()) {
            throw new NotFoundException("Book with ID " + id + " not found");
        }
        
        boolean deleted = getBookDAO().deleteById(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Libro eliminado exitosamente");
        response.put("status", "success");
        response.put("id", id);
        
        return Response.ok(response).build();
    }
} 