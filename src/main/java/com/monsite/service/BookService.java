package com.monsite.service;

import com.monsite.data.FakeDatabase;
import com.monsite.model.Book;
import com.monsite.model.Review;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BookService {

    List<Book> books = new ArrayList<>(FakeDatabase.getBooks());
    List<Review> reviews = FakeDatabase.getReviews();

    public Map<Integer, List<Review>> filtrerCritiqueParIdLivre() {
        Map<Integer, List<Review>> critiqueParIdLivre = new HashMap<>();
        for (Review review : reviews) {
            int bookId = review.getId();
            if (critiqueParIdLivre.containsKey(bookId)) {
                List<Review> avisExistant = critiqueParIdLivre.get(bookId);
                avisExistant.add(review);
            } else {
                List<Review> nouvelleListeLivre = new ArrayList<>();
                nouvelleListeLivre.add(review);
                critiqueParIdLivre.put(bookId, nouvelleListeLivre);
            }
        }

        return critiqueParIdLivre;

    }

    public Map<Integer, Double> calculerLaMoyenneDesNotes() {
        Map<Integer, List<Review>> critique = filtrerCritiqueParIdLivre();
        Map<Integer, Double> moyennesParLivre = new HashMap<>();

        for (Map.Entry<Integer, List<Review>> entry : critique.entrySet()) {
            Integer bookId = entry.getKey();
            List<Review> avisList = entry.getValue();

            double sommeNotes = 0.0;
            for (Review review : avisList) {
                sommeNotes += review.getNote();
            }

            double moyenne = 0.0;
            if (!avisList.isEmpty()) {
                moyenne = sommeNotes / avisList.size();
            }
            moyennesParLivre.put(bookId, moyenne);

        }
        return moyennesParLivre;
    }

    public void trierMoyenneNotes() {
        Map<Integer, Double> tableauIdNotesMoyennes = calculerLaMoyenneDesNotes();
        List<Book> tousLesLivres = this.books;

        tousLesLivres.sort((book1, book2) -> {
            double moyenne1 = tableauIdNotesMoyennes.getOrDefault(book1.getId(), 0.0);
            double moyenne2 = tableauIdNotesMoyennes.getOrDefault(book2.getId(), 0.0);
            return Double.compare(moyenne2, moyenne1);
        });

        System.out.println("\n--- Liste des livres triés par moyenne des notes (du plus haut au plus bas) ---");
        for (Book book : tousLesLivres) {
            double moyenneAffichee = tableauIdNotesMoyennes.getOrDefault(book.getId(), 0.0);
            System.out.println("  " + book.getTitle() + " (Auteur: " + book.getAuthor() + ")" +
                    " - Moyenne: " + String.format("%.2f", moyenneAffichee));
        }
    }
}
