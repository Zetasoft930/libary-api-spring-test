package com.curso.libary_api.resource;


import com.curso.libary_api.config.AppConfig;
import com.curso.libary_api.domain.Book;
import com.curso.libary_api.domain.dto.BookDTO;
import com.curso.libary_api.handle.BusinessException;
import com.curso.libary_api.handle.IdNotFoundException;
import com.curso.libary_api.respository.BookRepository;
import com.curso.libary_api.service.interafaces.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(BookController.class) // Carrega apenas o controller e as configurações de MVC.
@AutoConfigureMockMvc
//@SpringBootTest
@Import(AppConfig.class) // Importando configuração para ModelMapper
public class BookControllerTest {

    private static String BOOK_API= "/api/books";

    @Autowired
    private MockMvc mvc;

    //@MockitoBean
    @MockitoBean
    private BookService service;

    @Mock
    private BookRepository repository;


   /* @BeforeEach
    public void setUp(){
        service = new BookServiceImpl();
    }*/


    @Test
    @DisplayName("Deve criar um livro com sucesso")
    public void createBook() throws Exception{



        //CENARIO

        BookDTO dto = BookDTO
                .builder()
                .title("As Aventuras")
                .isbn("001")
                .author("Arthur")
                .build();



        Book dtoSave = Book
                .builder()
                .id(1L)
                .title("As Aventuras")
                .isbn("001")
                .author("Arthur")
                .build();


        BDDMockito.given(service.save(BDDMockito.any(Book.class))).willReturn(dtoSave);

        String json = new ObjectMapper().writeValueAsString(dto);

        //EXECUÇÃO
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);


