package com.practice.books;

import com.practice.books.model.Book;
import com.practice.books.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookRepository bookRepository;

    private Book book;

    @BeforeEach
    public void setUp() {
        book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setPublicationYear(2023);
        book.setGenre("Test Genre");
    }

    @Test
    public void getAllBooksTest() throws Exception {
        when(bookRepository.findAll()).thenReturn(Arrays.asList(book));

        mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'title': 'Test Book', 'author': 'Test Author', 'publicationYear': 2023, 'genre': 'Test Genre'}]"));
    }

    @Test
    public void getBookByIdTest() throws Exception {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        mockMvc.perform(get("/books/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{'title': 'Test Book', 'author': 'Test Author', 'publicationYear': 2023, 'genre': 'Test Genre'}"));
    }

    @Test
    public void getBookByIdNotFoundTest() throws Exception {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/books/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createBookTest() throws Exception {
        Book newBook = new Book();
        newBook.setTitle("New Book");
        newBook.setAuthor("New Author");
        newBook.setPublicationYear(2024);
        newBook.setGenre("New Genre");

        when(bookRepository.save(any(Book.class))).thenReturn(newBook);

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBook)))
                .andExpect(status().isOk())
                .andExpect(content().json("{'title': 'New Book', 'author': 'New Author', 'publicationYear': 2024, 'genre': 'New Genre'}"));
    }

    @Test
    public void updateBookTest() throws Exception {
        Book updatedBook = new Book();
        updatedBook.setTitle("Updated Book");
        updatedBook.setAuthor("Updated Author");
        updatedBook.setPublicationYear(2025);
        updatedBook.setGenre("Updated Genre");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

        mockMvc.perform(put("/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBook)))
                .andExpect(status().isOk())
                .andExpect(content().json("{'title': 'Updated Book', 'author': 'Updated Author', 'publicationYear': 2025, 'genre': 'Updated Genre'}"));
    }

    @Test
    public void deleteBookTest() throws Exception {
        when(bookRepository.existsById(1L)).thenReturn(true);

        mockMvc.perform(delete("/books/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Book with id 1 has been deleted successfully"));
    }

    @Test
    public void deleteBookNotFoundTest() throws Exception {
        when(bookRepository.existsById(1L)).thenReturn(false);

        mockMvc.perform(delete("/books/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
