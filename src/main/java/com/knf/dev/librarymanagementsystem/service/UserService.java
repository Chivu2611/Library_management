package com.knf.dev.librarymanagementsystem.service;

import com.knf.dev.librarymanagementsystem.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User save(User user);

    User findByEmail(String email);
}