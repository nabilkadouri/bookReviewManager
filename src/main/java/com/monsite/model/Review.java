package com.monsite.model;

import java.time.LocalDate;
import java.util.Objects;

public class Review {
    private int id;
    private String pseudo;
    private double note;
    private String comment;
    private LocalDate date;

    public Review(int id, String pseudo, double note, String comment, LocalDate date) {
        this.id = id;
        this.pseudo = pseudo;
        this.note = note;
        this.comment = comment;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public double getNote() {
        return note;
    }

    public void setNote(double note) {
        this.note = note;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return id == review.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", pseudo='" + pseudo + '\'' +
                ", note=" + note +
                ", comment='" + comment + '\'' +
                ", date=" + date +
                '}';
    }
}
