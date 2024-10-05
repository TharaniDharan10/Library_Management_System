package com.example.Minor_Project.service;

import com.example.Minor_Project.dto.AddUserRequest;
import com.example.Minor_Project.enums.UserType;
import com.example.Minor_Project.mapper.UserMapper;
import com.example.Minor_Project.model.User;
import com.example.Minor_Project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;


    public User addStudent(AddUserRequest addUserRequest) {
        User user = UserMapper.mapToUser(addUserRequest);
        user.setUserType(UserType.STUDENT);

        return userRepository.save(user);
    }


    public User fetchUserByEmail(String email){
       return userRepository.findByEmail(email);
    }

//    public User addAdmin(AddUserRequest addUserRequest) {
//        User user = UserMapper.mapToUser(addUserRequest);
//        user.setUserType(UserType.ADMIN);
//    }
}
