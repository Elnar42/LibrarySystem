package org.example.domain;

import org.example.enums.BookGenre;

import java.time.LocalDate;

public class Book implements Displayable {

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

    @Override
    public String toFormattedString() {
        return String.format("ID: %-3s | Title: %-40s | Author: %-25s | Genre: %-12s | Publication Date: %-12s | Available to borrow: %s",
                id, title, author, genre, publicationDate, isAvailable);
    }


}
