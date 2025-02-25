package com.curso.libary_api.resource;

import com.curso.libary_api.domain.Book;
import com.curso.libary_api.domain.dto.BookDTO;
import com.curso.libary_api.handle.IdNotFoundException;
import com.curso.libary_api.service.interafaces.BookService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/books")
@Slf4j
public class BookController {

    @Autowired
    private BookService service;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create(@RequestBody @Valid BookDTO  request){

        log.info("create a bok for isbn {}",request.getIsbn());
        Book modelForSave= modelMapper.map(request, Book.class);

        Book resultSave = null;
        try {
            resultSave = service.save(modelForSave);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return modelMapper.map(resultSave, BookDTO.class);
    }
    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public BookDTO findById(@PathVariable Long id){

        Book resultSave = null;

        try {
            resultSave =  resultSave = service.findById(id)
                    .orElseThrow( ()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return modelMapper.map(resultSave, BookDTO.class);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id){


        try {

            Book model = service.findById(id)
                    .orElseThrow( ()-> new ResponseStatusException(HttpStatus.NOT_FOUND));

            service.delete(model);

        } catch (IdNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @PutMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public BookDTO updateById(@PathVariable Long id,@RequestBody @Valid BookDTO request){

        Book resultSave = null;
        try {

            Book model = service.findById(id)
                    .orElseThrow( ()-> new ResponseStatusException(HttpStatus.NOT_FOUND));

            model.setTitle(request.getTitle());
            model.setAuthor(request.getAuthor());

            resultSave =   service.update(model);

        } catch (IdNotFoundException e) {
            throw new RuntimeException(e);
        }
        return modelMapper.map(resultSave, BookDTO.class);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Page<BookDTO> find(@RequestParam String title, @RequestParam String author, Pageable pageable){


        try{

            Page<Book> pageBook = service.find(Book
                    .builder()
                    .title(title)
                    .author(author)
                    .build(), pageable);


            Page<BookDTO> pageBookDto = pageBook.map(item ->{
                return modelMapper.map(item,BookDTO.class);
            });

            return pageBookDto;

        }catch (Exception ex){
            throw new RuntimeException(ex);
        }

    }
}
