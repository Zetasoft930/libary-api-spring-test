package com.curso.libary_api.service.impl;

import com.curso.libary_api.domain.Book;
import com.curso.libary_api.handle.BusinessException;
import com.curso.libary_api.respository.BookRepository;
import com.curso.libary_api.service.interafaces.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository repository;

    @Override
    public Book save(Book model) throws BusinessException{

        boolean flag  = repository.existsByIsbn(model.getIsbn());
        if (flag) {
            throw new BusinessException("Isbn Já cadastrado");
        }

        System.out.println("ojecto salvo");

        return repository.save(model);

    }
}
