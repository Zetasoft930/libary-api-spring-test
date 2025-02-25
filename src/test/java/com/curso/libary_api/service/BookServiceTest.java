package com.curso.libary_api.service;

import com.curso.libary_api.domain.Book;
import com.curso.libary_api.handle.BusinessException;
import com.curso.libary_api.handle.IdNotFoundException;
import com.curso.libary_api.respository.BookRepository;
import com.curso.libary_api.service.impl.BookServiceImpl;
import com.curso.libary_api.service.interafaces.BookService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {


    @Mock
    private BookRepository repository;

    @InjectMocks  // ✅ Injeta o mock do repository na BookService real
    private BookServiceImpl service;



    @Test
    @DisplayName("Deve dar erro quando o ISBN for repetido")
    public void createBookWithDuplicatedIsbn()  {


        //CENARIO
        Book model = Book
                .builder()
                .title("As Aventuras")
                .isbn("002")
                .author("Arthur")
                .build();

        String messageError = "Isbn Já cadastrado";

        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);

        // EXECUÇÃO
        Executable executable = ()-> service.save(model);

        //VERIFICACAO

        Assertions.assertThrows(BusinessException.class, executable,messageError );



    }

    @Test
    @DisplayName("Deve salvar o livro com sucesso")
    public void saveBookTest() throws Exception {


        //CENARIO
        Book model = Book
                .builder()
                .title("As Aventuras")
                .isbn("002")
                .author("Arthur")
                .build();

        Mockito.when(repository.save(model))
                .thenReturn( Book
                        .builder()
                        .title("As Aventuras")
                        .isbn("002")
                        .author("Arthur")
                        .build());

        Mockito.when(service.save(model))
                .thenReturn( Book
                        .builder()
                        .id(1L)
                        .title("As Aventuras")
                        .isbn("002")
                        .author("Arthur")
                        .build());


        // EXECUÇÃO

        Book saved = service.save(model);

        //VERIFICACAO


        Assertions.assertNotEquals(null,saved);
        Assertions.assertNotEquals(null,saved.getId());
        Assertions.assertEquals("As Aventuras",saved.getTitle());
        Assertions.assertEquals("002",saved.getIsbn());
        Assertions.assertEquals("Arthur",saved.getAuthor());


    }


    @Test
    @DisplayName("Deve mostrar um livro de id existir na base de dados")
    public void getBookDetailsById() throws Exception{



        //CENARIO

        Long id = 1L;

        Book model = Book
                .builder()
                .id(5L)
                .title("As Aventuras")
                .isbn("005")
                .author("Arthur")
                .build();


        Mockito.when(repository.findById(Mockito.anyLong()))
                    .thenReturn(Optional.of(model));


        //EXECUÇÃO

        Book  resultModel =  service.findById(id).orElse(null);


        //VERIFICAÇÃO

        Assertions.assertNotEquals(null,resultModel);
        Assertions.assertEquals(5L,resultModel.getId());

    }


    @Test
    @DisplayName("Deve mostrar Mensagem de erro quando id não existir")
    public void getBookDetailsIdNotFound() throws Exception{



        //CENARIO

        Long id = 1L;

        Book model = Book
                .builder()
                .id(5L)
                .title("As Aventuras")
                .isbn("005")
                .author("Arthur")
                .build();

        String messageError = "Id Not found";

        Mockito.when(repository.findById(Mockito.anyLong()))
                .thenThrow(new IdNotFoundException(messageError));


        //EXECUÇÃO

        Executable  executable =  ()-> service.findById(id);


        //VERIFICAÇÃO

        Assertions.assertThrows(IdNotFoundException.class,executable,messageError);

    }
    @Test
    @DisplayName("Deve eliminar  um livro")
    public void deleteBook() throws Exception{



        //CENARIO


        Book model = Book
                .builder()
                .id(1L)
                .title("As Aventuras")
                .isbn("005")
                .author("Arthur")
                .build();

        String messageError = "Id Not found";

        //EXECUÇÃO

        service.delete(model);


        //VERIFICAÇÃO

        Mockito.verify(repository,Mockito.times(1)).delete(model);


    }

    @Test
    @DisplayName("Deve retornar erro quando id for nulo")
    public void deleteBookNotFoundId() throws Exception{



        //CENARIO


        Book model = Book
                .builder()
                .title("As Aventuras")
                .isbn("005")
                .author("Arthur")
                .build();

        String messageError = "Id Não pode ser Nulo";


        //EXECUÇÃO

        Executable  executable =  ()-> service.delete(model);


        //VERIFICAÇÃO
        Mockito.verify(repository,Mockito.never()).delete(model);
        Assertions.assertThrows(IdNotFoundException.class,executable,messageError);


    }


    @Test
    @DisplayName("Deve actualiza um livro")
    public void updateBook() throws Exception{



        //CENARIO


        Book model = Book
                .builder()
                .id(1L)
                .title("As Aventuras")
                .isbn("005")
                .author("Arthur")
                .build();

        Book modelSaveReturn = Book
                .builder()
                .id(1L)
                .title("POO - Programação orientada a Objecto")
                .isbn("005")
                .author("Arthur")
                .build();

        Mockito.when(service.update(model))
                .thenReturn(modelSaveReturn);

        //EXECUÇÃO

        Book modeResult =  service.update(model);


        //VERIFICAÇÃO

        Assertions.assertEquals(1L,modeResult.getId());
        Assertions.assertEquals("POO - Programação orientada a Objecto",modeResult.getTitle());
        Assertions.assertEquals("005",modeResult.getIsbn());
        Assertions.assertEquals("Arthur",modeResult.getAuthor());


    }


    @Test
    @DisplayName("Deve retornar um erro quando Id for invalido")
    public void updateBookInvalidId() throws Exception{



        //CENARIO


        Book model = Book
                .builder()
                .title("As Aventuras")
                .id(null)
                .isbn("005")
                .author("Arthur")
                .build();


        String messageError = "Id Não pode ser Nulo";

        //EXECUÇÃO
        Executable  executable =  () -> service.update(model);

        //VERIFICAÇÃO
        Mockito.verify(repository,Mockito.never()).save(model);
        //Assertions.assertEquals(IdNotFoundException.class,executable,messageError);


    }


    @Test
    @DisplayName("pesquisar livro por titulo e autor")
    public void find() throws Exception{


        //CENARIO


        Book model = Book
                .builder()
                .title("As Aventuras")
                .id(1L)
                .isbn("005")
                .author("Arthur")
                .build();


        List<Book> list = Arrays.asList(model);
        PageRequest pageRequest = PageRequest.of(0,100);
        Page<Book> pageable = new PageImpl<Book>(list,pageRequest,1);


        Mockito.when(repository.find(Mockito.anyString(),Mockito.anyString(),Mockito.any(Pageable.class)))
                .thenReturn(pageable);

        Page<Book> bookPage =  service.find(model,pageRequest);


        Assertions.assertEquals(1L,bookPage.getTotalElements());
        Assertions.assertEquals(100,bookPage.getSize());
        Assertions.assertEquals(0,bookPage.getNumber());

    }

}
