package com.knf.dev.librarymanagementsystem.entity;

import javax.persistence.*;

@Entity
@Table(
    name = "favorites",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"user_id", "book_id"}
    )
)
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;


    public Favorite() {
    }

    public Favorite(User user, Book book) {
        this.user = user;
        this.book = book;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}