package com.controllers;

import jakarta.ejb.Stateless;

@Stateless
public class Controller {
    
    public String hello(String nombre) {
        return "Hello, " + nombre;
    }
    
    public String getBookInfo(String bookId) {
        // TODO: Implementar lógica de negocio para obtener información del libro
        return "Book ID: " + bookId;
    }
}
