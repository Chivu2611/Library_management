package com.knf.dev.librarymanagementsystem.service;

import java.util.List;

import com.knf.dev.librarymanagementsystem.entity.Borrow;
import com.knf.dev.librarymanagementsystem.entity.User;

public interface BorrowService {

    Borrow borrowBook(Long bookId, User user);

    List<Borrow> findByUser(User user);

    List<Borrow> findAllBorrows();

    void returnBook(Long borrowId);

    // Kiểm tra user có đang mượn sách này không
    boolean isBorrowing(User user, Long bookId);
}