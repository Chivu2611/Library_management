package com.knf.dev.librarymanagementsystem.service;

import java.util.List;

import com.knf.dev.librarymanagementsystem.entity.Book;
import com.knf.dev.librarymanagementsystem.entity.Favorite;
import com.knf.dev.librarymanagementsystem.entity.User;

public interface FavoriteService {

    void addFavorite(User user, Book book);

    void removeFavorite(User user, Book book);

    boolean isFavorite(User user, Book book);

    List<Favorite> findByUser(User user);
}