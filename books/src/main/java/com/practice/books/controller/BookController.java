package com.practice.books.controller;

import com.practice.books.exception.BookNotFoundException;
import com.practice.books.model.Book;
import com.practice.books.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class BookController {
    @Autowired
    private BookRepository bookRepository;

    // GET /books: Retrieve a list of all books
    @GetMapping("/books")
    List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    // GET /books/{id}: Retrieve a specific book by ID
    @GetMapping("/books/{id}")
    Book getBookById(@PathVariable Long id){
        return bookRepository.findById(id).
                orElseThrow(()->new BookNotFoundException(id));
    }

    // POST /books: Create a new book
    @PostMapping("/books")
    Book newUser(@RequestBody Book newBook){
        return bookRepository.save(newBook);
    }

    // PUT /books/{id}: Update an existing book by ID
    @PutMapping("/books/{id}")
    Book updateBook(@RequestBody Book newBook, @PathVariable Long id) {
        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(newBook.getTitle());
                    book.setAuthor(newBook.getAuthor());
                    book.setPublicationYear(newBook.getPublicationYear());
                    book.setGenre(newBook.getGenre());
                    return bookRepository.save(book);
                }).orElseThrow(() -> new BookNotFoundException(id));
    }

    // DELETE /books/{id}: Delete a specific book by ID
    @DeleteMapping("/books/{id}")
    String deleteBook(@PathVariable Long id){
        if (!bookRepository.existsById(id)){
            throw new BookNotFoundException(id);
        }
        bookRepository.deleteById(id);
        return "Book with id " + id + " has been deleted successfully";
    }
}
