package com.curso.libary_api.respository;

import com.curso.libary_api.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {

   // @Query(value = "SELECT u FROM Book u WHERE isbu.isbn =?1")
    boolean existsByIsbn(String isbn);
    @Query(value = "SELECT u FROM Book u WHERE u.title =?1 AND u.author=?2")
    Page<Book> find(String title, String author, Pageable pageable);
}
