package com.curso.libary_api.resource;

import com.curso.libary_api.domain.Book;
import com.curso.libary_api.domain.dto.BookDTO;
import com.curso.libary_api.service.interafaces.BookService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService service;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create(@RequestBody @Valid BookDTO  request){

        Book modelForSave= modelMapper.map(request, Book.class);

        Book resultSave = null;
        try {
            resultSave = service.save(modelForSave);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return modelMapper.map(resultSave, BookDTO.class);
    }
}
