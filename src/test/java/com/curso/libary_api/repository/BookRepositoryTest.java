package com.curso.libary_api.repository;

import com.curso.libary_api.domain.Book;
import com.curso.libary_api.respository.BookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryTest {


    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository repository;


    @Test
    @DisplayName("Deve retornar sucesso quando existir um livro com ISBN no banco")
    public  void isVerifyExistIsbn(){

        //CENARIO
        String isbn = "002";

        Book model = Book
                .builder()
                .title("As Aventuras")
                .isbn("002")
                .author("Arthur")
                .build();
        entityManager.persist(model);


        //EXECUÇÃO
        boolean isExist =  repository.existsByIsbn(isbn);

        //VERIFICAÇÃO

        Assertions.assertEquals(true,isExist);


    }

    @Test
    @DisplayName("Deve retornar sucesso quando  o livro não existir")
    public  void isVerifyNotExistIsbn(){

        //CENARIO
        String isbn = "002";


        //EXECUÇÃO
        boolean isExist =  repository.existsByIsbn(isbn);

        //VERIFICAÇÃO

        Assertions.assertEquals(false,isExist);


    }


}
