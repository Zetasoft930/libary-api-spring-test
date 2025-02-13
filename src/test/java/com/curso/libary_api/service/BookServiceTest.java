package com.curso.libary_api.service;

import com.curso.libary_api.domain.Book;
import com.curso.libary_api.handle.BusinessException;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;


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


}
