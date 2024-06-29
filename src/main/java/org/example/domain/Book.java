package org.example.domain;

import org.example.enums.BookGenre;
import org.example.repisitory.GeneralRepository;
import org.example.service.BookService;
import org.example.service.UserService;

import java.time.LocalDate;

import static org.example.service.BookService.books;

public class Book {

    private Long id;
    private String title;

    private String author;

    private BookGenre genre;

    private LocalDate publicationDate;

    private boolean isAvailable;

    public Book(Long id, String title, String author, BookGenre genre, LocalDate publicationDate, boolean isAvailable) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publicationDate = publicationDate;
        this.isAvailable = isAvailable;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public BookGenre getGenre() {
        return genre;
    }

    public void setGenre(BookGenre genre) {
        this.genre = genre;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        return id + "," + title + "," + author + "," + genre + "," + publicationDate + "," + isAvailable;
    }

}
