package com.example.Minor_Project.controller;


import com.example.Minor_Project.dto.TransactionRequest;
import com.example.Minor_Project.exceptions.TransactionException;
import com.example.Minor_Project.model.Transaction;
import com.example.Minor_Project.model.User;
import com.example.Minor_Project.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping("/issue")
    public ResponseEntity<?> issueBook(@RequestBody @Valid TransactionRequest request){
//        Transaction  createdTransaction = null;
//
//        //Method 1 to handle exception thrown to frontend
//        try{  //we put this in try catch bcoz when we look to issue a book which is already issued,it throws 500 Internal Server error and in frontend to show what is the issue we do this.
//            createdTransaction = transactionService.issueBook(request);
//        }catch(TransactionException transactionException){
//            return new ResponseEntity<>(transactionException.getMessage(),HttpStatus.BAD_REQUEST);
//        }
//        return new ResponseEntity<>(createdTransaction, HttpStatus.OK);

        //fetch user details from SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if(!user.getEmail().equals(request.getUserEmail())){
            throw new TransactionException("You cannot issue book to some other user");
        }

        Transaction  createdTransaction =  transactionService.issueBook(request);
        return new ResponseEntity<>(createdTransaction, HttpStatus.OK);

    }

    @PutMapping("/return")
    public ResponseEntity<Integer> returnBook(@RequestBody @Valid TransactionRequest transactionRequest){ //returns settlement_Amount
        Integer settlementAmount = transactionService.returnBook(transactionRequest);
        return new ResponseEntity<>(settlementAmount, HttpStatus.OK);


    }
}
