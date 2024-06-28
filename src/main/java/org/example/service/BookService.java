package org.example.service;
import org.example.domain.Book;
import org.example.enums.BookGenre;
import org.example.repisitory.BookRepository;
import java.io.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
public class BookService implements BookRepository {
    public static HashMap<Long, Book>  books = new HashMap<>();

    @Override
    public boolean saveAllBooks(Book... args) throws IOException {
        for(Book book: args){
            saveBook(book);
        }
        return true;
    }
    @Override
    public Book saveBook(Book book) throws IOException {
        loadAllBooks();
       Set<Long> ids = books.keySet();
       if(ids.contains(book.getId())) throw new IllegalArgumentException("Book with id (" + book.getId() + ") is already in library!");
       books.clear();
        File file = new File("book_database.txt");
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))){
            bw.write(book.toString());
            bw.newLine();
            return book;
        }catch(IOException io){
            return null;
        }
    }

    @Override
    public boolean loadAllBooks() {
        File file = new File("book_database.txt");
        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            String details;
            while((details = br.readLine()) != null){
                String [] detail = details.split(",");
                if(detail[0].isEmpty()) break;
                Book book = new Book(Long.parseLong(detail[0]), detail[1], detail[2], BookGenre.valueOf(detail[3]), LocalDate.parse(detail[4]));
                books.put(Long.parseLong(detail[0]), book);
            }
            return true;
        }catch(IOException io){
            return false;
        }
    }

    @Override
    public List<Book> loadByTitle(String title) {
       return loadBook("title", title);

    }

    @Override
    public List<Book> loadByAuthor(String author) {
        return loadBook("author", author);
    }

    @Override
    public List<Book> loadByGenre(BookGenre genre) {
        return loadBook("genre", genre);
    }
    private <T> List<Book> loadBook(String loader, T loadBy){
        List<Book> books = new LinkedList<>();
        File file = new File("book_database.txt");
        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            String details;
            while((details = br.readLine()) != null){
                String [] detail = details.split(",");
                if(detail[0].isEmpty()) return null;
                switch (loader) {
                    case "author" -> {
                        if (detail[2].equals(loadBy)) {
                            Book book = new Book(Long.parseLong(detail[0]), detail[1], detail[2], BookGenre.valueOf(detail[3]), LocalDate.parse(detail[4]));
                            books.add(book);
                        }
                    }
                    case "genre" -> {
                        if (detail[3].equals(loadBy.toString())) {
                            Book book = new Book(Long.parseLong(detail[0]), detail[1], detail[2], BookGenre.valueOf(detail[3]), LocalDate.parse(detail[4]));
                            books.add(book);
                        }
                    }
                    case "title" -> {
                        if (detail[1].equals(loadBy)) {
                            Book book = new Book(Long.parseLong(detail[0]), detail[1], detail[2], BookGenre.valueOf(detail[3]), LocalDate.parse(detail[4]));
                            books.add(book);
                        }
                    }
                }
            }
            return books;

        }catch(IOException io){
            return null;
        }
    }
}
