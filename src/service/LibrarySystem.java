package service;

import model.Book;
import java.util.ArrayList;

public class LibrarySystem {
    private ArrayList<Book> books = new ArrayList<>();

    public void addBook(String title, String author) {
        books.add(new Book(title, author));
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    //search book
    public Book findBook(String title) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                return book;
            }
        }
        return null;
    }

    //remove book
    public boolean removeBook(String title) {
        Book book = findBook(title);
        if (book != null) {
            books.remove(book);
            return true;
        }
        return false;
    }

    //borrow book
    public boolean borrowBook(String title) {
        Book book = findBook(title);
        if (book != null && book.isAvailable()) {
            book.borrowBook();
            return true;
        }
        return false;
    }

    //return book
    public boolean returnBook(String title) {
        Book book = findBook(title);
        if (book != null && !book.isAvailable()) {
            book.returnBook();
            return true;
        }
        return false;
    }
}