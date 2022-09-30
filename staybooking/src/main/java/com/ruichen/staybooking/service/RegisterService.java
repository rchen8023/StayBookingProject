package com.ruichen.staybooking.service;

import com.ruichen.staybooking.exception.UserAlreadyExistException;
import com.ruichen.staybooking.model.Authority;
import com.ruichen.staybooking.model.User;
import com.ruichen.staybooking.model.UserRole;
import com.ruichen.staybooking.repository.AuthorityRepository;
import com.ruichen.staybooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterService {

    private UserRepository userRepository;
    private AuthorityRepository authorityRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public RegisterService(UserRepository userRepository, AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //This annotation is to tell system in this function save user and authority must success together or fail together.
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void add(User user, UserRole role) {
        if(userRepository.existsById(user.getUsername())) {
            throw new UserAlreadyExistException("User Already Exist");
        }
        //passwordEncoder --> encrypt password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);

        userRepository.save(user);
        authorityRepository.save(new Authority(user.getUsername(), role.name()));
    }
}
