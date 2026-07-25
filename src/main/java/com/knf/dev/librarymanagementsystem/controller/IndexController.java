package com.knf.dev.librarymanagementsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.knf.dev.librarymanagementsystem.service.AuthorService;
import com.knf.dev.librarymanagementsystem.service.BookService;
import com.knf.dev.librarymanagementsystem.service.CategoryService;
import com.knf.dev.librarymanagementsystem.service.PublisherService;

@Controller
public class IndexController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final CategoryService categoryService;
    private final PublisherService publisherService;


    public IndexController(
            BookService bookService,
            AuthorService authorService,
            CategoryService categoryService,
            PublisherService publisherService) {

        this.bookService = bookService;
        this.authorService = authorService;
        this.categoryService = categoryService;
        this.publisherService = publisherService;
    }


    // =========================
    // LOGIN
    // =========================

    @GetMapping("/login")
    public String login() {
        return "login";
    }


    // =========================
    // DASHBOARD ADMIN
    // =========================

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }


    // =========================
    // TRANG CHỦ USER
    // =========================

    @GetMapping("/welcome")
    public String welcome(Model model) {

        // Danh sách sách
        model.addAttribute(
                "books",
                bookService.findAllBooks()
        );

        // Danh sách tác giả
        model.addAttribute(
                "authors",
                authorService.findAllAuthors()
        );

        // Danh sách thể loại
        model.addAttribute(
                "categories",
                categoryService.findAllCategories()
        );

        // Danh sách nhà xuất bản
        model.addAttribute(
                "publishers",
                publisherService.findAllPublishers()
        );

        return "user-welcome";
    }
}