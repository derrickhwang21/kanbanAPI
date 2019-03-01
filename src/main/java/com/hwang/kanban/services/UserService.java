package com.hwang.kanban.services;

import com.hwang.kanban.domain.User;
import com.hwang.kanban.exceptions.UsernameAlreadyExistsException;
import com.hwang.kanban.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser(User newUser){

        try{
            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
            newUser.setUsername(newUser.getUsername());
            newUser.setConfirmPassword("");
            return userRepository.save(newUser);

        }catch (Exception e){
            throw new UsernameAlreadyExistsException("Username '" + newUser.getUsername()+ "' Already Exists");
        }




    }


}
