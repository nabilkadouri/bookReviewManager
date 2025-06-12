package com.monsite.model;

import java.time.LocalDate;
import java.util.Objects;


public class Book {
    private int id;
    private String title;
    private String author;
    private int anneePublication;
    private Genre genre;


    public Book(int id, String title, String author, int anneePublication, Genre genre ) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.anneePublication = anneePublication;
    }

    public int getId() {
        return id;
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

    public int getAnneePublication() {
        return anneePublication;
    }

    public void setAnneePublication(int anneePublication) {
        this.anneePublication = anneePublication;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id && Objects.equals(title, book.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", anneePublication=" + anneePublication +
                ", genre=" + genre +
                '}';
    }
}
