package com.knf.dev.librarymanagementsystem.controller;

import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.knf.dev.librarymanagementsystem.entity.Book;
import com.knf.dev.librarymanagementsystem.service.BookService;
import com.knf.dev.librarymanagementsystem.service.BorrowService;
import com.knf.dev.librarymanagementsystem.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.knf.dev.librarymanagementsystem.cart.BorrowCart;
import org.springframework.security.core.Authentication;

import com.knf.dev.librarymanagementsystem.entity.User;

@Controller
public class CartController {
    private final BookService bookService;
    private final BorrowService borrowService;
    private final UserService userService;

    public CartController(BookService bookService,
                            BorrowService borrowService,
                            UserService userService) {
    this.bookService = bookService;
    this.borrowService = borrowService;
    this.userService = userService;

    }

    @PostMapping("/cart/add/{bookId}")
    public String addToCart(
            @PathVariable Long bookId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        BorrowCart cart =
                (BorrowCart) session.getAttribute("borrowCart");

        if (cart == null) {

            cart = new BorrowCart();

            session.setAttribute("borrowCart", cart);
        }

        cart.addBook(bookId);
        
        Book book = bookService.findBookById(bookId);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Đã thêm \"" + book.getName() + "\" vào danh sách mượn."
        );

        return "redirect:/books/";
    }
    @GetMapping("/cart")
    public String viewCart(
            HttpSession session,
            Model model) {

        BorrowCart cart =
                (BorrowCart) session.getAttribute("borrowCart");

        List<Book> books = new ArrayList<>();

        if (cart != null) {

            for (Long id : cart.getBookIds()) {

                books.add(bookService.findBookById(id));

            }

        }

        model.addAttribute("books", books);

        return "cart";
    }

    @PostMapping("/cart/remove/{bookId}")
    public String removeFromCart(
            @PathVariable Long bookId,
            HttpSession session) {

        BorrowCart cart =
                (BorrowCart) session.getAttribute("borrowCart");

        if (cart != null) {
            cart.removeBook(bookId);
        }

        return "redirect:/cart";
    }

    @PostMapping("/cart/checkout")
    public String checkout(HttpSession session,
                        Authentication authentication) {

        BorrowCart cart =
                (BorrowCart) session.getAttribute("borrowCart");

        if (cart == null || cart.getBookIds().isEmpty()) {
            return "redirect:/cart";
        }

        User user = userService.findByEmail(authentication.getName());

        for (Long id : cart.getBookIds()) {
            borrowService.borrowBook(id, user);
        }

        cart.clear();

        return "redirect:/borrow/slip";
    }

}