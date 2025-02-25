package com.curso.libary_api.service.impl;

import com.curso.libary_api.domain.Book;
import com.curso.libary_api.handle.BusinessException;
import com.curso.libary_api.handle.IdNotFoundException;
import com.curso.libary_api.respository.BookRepository;
import com.curso.libary_api.service.interafaces.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

        return repository.save(model);

    }

    @Override
    public Optional<Book> findById(Long id) throws IdNotFoundException {
        return repository.findById(id);
    }

    @Override
    public void delete(Book model) throws IdNotFoundException {

        try{

            if(  model == null)
                throw new IdNotFoundException("Objecto não pode ser nulo");
            else if(  model.getId() == null)
                throw new IdNotFoundException("Id Não pode ser Nulo");

            repository.delete(model);
        }catch (Exception ex){
            throw ex;
        }

    }

    @Override
    public Book update(Book model) throws IdNotFoundException {

            if(  model == null)
                throw new IdNotFoundException("Objecto não pode ser nulo");
            else if(  model.getId() == null)
                throw new IdNotFoundException("Id Não pode ser Nulo");

            return   repository.save(model);


    }

    @Override
    public Page<Book> find(Book filtro, Pageable pageable) {
        return repository.find(filtro.getTitle(),filtro.getAuthor(),pageable);
    }
}
