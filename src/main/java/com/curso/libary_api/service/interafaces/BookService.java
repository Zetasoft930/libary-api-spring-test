package com.curso.libary_api.service.interafaces;

import com.curso.libary_api.domain.Book;
import com.curso.libary_api.handle.BusinessException;
import com.curso.libary_api.handle.IdNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface BookService {

    public Book save(Book model) throws BusinessException;

    Optional<Book> findById(Long id)throws IdNotFoundException;

    void delete(Book model) throws IdNotFoundException;

    Book update(Book model) throws IdNotFoundException;

    Page<Book> find(Book filtro, Pageable pageable);
}
