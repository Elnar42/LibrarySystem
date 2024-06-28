package org.example.repisitory;

import org.example.domain.Book;
import org.example.enums.BookGenre;

import java.util.List;

public interface BookRepository {

    Book findByTitle(String title);
    List<Book> findByAuthor(String author);
    List<Book> findByGenre(BookGenre genre);
}
