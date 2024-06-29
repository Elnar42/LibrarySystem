package org.example.repisitory;
import org.example.domain.Book;
import org.example.enums.BookGenre;
import java.io.IOException;
import java.util.List;

public interface BookRepository {
    boolean saveAllBooks(Book... args) throws IOException;

    void saveBook(Book book) throws IOException;

    void loadAllBooks();

    boolean getBookAvailability(Long id);

    void setBookAvailability(Long id, boolean available) throws IOException;

    boolean removeBookById(Long id);

    List<Book> loadById(Long id);

    List<Book> loadByTitle(String title);

    List<Book> loadByAuthor(String author);

    List<Book> loadByGenre(BookGenre genre);
}
