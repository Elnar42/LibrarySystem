package org.example.repisitory;

import org.example.domain.Book;
import org.example.enums.BookGenre;

import java.io.IOException;
import java.util.List;

public interface BookRepository {

    boolean saveAllBooks(Book ...args) throws IOException;
    Book saveBook(Book book) throws IOException;

    boolean loadAllBooks();

    List<Book> loadByTitle(String title);

    List<Book> loadByAuthor(String author);

    List<Book> loadByGenre(BookGenre genre);
}
