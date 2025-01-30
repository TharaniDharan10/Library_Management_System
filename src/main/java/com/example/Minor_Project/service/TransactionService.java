package com.example.Minor_Project.service;

import com.example.Minor_Project.dto.TransactionRequest;
import com.example.Minor_Project.enums.TransactionStatus;
import com.example.Minor_Project.enums.UserStatus;
import com.example.Minor_Project.enums.UserType;
import com.example.Minor_Project.exceptions.TransactionException;
import com.example.Minor_Project.model.Book;
import com.example.Minor_Project.model.Transaction;
import com.example.Minor_Project.model.User;
import com.example.Minor_Project.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionService {

    @Autowired
    UserService userService;

    @Autowired
    BookService bookService;

    @Autowired
    TransactionRepository transactionRepository;

    @Value("${book.maximum.validity}")    //this is used to retrieve values from application.properties
    int validDays;   //so now 14 is fetched here

    @Value("${book.fine.per.day}")
    int finePerDay;

    //as this class is annotated with FieldDefaults,so any property inside this class is private now,so inorder to access any property from outside class area especially for unit test,dont use methods like this.
    //Instead in Test ,use ReflectionTestUtils.setField(class_object , parameter , value)
    public void setValidDays(int days){
        validDays = days;
    }

    public Transaction issueBook(TransactionRequest request) {

        User user = fetchUser(request); //as this and next line just fetching book and not updating any table,we havenot put them inside @Transactional below and added separately

        if(user.getUserStatus() == UserStatus.BLOCKED){
            throw new TransactionException("User is blocked ,book cannot be issued");
        }

        Book book = fetchBook(request);
        if(book.getUser() != null){
            throw new TransactionException("Book is already issued to other user");
        }

        return issueBook(user,book);
    }

    @Transactional
    protected Transaction issueBook(User user,Book book){
        Transaction transaction = Transaction.builder()
                .book(book)
                .user(user)
                .transactionId(UUID.randomUUID().toString().substring(0,30)) //we did substring here from 0 to 30 as our TransactionId is set to length 30
                .settlementAmount(-book.getSecurityAmount())
                .transactionStatus(TransactionStatus.ISSUED)
                .build();

        transaction = transactionRepository.save(transaction);
        book.setUser(user);
        bookService.updateBookMetaData(book);
        return transaction;
    }
    public User fetchUser(TransactionRequest request){
        User user = userService.fetchUserByEmail(request.getUserEmail());

        if(user == null){
            throw new TransactionException("User does not exist in library");
        }

        if(user.getUserType() != UserType.STUDENT){
            throw new TransactionException("User is not of type student");  //if i dont want for both same exception different message is shown and i want same message,then i can use findByEmailAndUserType in UserRepository

        }



        return user;
    }

    private Book fetchBook(TransactionRequest request){
        Book book = bookService.getBookByBookNo(request.getBookNo());

        if(book == null){
            throw new TransactionException("Book does not exist in library");
        }


        return book;
    }


    public Integer returnBook(TransactionRequest request) {

        User user = fetchUser(request);

        Book book = fetchBook(request);
        if(book.getUser() != user){
            throw new TransactionException("Book is issued to some other user");
        }

        Transaction transaction = transactionRepository.findByUserEmailAndBookBookNo(request.getUserEmail() , request.getBookNo());
        return returnBook(transaction,book);


    }

    @Transactional
    protected Integer returnBook(Transaction transaction , Book book) {


        int amount = calculateFine(transaction);
        transactionRepository.save(transaction);
        book.setUser(null);
        bookService.updateBookMetaData(book);
        return amount;

    }

    public int calculateFine(Transaction transaction ){
        long issuedDateInTime = transaction.getCreatedOn().getTime(); //getCreatedOn() is of type Date .SO getTime will get it in millisec
        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime - issuedDateInTime; //in millisec

        long days = TimeUnit.MILLISECONDS.toDays(timeDifference);  //in days


        int amount = 0;
        if (days > validDays) {
            //some fine is there
            int fine = (int) (days - validDays) * finePerDay;
//            if(fine > Math.abs(transaction.getSettlementAmount())){
//                //take some money
//                amount = fine - Math.abs(transaction.getSettlementAmount());
//                transaction.setSettlementAmount(-fine);
//
//            }else{
//                //return some money
//                amount = fine - Math.abs(transaction.getSettlementAmount());
//                transaction.setSettlementAmount(-fine);
//            }

            amount = fine - Math.abs(transaction.getSettlementAmount());
            transaction.setSettlementAmount(-fine);
            transaction.setTransactionStatus(TransactionStatus.FINED);


        } else {
            transaction.setTransactionStatus(TransactionStatus.RETURNED);
            amount = transaction.getSettlementAmount();
            transaction.setSettlementAmount(0);
        }
        return amount;
    }


}

