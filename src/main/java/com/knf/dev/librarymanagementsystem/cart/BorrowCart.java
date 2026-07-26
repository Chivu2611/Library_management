package com.knf.dev.librarymanagementsystem.cart;

import java.util.ArrayList;
import java.util.List;

public class BorrowCart {

    private List<Long> bookIds = new ArrayList<>();

    public List<Long> getBookIds() {
        return bookIds;
    }

    public void setBookIds(List<Long> bookIds) {
        this.bookIds = bookIds;
    }

    public void addBook(Long bookId) {

        if (!bookIds.contains(bookId)) {
            bookIds.add(bookId);
        }

    }

    public void removeBook(Long bookId) {
        bookIds.remove(bookId);
    }

    public void clear() {
        bookIds.clear();
    }

    public int size() {
        return bookIds.size();
    }
}