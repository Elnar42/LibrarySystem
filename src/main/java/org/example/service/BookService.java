package org.example.service;

import org.example.domain.Book;
import org.example.enums.BookGenre;
import org.example.repisitory.BookRepository;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class BookService implements BookRepository {
    public static HashMap<Long, Book> books = new HashMap<>();

    @Override
    public boolean saveAllBooks(Book... args) {
        for (Book book : args) {
            saveBook(book);
        }
        return true;
    }

    @Override
    public void saveBook(Book book) {
        loadAllBooks();
        Set<Long> ids = books.keySet();
        if (ids.contains(book.getId()))
            throw new IllegalArgumentException("Book with id (" + book.getId() + ") is already in library!");
        books.clear();
        File file = new File("book_database.txt");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            bw.write(book.toString());
            bw.newLine();
        } catch (IOException io) {
            System.out.println("Unable to save book!");
        }
    }

    @Override
    public void loadAllBooks() {
        File file = new File("book_database.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String details;
            while ((details = br.readLine()) != null) {
                String[] detail = details.split(",");
                Book book = new Book(Long.parseLong(detail[0]), detail[1], detail[2], BookGenre.valueOf(detail[3]), LocalDate.parse(detail[4]), Boolean.parseBoolean(detail[5]));
                books.put(Long.parseLong(detail[0]), book);
            }
        } catch (IOException io) {
            System.out.println("Unable to save book!");
        }
    }

    @Override
    public boolean getBookAvailability(Long id) {
        loadAllBooks();
        if (books.entrySet().stream().noneMatch(t -> t.getValue().getId().equals(id)))
            throw new IllegalArgumentException("Book with given id (" + id + ") does not exist in the library!");
        boolean isAvailable = books.get(id).isAvailable();
        books.clear();
        return isAvailable;
    }

    @Override
    public void setBookAvailability(Long id, boolean available) throws IOException {
        loadAllBooks();
        if (books.entrySet().stream().noneMatch(t -> t.getValue().getId().equals(id)))
            throw new IllegalArgumentException("Book with given id (" + id + ") does not exist in the library!");
        Book book = books.get(id);
        book.setAvailable(available);
        List<Book> books1 = new ArrayList<>();
        books.forEach((key, value) -> books1.add(value));
        saveBookWithoutAppend(books1);
        books.clear();
    }


    @Override
    public boolean removeBookById(Long id) {
        List<Book> bookList = new ArrayList<>();
        File file = new File("book_database.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String details;
            while ((details = br.readLine()) != null) {
                String[] detail = details.split(",");
                if (detail[0].isEmpty()) break;
                Book book = new Book(Long.parseLong(detail[0]), detail[1], detail[2], BookGenre.valueOf(detail[3]), LocalDate.parse(detail[4]), Boolean.parseBoolean(detail[5]));
                if (book.getId().equals(id)) continue;
                bookList.add(book);
            }
            saveBookWithoutAppend(bookList);
            return true;
        } catch (IOException io) {
            return false;
        }
    }

    @Override
    public List<Book> loadById(Long id) {
        return loadBook("id", id);
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

    private <T> List<Book> loadBook(String loader, T loadBy) {
        List<Book> books = new LinkedList<>();
        File file = new File("book_database.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String details;
            while ((details = br.readLine()) != null) {
                String[] detail = details.split(",");
                if (detail[0].isEmpty()) return null;
                switch (loader) {
                    case "author" -> {
                        if (detail[2].equals(loadBy)) {
                            Book book = new Book(Long.parseLong(detail[0]), detail[1], detail[2], BookGenre.valueOf(detail[3]), LocalDate.parse(detail[4]), Boolean.parseBoolean(detail[5]));
                            books.add(book);
                        }
                    }
                    case "genre" -> {
                        if (detail[3].equals(loadBy.toString())) {
                            Book book = new Book(Long.parseLong(detail[0]), detail[1], detail[2], BookGenre.valueOf(detail[3]), LocalDate.parse(detail[4]), Boolean.parseBoolean(detail[5]));
                            books.add(book);
                        }
                    }
                    case "title" -> {
                        if (detail[1].equals(loadBy)) {
                            Book book = new Book(Long.parseLong(detail[0]), detail[1], detail[2], BookGenre.valueOf(detail[3]), LocalDate.parse(detail[4]), Boolean.parseBoolean(detail[5]));
                            books.add(book);
                        }
                    }
                    case "id" -> {
                        if (detail[0].equals(String.valueOf(loadBy))) {
                            Book book = new Book(Long.parseLong(detail[0]), detail[1], detail[2], BookGenre.valueOf(detail[3]), LocalDate.parse(detail[4]), Boolean.parseBoolean(detail[5]));
                            books.add(book);
                        }
                    }
                }
            }
            return books;

        } catch (IOException io) {
            return null;
        }
    }

    private void saveBookWithoutAppend(List<Book> books) throws IOException {
        File file = new File("book_database.txt");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
            for (Book book : books) {
                bw.write(book.toString());
                bw.newLine();
            }
        } catch (IOException io) {
            System.out.println("Unable to save book!");
        }
    }
}
