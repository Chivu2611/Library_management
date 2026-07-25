package com.knf.dev.librarymanagementsystem.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.knf.dev.librarymanagementsystem.entity.User;
import com.knf.dev.librarymanagementsystem.service.BorrowService;
import com.knf.dev.librarymanagementsystem.service.FavoriteService;
import com.knf.dev.librarymanagementsystem.service.UserService;

@Controller
public class BorrowController {

    private final BorrowService borrowService;
    private final UserService userService;
    private final FavoriteService favoriteService;


    public BorrowController(
            BorrowService borrowService,
            UserService userService,
            FavoriteService favoriteService) {

        this.borrowService = borrowService;
        this.userService = userService;
        this.favoriteService = favoriteService;
    }


    // =========================
    // MƯỢN SÁCH
    // =========================

    @PostMapping("/borrow/{bookId}")
    public String borrowBook(
            @PathVariable("bookId") Long bookId,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        User user = userService.findByEmail(
                principal.getName()
        );

        try {

            borrowService.borrowBook(bookId, user);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Mượn sách thành công!"
            );

        } catch (RuntimeException e) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    e.getMessage()
            );
        }

        return "redirect:/book/" + bookId;
    }


    // =========================
    // SÁCH CỦA TÔI
    // =========================

    @GetMapping("/my-borrows")
    public String myBorrows(
            Principal principal,
            Model model) {

        User user = userService.findByEmail(
                principal.getName()
        );


        // Sách đã mượn
        model.addAttribute(
                "borrows",
                borrowService.findByUser(user)
        );


        // Sách yêu thích
        model.addAttribute(
                "favorites",
                favoriteService.findByUser(user)
        );


        return "my-borrows";
    }


    // =========================
    // TRẢ SÁCH
    // =========================

    @PostMapping("/return-book/{borrowId}")
    public String returnBook(
            @PathVariable("borrowId") Long borrowId) {

        borrowService.returnBook(borrowId);

        return "redirect:/my-borrows";
    }
}