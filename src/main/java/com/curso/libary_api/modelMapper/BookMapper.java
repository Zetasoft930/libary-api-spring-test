package com.curso.libary_api.modelMapper;


import com.curso.libary_api.domain.Book;
import com.curso.libary_api.domain.dto.BookDTO;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.Set;
import java.util.stream.Collectors;


public class BookMapper implements Converter<BookDTO, Book> {

    @Override
    public Book convert(MappingContext<BookDTO, Book> mappingContext) {

        BookDTO source = mappingContext.getSource();

        Book destination = new Book();
       destination.setIsbn(source.getIsbn());
       destination.setTitle(source.getTitle());
destination.setAuthor(source.getAuthor());
        return destination;

    }
}