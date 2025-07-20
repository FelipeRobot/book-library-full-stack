package com.booklibrary.config;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import com.booklibrary.rest.BookResource;
import com.booklibrary.exception.ValidationExceptionMapper;
import com.booklibrary.exception.NotFoundExceptionMapper;
import com.booklibrary.exception.GenericExceptionMapper;
import com.booklibrary.exception.RuntimeExceptionMapper;
import com.booklibrary.exception.EJBExceptionMapper;
import com.booklibrary.config.CORSFilter;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        
        // Registrar los recursos REST
        resources.add(BookResource.class);
        
        // Registrar los ExceptionMapper
        resources.add(ValidationExceptionMapper.class);
        resources.add(NotFoundExceptionMapper.class);
        resources.add(GenericExceptionMapper.class);
        resources.add(RuntimeExceptionMapper.class);
        resources.add(EJBExceptionMapper.class);
        
        // Registrar el filtro CORS
        resources.add(CORSFilter.class);
        
        return resources;
    }
} 