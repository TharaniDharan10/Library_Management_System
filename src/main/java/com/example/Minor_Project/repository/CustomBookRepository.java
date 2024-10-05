package com.example.Minor_Project.repository;

import com.example.Minor_Project.enums.BookType;
import com.example.Minor_Project.model.Book;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomBookRepository { //its implemented by CustomBookRepositoryImpl class

    List<Book> findBookByFilters(String bookTitle , BookType bookType);
}
