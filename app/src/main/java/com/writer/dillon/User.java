package com.writer.dillon;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class User {
    private List<Book> booksBought;
    Dictionary progressInBook = new Hashtable();
    String username;
    String password;
    String email;

    public User() {

    }

    public User(List<Book> booksBought, Dictionary progressInBook, String username, String password, String email) {
        this.booksBought = booksBought;
        this.progressInBook = progressInBook;
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
