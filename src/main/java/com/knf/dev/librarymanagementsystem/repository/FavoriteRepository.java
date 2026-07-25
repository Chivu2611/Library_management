package com.knf.dev.librarymanagementsystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.knf.dev.librarymanagementsystem.entity.Book;
import com.knf.dev.librarymanagementsystem.entity.Favorite;
import com.knf.dev.librarymanagementsystem.entity.User;

public interface FavoriteRepository
        extends JpaRepository<Favorite, Long> {

    List<Favorite> findByUser(User user);

    Optional<Favorite> findByUserAndBook(
            User user,
            Book book
    );

    boolean existsByUserAndBook(
            User user,
            Book book
    );
}