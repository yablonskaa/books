package com.practice.books.exception;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(Long id) {
        super("Could not find the book with id " + id);
    }
}
