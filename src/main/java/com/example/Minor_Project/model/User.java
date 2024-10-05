package com.example.Minor_Project.model;


import com.example.Minor_Project.enums.UserStatus;
import com.example.Minor_Project.enums.UserType;
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
@ToString
@Builder
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)  // as we know that in a POJO table all parameters will be private,if we dont want to mention everytime ,we can add this line and it makes all non static fileds as private

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(length = 30)
    String name;

    @Column(unique = true, nullable = false, length = 50)//tels if there are n no of rows in a table,all of them should be unique
    String email;

    @Column(unique = true, nullable = false, length = 50)
//    @Column(unique = true, length = 15) // we didnot mention nullable bcoz i want phNo to be optional in my case
    String phoneNo;

    String temp;

    String address; // length by default is 255

    @Enumerated(value = EnumType.STRING) //(this class doesnot know that there is a enum already present,so we add this.If we didnot mention this value ,then by default ,ORDINAL number will be stored in DB.Making it string will make it get stored in DB as ADMIN and STUDENT
    UserType userType; //ADMIN , STUDENT     //this should never be taken from client during registration


    @Enumerated
    UserStatus userStatus;//0,1,2    [ORDINAL values]

    @OneToMany(mappedBy = "user")  // one useer can have many books
    List<Book> books;


    @OneToMany(mappedBy = "user")
    List<Transaction> transactions;

    @CreationTimestamp(source = SourceType.DB)//provided by JPA .By default its SourceType is SourceType.VM where VM is virtual machine.Setting it to DB will createTimeStamp based on where mysql server is running in which country server;
    Date createdOn;

    @UpdateTimestamp//provided by JPA . Similarly here also like CreationTimeStamp
    Date updatedOn;
}

