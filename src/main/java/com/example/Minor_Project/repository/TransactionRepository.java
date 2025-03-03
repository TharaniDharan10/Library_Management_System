package com.example.Minor_Project.repository;

import com.example.Minor_Project.model.Book;
import com.example.Minor_Project.model.Transaction;
import com.example.Minor_Project.model.User;
import com.sun.source.tree.LambdaExpressionTree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Integer> {

    Transaction findByUserEmailAndBookBookNo(String email,String bookNo);   //this is findBy(object of user object with u in capital letter and its properties object with first letter capital) And (object of book with b in capital letter and its properties object with first letter capital)

    Transaction findByUserAndBook(User user , Book book);

}
