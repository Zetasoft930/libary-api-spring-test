package com.curso.libary_api.config;

import com.curso.libary_api.domain.Book;
import com.curso.libary_api.domain.dto.BookDTO;
import com.curso.libary_api.modelMapper.BookMapper;
import com.curso.libary_api.modelMapper.BookResponseMapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapperConfig() {

        ModelMapper model = new ModelMapper();

        domain(model);
        dto(model);

        return model;
    }

    private void domain(ModelMapper model) {

        model.addConverter(new BookMapper(),
                BookDTO.class,
                Book.class);
    }

    private void dto(ModelMapper model) {

        model.addConverter(new BookResponseMapper(),
                Book.class, BookDTO.class);
        }
}
