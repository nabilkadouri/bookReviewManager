package com.monsite.service;

import com.monsite.data.FakeDatabase;
import com.monsite.model.Book;
import com.monsite.model.Genre;
import com.monsite.model.Review;

import java.util.*;
import java.util.stream.Collectors;


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

    public void afficherLivresTrieParMoyenneNotes() {
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

    public void afficherAuteurTrieParCritique(String nomAuteur) {
        System.out.println("\n--- Critiques de l'auteur " + nomAuteur + " (triées par date décroissante) ---");

        Map<Integer, Book> livresParId = this.books.stream()
                .collect(Collectors.toMap(Book::getId, book -> book));
        List<Review> critiquesTrieesPourAffichage = this.reviews.stream()
                .filter(critique -> {
                    Book livreAssocie = livresParId.get(critique.getId());
                    return livreAssocie != null && livreAssocie.getAuthor().equalsIgnoreCase(nomAuteur);
                })
                .sorted(Comparator.comparing(Review::getDate).reversed())
                .collect(Collectors.toList());

        if (critiquesTrieesPourAffichage.isEmpty()) {
            System.out.println("--- Aucune critique trouvée pour l'auteur : " + nomAuteur + " ---");
        } else {
            for (Review critique : critiquesTrieesPourAffichage) {
                Book livreAssocie = livresParId.get(critique.getId());
                String titreLivre = (livreAssocie != null) ? livreAssocie.getTitle() : "Titre inconnu";

                System.out.println("  Livre: '" + titreLivre + "'");
                System.out.println("    - Date: " + critique.getDate());
                System.out.println("    - Pseudo: " + critique.getPseudo());
                System.out.println("    - Note: " + critique.getNote());
                System.out.println("    - Commentaire: '" + critique.getComment() + "'");
                System.out.println("    --------------------");
            }
        }
        System.out.println("--------------------------------------------------------------------");
    }

    public void rechercherMeilleursLivresParGenre() {
        System.out.println("\n--- Meilleurs Livres par Genre (basé sur la meilleure moyenne) ---");
        Map<Integer, Double> tableauIdNotesMoyennes = calculerLaMoyenneDesNotes();
        Map<Genre, Optional<Book>> meilleursLivresParGenreOptional = this.books.stream()
                .collect(Collectors.groupingBy(
                        Book::getGenre,
                        Collectors.maxBy(Comparator.comparingDouble(book ->
                                tableauIdNotesMoyennes.getOrDefault(book.getId(), 0.0)
                        ))
                ));

        for (Map.Entry<Genre, Optional<Book>> entry : meilleursLivresParGenreOptional.entrySet()) {
            Genre genre = entry.getKey();
            Optional<Book> meilleurLivreOptional = entry.getValue();

            System.out.println("\n--- Genre : " + genre + " ---");


            if (meilleurLivreOptional.isPresent()) {
                Book meilleurLivre = meilleurLivreOptional.get();

                double moyenneAffichee = tableauIdNotesMoyennes.getOrDefault(meilleurLivre.getId(), 0.0);

                System.out.println("  Titre: " + meilleurLivre.getTitle());
                System.out.println("  Auteur: " + meilleurLivre.getAuthor());
                System.out.println("  Moyenne des notes: " + String.format("%.2f", moyenneAffichee));
            } else {
                System.out.println("  Aucun livre trouvé avec des critiques pour ce genre.");
            }
        }
        System.out.println("--------------------------------------------------");
    }

    public void afficherCritiquesLivresAnciens(int anneeLimite) {
        System.out.println("\n--- Critiques des livres publiés avant " + anneeLimite + " ---");

        Map<Integer, List<Review>> critiquesParIdLivre = filtrerCritiqueParIdLivre();
        this.books.stream()
                .filter(book -> book.getAnneePublication() < anneeLimite)
                .forEach(book -> {
                    System.out.println("\nLivre: " + book.getTitle() + " (Auteur: " + book.getAuthor() + ", Année: " + book.getAnneePublication() + ")");

                    List<Review> critiquesDuLivre = critiquesParIdLivre.getOrDefault(book.getId(), new ArrayList<>());

                    if (!critiquesDuLivre.isEmpty()) {
                        System.out.println("  Critiques :");
                        critiquesDuLivre.forEach(review -> {
                            System.out.println("    - Pseudo: " + review.getPseudo() + ", Note: " + review.getNote() + ", Commentaire: '" + review.getComment() + "'");
                        });
                    } else {
                        System.out.println("  Aucune critique pour ce livre.");
                    }
                });

        System.out.println("--------------------------------------------------");
    }

}
