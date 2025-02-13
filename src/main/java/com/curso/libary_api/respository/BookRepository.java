package com.curso.libary_api.respository;

import com.curso.libary_api.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {

   // @Query(value = "SELECT u FROM Book u WHERE isbu.isbn =?1")
    boolean existsByIsbn(String isbn);
}
