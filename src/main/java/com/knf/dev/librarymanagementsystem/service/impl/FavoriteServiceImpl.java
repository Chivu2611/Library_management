package com.knf.dev.librarymanagementsystem.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.knf.dev.librarymanagementsystem.entity.Book;
import com.knf.dev.librarymanagementsystem.entity.Favorite;
import com.knf.dev.librarymanagementsystem.entity.User;
import com.knf.dev.librarymanagementsystem.repository.FavoriteRepository;
import com.knf.dev.librarymanagementsystem.service.FavoriteService;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;

    public FavoriteServiceImpl(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    @Override
    public void addFavorite(User user, Book book) {

        if (!favoriteRepository.existsByUserAndBook(user, book)) {

            Favorite favorite = new Favorite(user, book);

            favoriteRepository.save(favorite);
        }
    }

    @Override
    public void removeFavorite(User user, Book book) {

        favoriteRepository
                .findByUserAndBook(user, book)
                .ifPresent(favoriteRepository::delete);
    }

    @Override
    public boolean isFavorite(User user, Book book) {

        return favoriteRepository
                .existsByUserAndBook(user, book);
    }

    @Override
    public List<Favorite> findByUser(User user) {

        return favoriteRepository.findByUser(user);
    }
}