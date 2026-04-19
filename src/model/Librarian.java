package model;

import service.LibrarySystem;

public class Librarian {

    private String name;
    private LibrarySystem system;

    public Librarian(String name, LibrarySystem system) {
        this.name = name;
        this.system = system;
    }

    public String getName() {
        return name;
    }

    //for terminal notes
    public void addBook(String title, String author) {
        system.addBook(title, author);
        System.out.println(name + " added a book: " + title);
    }

    public boolean removeBook(String title) {
        return system.removeBook(title);
    }
}
