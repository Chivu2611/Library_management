package com.knf.dev.librarymanagementsystem.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.knf.dev.librarymanagementsystem.entity.Book;
import com.knf.dev.librarymanagementsystem.entity.Borrow;
import com.knf.dev.librarymanagementsystem.entity.User;
import com.knf.dev.librarymanagementsystem.repository.BorrowRepository;
import com.knf.dev.librarymanagementsystem.service.BookService;
import com.knf.dev.librarymanagementsystem.service.BorrowService;

@Service
public class BorrowServiceImpl implements BorrowService {

    private final BorrowRepository borrowRepository;
    private final BookService bookService;


    public BorrowServiceImpl(
            BorrowRepository borrowRepository,
            BookService bookService) {

        this.borrowRepository = borrowRepository;
        this.bookService = bookService;
    }


    // =========================
    // MƯỢN SÁCH
    // =========================

    @Override
    public Borrow borrowBook(Long bookId, User user) {

        Book book = bookService.findBookById(bookId);

        boolean alreadyBorrowed =
                borrowRepository.existsByUserAndBookAndStatus(
                        user,
                        book,
                        "BORROWED"
                );

        if (alreadyBorrowed) {

            throw new RuntimeException(
                    "Bạn đang mượn cuốn sách này."
            );
        }

        Borrow borrow = new Borrow();

        borrow.setUser(user);
        borrow.setBook(book);

        borrow.setBorrowDate(LocalDate.now());

        // Hạn trả: 14 ngày
        borrow.setDueDate(
                LocalDate.now().plusDays(14)
        );

        borrow.setStatus("BORROWED");

        return borrowRepository.save(borrow);
    }


    // =========================
    // DANH SÁCH MƯỢN CỦA USER
    // =========================

    @Override
    public List<Borrow> findByUser(User user) {

        return borrowRepository
                .findByUserOrderByBorrowDateDesc(user);
    }


    // =========================
    // TẤT CẢ PHIẾU MƯỢN
    // =========================

    @Override
    public List<Borrow> findAllBorrows() {

        return borrowRepository.findAll();
    }


    // =========================
    // TRẢ SÁCH
    // =========================

    @Override
    public void returnBook(Long borrowId) {

        Borrow borrow = borrowRepository
                .findById(borrowId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Không tìm thấy phiếu mượn."
                        )
                );

        borrow.setStatus("RETURNED");
        borrow.setReturnDate(LocalDate.now());

        borrowRepository.save(borrow);
    }


    // =========================
    // KIỂM TRA ĐANG MƯỢN
    // =========================

    @Override
    public boolean isBorrowing(
            User user,
            Long bookId) {

        Book book = bookService.findBookById(bookId);

        return borrowRepository
                .existsByUserAndBookAndStatus(
                        user,
                        book,
                        "BORROWED"
                );
    }
}