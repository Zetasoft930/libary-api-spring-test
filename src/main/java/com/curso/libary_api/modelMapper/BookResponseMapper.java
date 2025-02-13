package com.curso.libary_api.modelMapper;


import com.curso.libary_api.domain.Book;
import com.curso.libary_api.domain.dto.BookDTO;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.Set;
import java.util.stream.Collectors;


public class BookResponseMapper implements Converter<Book, BookDTO> {

	@Override
	public BookDTO convert(MappingContext<Book, BookDTO> mappingContext) {

		Book source = mappingContext.getSource();

		BookDTO destination = new BookDTO();
		destination.setId(source.getId());

		destination.setIsbn(source.getIsbn());
		destination.setTitle(source.getTitle());
		destination.setAuthor(source.getAuthor());

		return destination;

	}
}