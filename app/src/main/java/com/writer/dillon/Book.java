package com.writer.dillon;

public class Book {
    private float price;
    private String title;
    private String description;
    private String author;

    public Book() {
    }

    public Book(String title, String description, String author, float price){
        this.title = title;
        this.description = description;
        this.author = author;
        this.price = price;
    }


    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
