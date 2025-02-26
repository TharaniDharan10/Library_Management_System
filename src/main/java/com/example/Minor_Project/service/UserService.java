package com.example.Minor_Project.service;

import com.example.Minor_Project.dto.AddUserRequest;
import com.example.Minor_Project.enums.UserType;
import com.example.Minor_Project.mapper.UserMapper;
import com.example.Minor_Project.model.User;
import com.example.Minor_Project.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {    //cause UserService is Service of POJO class User, which is implementing UserDetails

    @Autowired
    UserRepository userRepository;


    public User addStudent(AddUserRequest addUserRequest) {
        User user = UserMapper.mapToUser(addUserRequest);
        user.setUserType(UserType.STUDENT);
        user.setAuthorities("STUDENT"); //added after learning Spring Security

        return userRepository.save(user);
    }


    public User fetchUserByEmail(String email){
       return userRepository.findByEmail(email);
    }

//    public User addAdmin(AddUserRequest addUserRequest) {
//        User user = UserMapper.mapToUser(addUserRequest);
//        user.setUserType(UserType.ADMIN);
//    }

    public void abc(){

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {   //unique identifier
        User user = userRepository.findByEmail(username);

        if(user != null){
            return user;

        }
        throw new UsernameNotFoundException(username.concat(" doesnot exist")); //thrown when username is not found in DB

    }

    public User addAdmin(@Valid AddUserRequest addUserRequest){
        User user = UserMapper.mapToUser(addUserRequest);
        user.setUserType(UserType.ADMIN);
        user.setAuthorities("ADMIN");   //if i want ADMIN to have authorities of STUDENT also,i can simply do user.setAuthorities("ADMIN,STUDENT");

        return userRepository.save(user);
    }
}
