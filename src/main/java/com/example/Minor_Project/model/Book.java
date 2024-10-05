package com.example.Minor_Project.model;


import com.example.Minor_Project.enums.BookType;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@ToString
@Data
@Builder
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(length = 30)
    String bookTitle;

    @Column(length = 10,nullable = false,unique = true)
    String bookNo;



    int securityAmount; //all books do not have equal importance,so the collected amount is different.In case if collected amount is same,then we can add this field in application,properties

    @Enumerated(value = EnumType.STRING)
    BookType bookType;

    @ManyToOne //many book to one author.This creates One Foreign key btn Book and Author
    @JoinColumn//doing this joins author_id   i.e author_its-primary-key   .If we want some other name,we should add name parameter inside()
//    @JoinColumn(name = "authorID")//this creates a row by name authorID
    @JsonIgnore
    Author author;


    @OneToMany(mappedBy = "book")
    @JsonIgnore
    List<Transaction> transactions;

    @ManyToOne //many books can be affored by one user
    @JsonIgnore
    User user;

    @CreationTimestamp(source = SourceType.DB)      //if its a case where we create this and UpdateTimestamp in every model,then we can make them in separate class and we can extend that class in every required model
    Date createdOn;

    @UpdateTimestamp
    Date updatedOn;
}
