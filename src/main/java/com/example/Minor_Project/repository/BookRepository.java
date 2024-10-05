package com.example.Minor_Project.repository;

import com.example.Minor_Project.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book,Integer> ,CustomBookRepository{

    Book findBookByBookNo(String bookNo);

}
