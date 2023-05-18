package com.practice.books.controller;

import com.practice.books.dto.BookDto;
import com.practice.books.exception.BookNotFoundException;
import com.practice.books.mapper.BookMapper;
import com.practice.books.model.Book;
import com.practice.books.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("http://localhost:3000")
public class BookController {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookMapper bookMapper;

    // GET /books: Retrieve a list of all books
    @GetMapping("/books")
    List<BookDto> getAllBooks(){
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    // GET /books/{id}: Retrieve a specific book by ID
    @GetMapping("/books/{id}")
    BookDto getBookById(@PathVariable Long id){
        Book book = bookRepository.findById(id).
                orElseThrow(()->new BookNotFoundException(id));
        return bookMapper.toDto(book);
    }

    // POST /books: Create a new book
    @PostMapping("/books")
    BookDto newUser(@RequestBody BookDto newBookDto){
        Book book = bookMapper.toEntity(newBookDto);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    // PUT /books/{id}: Update an existing book by ID
    @PutMapping("/books/{id}")
    BookDto updateBook(@RequestBody BookDto newBookDto, @PathVariable Long id) {
        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(newBookDto.getTitle());
                    book.setAuthor(newBookDto.getAuthor());
                    book.setPublicationYear(newBookDto.getPublicationYear());
                    book.setGenre(newBookDto.getGenre());
                    Book updatedBook = bookRepository.save(book);
                    return bookMapper.toDto(updatedBook);
                })
                .orElseThrow(() -> new BookNotFoundException(id));
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
