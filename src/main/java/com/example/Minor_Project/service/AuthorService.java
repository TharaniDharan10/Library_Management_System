package com.example.Minor_Project.service;

import com.example.Minor_Project.model.Author;
import com.example.Minor_Project.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {
    @Autowired
    AuthorRepository authorRepository;

    public Author getAuthorByEmail(String email){
        return authorRepository.fetchAuthorByEmailByNativeQuery(email);

    }

    public Author addAuthor(Author author){
        return authorRepository.save(author);
    }

}
