package com.curso.libary_api.repository;

import com.curso.libary_api.domain.Book;
import com.curso.libary_api.handle.IdNotFoundException;
import com.curso.libary_api.respository.BookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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


    @Test
    @DisplayName("Deve retornar sucesso quando  o livro for eliminado")
    public  void delete(){

        Book model = Book
                .builder()
                .title("As Aventuras")
                .isbn("002")
                .author("Arthur")
                .build();
        entityManager.persist(model);


        Book resultFirstResult = repository.findById(1L)
                .orElseThrow( () -> new RuntimeException("Id invalido"));


        repository.delete(resultFirstResult);

        Executable executable  = ()-> repository.findById(1L)
                .orElseThrow( () -> new IdNotFoundException("Id invalido"));

        Assertions.assertThrows(IdNotFoundException.class,executable);


    }


    @Test
    @DisplayName("pesquisar livro por titulo e autor")
    public void find() throws Exception{

        Book model = Book
                .builder()
                .title("As Aventuras")
                .isbn("002")
                .author("Arthur")
                .build();
        entityManager.persist(model);

        PageRequest pageRequest = PageRequest.of(0,100);

        Page<Book> bookPage = repository.find(model.getTitle(), model.getAuthor(),pageRequest);


        Assertions.assertEquals(1L,bookPage.getTotalElements());
        Assertions.assertEquals(100,bookPage.getSize());
        Assertions.assertEquals(0,bookPage.getNumber());

    }


    @Test
    @DisplayName("Deve retornar sucesso quando  o livro for eliminado")
    public  void save(){




        boolean isExisteIsbn = repository.existsByIsbn("002");

        Book model = Book
                .builder()
                .title("As Aventuras")
                .isbn("002")
                .author("Arthur")
                .build();
        Book save =    entityManager.persist(model);



        Assertions.assertNotEquals(null,save.getId());
        Assertions.assertEquals(false,isExisteIsbn);




    }


}
