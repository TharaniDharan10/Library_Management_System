package com.example.Minor_Project.repository;

import com.example.Minor_Project.model.Book;
import com.example.Minor_Project.model.Transaction;
import com.example.Minor_Project.model.User;
import com.sun.source.tree.LambdaExpressionTree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Integer> {

    Transaction findByUserEmailAndBookBookNo(String email,String bookNo);

    Transaction findByUserAndBook(User user , Book book);
}
