package com.knf.dev.librarymanagementsystem.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.knf.dev.librarymanagementsystem.entity.Book;
import com.knf.dev.librarymanagementsystem.entity.User;
import com.knf.dev.librarymanagementsystem.service.BookService;
import com.knf.dev.librarymanagementsystem.service.FavoriteService;
import com.knf.dev.librarymanagementsystem.service.UserService;

@Controller
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final UserService userService;
    private final BookService bookService;


    public FavoriteController(
            FavoriteService favoriteService,
            UserService userService,
            BookService bookService) {

        this.favoriteService = favoriteService;
        this.userService = userService;
        this.bookService = bookService;
    }


    @PostMapping("/favorite/{bookId}")
    public String addFavorite(
            @PathVariable("bookId") Long bookId,
            Principal principal) {

        User user = userService.findByEmail(
                principal.getName()
        );

        Book book = bookService.findBookById(bookId);

        favoriteService.addFavorite(user, book);

        return "redirect:/book/" + bookId;
    }


    @PostMapping("/unfavorite/{bookId}")
    public String removeFavorite(
            @PathVariable("bookId") Long bookId,
            Principal principal) {

        User user = userService.findByEmail(
                principal.getName()
        );

        Book book = bookService.findBookById(bookId);

        favoriteService.removeFavorite(user, book);

        return "redirect:/book/" + bookId;
    }
}