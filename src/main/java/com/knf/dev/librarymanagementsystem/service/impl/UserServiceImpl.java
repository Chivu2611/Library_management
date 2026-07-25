package com.knf.dev.librarymanagementsystem.service.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.knf.dev.librarymanagementsystem.entity.Role;
import com.knf.dev.librarymanagementsystem.entity.User;
import com.knf.dev.librarymanagementsystem.repository.UserRepository;
import com.knf.dev.librarymanagementsystem.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserRepository userRepository,
            BCryptPasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    // =========================
    // ĐĂNG KÝ USER
    // =========================

    @Override
    public User save(User user) {

        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        user.setRoles(
                Arrays.asList(new Role("ROLE_USER"))
        );

        return userRepository.save(user);
    }


    // =========================
    // TÌM USER THEO EMAIL
    // =========================

    @Override
    public User findByEmail(String email) {

        return userRepository.findByEmail(email);
    }


    // =========================
    // ĐĂNG NHẬP
    // =========================

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        var user = userRepository.findByEmail(username);

        if (user == null) {

            throw new UsernameNotFoundException(
                    "Email hoặc mật khẩu không chính xác."
            );
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles())
        );
    }


    // =========================
    // CHUYỂN ROLE → AUTHORITY
    // =========================

    private Collection<? extends GrantedAuthority>
            mapRolesToAuthorities(Collection<Role> roles) {

        return roles.stream()
                .map(role ->
                        new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}