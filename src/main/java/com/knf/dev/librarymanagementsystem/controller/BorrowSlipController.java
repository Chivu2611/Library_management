package com.knf.dev.librarymanagementsystem.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.knf.dev.librarymanagementsystem.entity.Borrow;
import com.knf.dev.librarymanagementsystem.entity.User;
import com.knf.dev.librarymanagementsystem.service.BorrowService;
import com.knf.dev.librarymanagementsystem.service.UserService;

@Controller
public class BorrowSlipController {

    private final BorrowService borrowService;
    private final UserService userService;

    public BorrowSlipController(BorrowService borrowService,
                                UserService userService) {
        this.borrowService = borrowService;
        this.userService = userService;
    }

    @GetMapping("/borrow/slip")
    public String borrowSlip(Authentication authentication,
                             Model model) {

        User user = userService.findByEmail(authentication.getName());

        List<Borrow> borrows = borrowService.findByUser(user);

        model.addAttribute("user", user);
        model.addAttribute("borrows", borrows);

        return "borrow-slip";
    }

}