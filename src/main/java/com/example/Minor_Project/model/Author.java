package com.example.Minor_Project.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(length = 50,nullable = false)
    String name;

    @Column(unique = true, nullable = false, length = 50)
    String email;

//    @OneToMany //this creates one Foreign key btn Author and Book
//    List<Book> books; //created this to make bidirectional relationship.Doing this creates one more table Author_books

    @OneToMany(mappedBy =  "author") //we do this when we dont want to create one more Foreign key btn Author and Book and want to use the one created previously by @ManytoOne
//    @JsonIgnore //if we dont add this annotation and go to postman and do localhost:8080/author and in params ,add email and its value yukta@gmail.com.It shows infinite loop in body of output i.e author->list of books ->author and so on which is controlled by hibernate.This is bcoz we have made author and book bidirectional,that is
    //we store list of books in author,we also store author in book.To avoid List of books printed,use this annotation
    @JsonIgnoreProperties(value = "author")//using this instead of @JsonIgnore ignores just author information,thus preventing infinite loop
    List<Book> books;

    @CreationTimestamp(source = SourceType.DB)
    Date createdOn;

    @UpdateTimestamp
    Date updatedOn;}
