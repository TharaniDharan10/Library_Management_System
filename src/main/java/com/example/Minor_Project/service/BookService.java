package com.example.Minor_Project.service;

import com.example.Minor_Project.dto.AddBookRequest;
import com.example.Minor_Project.enums.BookType;
import com.example.Minor_Project.mapper.AuthorMapper;
import com.example.Minor_Project.mapper.BookMapper;
import com.example.Minor_Project.model.Author;
import com.example.Minor_Project.model.Book;
import com.example.Minor_Project.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BookService {

    @Autowired
    AuthorService authorService;

    @Autowired
    BookRepository bookRepository;


    public Book addBook(AddBookRequest bookRequest) {
        Author authorFromDB = authorService.getAuthorByEmail(bookRequest.getAuthorEmail());
        if(authorFromDB == null){
            authorFromDB = AuthorMapper.mapToAuthor(bookRequest);
            authorFromDB = authorService.addAuthor(authorFromDB);

        }
        Book book = BookMapper.mapToBook(bookRequest);
        book.setAuthor(authorFromDB);

        return bookRepository.save(book);


    }

    public void updateBookMetaData(Book book){
        bookRepository.save(book);
    }

    public Book getBookByBookNo(String bookNo) {
        return bookRepository.findBookByBookNo(bookNo);
    }

    public List<Book> getBooks(String bookTitle, BookType bookType) {

        log.info("I am in bookService");
        List<Book> res = bookRepository.findBookByFilters(bookTitle,bookType);
        log.info("I am returning from bookService");
        return res;

    }

}

