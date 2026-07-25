package com.knf.dev.librarymanagementsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.knf.dev.librarymanagementsystem.entity.User;
import com.knf.dev.librarymanagementsystem.service.UserService;

@Controller
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    // Hiển thị trang đăng ký
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {

        model.addAttribute("user", new User());

        return "register";
    }

    // Xử lý đăng ký
    @PostMapping("/register")
    public String registerUser(User user, Model model) {

        // Kiểm tra email đã tồn tại
        if (userService.findByEmail(user.getEmail()) != null) {

            model.addAttribute(
                    "error",
                    "Email này đã được sử dụng."
            );

            return "register";
        }

        userService.save(user);

        return "redirect:/login?registerSuccess";
    }
}