package com.curso.libary_api.service.interafaces;

import com.curso.libary_api.domain.Book;
import com.curso.libary_api.handle.BusinessException;


public interface BookService {

    public Book save(Book model) throws BusinessException;
}
