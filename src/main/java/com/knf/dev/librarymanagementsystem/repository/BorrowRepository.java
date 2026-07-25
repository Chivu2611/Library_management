package com.knf.dev.librarymanagementsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.knf.dev.librarymanagementsystem.entity.Book;
import com.knf.dev.librarymanagementsystem.entity.Borrow;
import com.knf.dev.librarymanagementsystem.entity.User;

public interface BorrowRepository extends JpaRepository<Borrow, Long> {

    List<Borrow> findByUserOrderByBorrowDateDesc(User user);

    boolean existsByUserAndBookAndStatus(
            User user,
            Book book,
            String status
    );
}