        //VERIFICAÇÃO
        mvc
           .perform(request)
           .andExpect(MockMvcResultMatchers.status().isCreated())
           .andExpect(MockMvcResultMatchers.jsonPath("id").isNotEmpty())
           .andExpect(MockMvcResultMatchers.jsonPath("title").value("As Aventuras"))
           .andExpect(MockMvcResultMatchers.jsonPath("author").value("Arthur"))
           .andExpect(MockMvcResultMatchers.jsonPath("isbn").value("001"));

    }


    @Test
    @DisplayName("Deve obter iformação de um livro")
    public void getBookDetailsById() throws Exception{



        //CENARIO

        Long id = 1L;

        Book dtoSave = Book
                .builder()
                .id(1L)
                .title("As Aventuras")
                .isbn("001")
                .author("Arthur")
                .build();

        BDDMockito.given(service.findById(Mockito.anyLong()))
                .willReturn(Optional.of(dtoSave));


        //EXECUÇÃO
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(BOOK_API+"/"+id)
                .accept(MediaType.APPLICATION_JSON);


        //VERIFICAÇÃO
        mvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("title").value("As Aventuras"))
                .andExpect(MockMvcResultMatchers.jsonPath("author").value("Arthur"))
                .andExpect(MockMvcResultMatchers.jsonPath("isbn").value("001"));

    }




    @Test
    @DisplayName("Deve retornar not found quando id não existir")
    public void getBookDetailsIdNotFound() throws Exception{



        //CENARIO

        Long id = 1L;

        Book dtoSave = Book
                .builder()
                .id(1L)
                .title("As Aventuras")
                .isbn("001")
                .author("Arthur")
                .build();

        String messageError = "Id Not found";

        BDDMockito.given(service.findById(Mockito.anyLong()))
                .willThrow(  new IdNotFoundException(messageError));


        //EXECUÇÃO
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(BOOK_API+"/"+id)
                .accept(MediaType.APPLICATION_JSON);


        //VERIFICAÇÃO
        mvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("errors[0]").value(messageError));

    }



    @Test
    @DisplayName("Deve dar erro quando o ISBN for repetido")
    public void createBookWithDuplicatedIsbn() throws Exception{


        //CENARIO

        BookDTO dto = BookDTO
                .builder()
                .title("As Aventuras")
                .isbn("001")
                .author("Arthur")
                .build();


        String json = new ObjectMapper().writeValueAsString(dto);
        String messageError = "Isbn Já cadastrado";

        BDDMockito.given(service.save(BDDMockito.any(Book.class)))
                .willThrow( new BusinessException(messageError));


        //EXECUÇÃO
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);


        //VERIFICAÇÃO
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("errors[0]").value(messageError));


    }


    @Test
    @DisplayName("Deve lançar um erro de validação quando não houver dados suficiente para criar o livro")
    public void createInvalidBook() throws Exception{



        String json = new ObjectMapper().writeValueAsString(new Book());

        //EXECUÇÃO
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);


        //VERIFICAÇÃO
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath("errors", Matchers.hasSize(3)));


    }

    @Test
    @DisplayName("Deve eliminar  um livro")
    public void deleteBook() throws Exception{



        //CENARIO

        Long id = 90L;

        Book dtoSave = Book
                .builder()
                .id(id)
                .title("As Aventuras")
                .isbn("001")
                .author("Arthur")
                .build();

        BDDMockito.given(service.findById(Mockito.anyLong()))
                .willReturn(Optional.of(dtoSave));


        //EXECUÇÃO
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(BOOK_API+"/"+id)
                .accept(MediaType.APPLICATION_JSON);


        //VERIFICAÇÃO
        mvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    @DisplayName("Deve Retornar erro quando o livro não for encontrado para ser eliminado")
    public void deleteBookNotFound() throws Exception{



        //CENARIO

        Long id = 99L;

        String messageError = "Id Not found";

        BDDMockito.given(service.findById(Mockito.anyLong()))
                .willThrow( new IdNotFoundException(messageError));


        //EXECUÇÃO
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(BOOK_API+"/"+id)
                .accept(MediaType.APPLICATION_JSON);


        //VERIFICAÇÃO
        mvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    @DisplayName("Deve actualizar  um livro")
    public void updateBook() throws Exception{



        //CENARIO

        Long id = 90L;


        BookDTO dto = BookDTO
                .builder()
                .title("As Aventuras de Harry Potter")
                .isbn("001")
                .author("Arthur")
                .build();


        Book bookUpdate = Book
                .builder()
                .id(id)
                .title(dto.getTitle())
                .isbn(dto.getIsbn())
                .author(dto.getAuthor())
                .build();


        Book bookExist = Book
                .builder()
                .id(id)
                .title("As Aventuras")
                .isbn("001")
                .author("Arthur")
                .build();

        BDDMockito.given(service.findById(Mockito.anyLong()))
                .willReturn(Optional.of(bookExist));

        BDDMockito.given(service.update(Mockito.any()))
                .willReturn(bookUpdate);


        String json = new ObjectMapper().writeValueAsString(dto);

        //EXECUÇÃO

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(BOOK_API+"/"+id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        //VERIFICAÇÃO
        mvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("title").value("As Aventuras de Harry Potter"))
                .andExpect(MockMvcResultMatchers.jsonPath("author").value("Arthur"))
                .andExpect(MockMvcResultMatchers.jsonPath("isbn").value("001"));

    }

    @Test
    @DisplayName("pesquisar livro por titulo e autor")
    public void findAll() throws Exception {



        Book bookExist = Book
                .builder()
                .id(1L)
                .title("As Aventuras")
                .isbn("001")
                .author("Arthur")
                .build();


        PageRequest pageRequest = PageRequest.of(0,100);
        List<Book> list = Arrays.asList(bookExist);

        BDDMockito.given(service.find(Mockito.any(Book.class),Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<Book>(list,pageRequest,1));

        String parametros = String.format( "title=%s&author=%s&page=0&size=100",
                bookExist.getTitle(),bookExist.getAuthor());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(BOOK_API+"?"+parametros)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);



        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("content",Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("totalElements").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("pageable.pageSize").value(100))
                .andExpect(MockMvcResultMatchers.jsonPath("pageable.pageNumber").value(0));
    }

}
