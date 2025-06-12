package com.monsite.service;

import com.monsite.data.FakeDatabase;
import com.monsite.model.Book;
import com.monsite.model.Genre;
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

    public void AfficherLivresTrieParMoyenneNotes() {
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

    public void AfficherAuteurTrieParCritique (String  nomAuteur) {
        //Récupération des listes des livres et critiques
        List<Review> critiques = this.reviews;
        List<Book> livres = this.books;

        //Création d'un Map (dictionnaire) clé= id /valeur = objet livre
        Map<Integer, Book> livresParId = new HashMap<>();
        //Boucle dans pour put l'id du livre en clé et l'objet livre en valeur
        for (Book book: livres)  {
            livresParId.put(book.getId(), book);
            }

        //Création d'un Arraylist pour ajouter toutes les critiques filtrées
        List<Review> critiquesFiltres = new ArrayList<>();
        //Boucle sur toutes les critiques qu'on a récupérés
        for (Review critique : critiques) {
            //Recherche de livre qui correspond à la critique en utilisant l'id du livre (qui est dans la critique)
            Book livreAssocie = livresParId.get(critique.getId());
            if (livreAssocie != null && livreAssocie.getAuthor().equalsIgnoreCase(nomAuteur)) {
                //ajoute de la critique à la liste de critiques filtrées
                critiquesFiltres.add(critique);
            }
        }

        //Maintenant qu'on a toutes les critiques du bon auteur, on les trie !
        //On utilise un 'Comparator' pour comparer deux critiques :
        //on compare la date de la deuxième critique (rev2) avec celle de la première (rev1).
        //Ça nous donne un tri décroissant, du plus récent au plus ancien.
        critiquesFiltres.sort((rev1, rev2) -> rev2.getDate().compareTo(rev1.getDate()));

        if (critiquesFiltres.isEmpty()) {
            System.out.println("\n--- Aucune critique trouvée pour l'auteur : " + nomAuteur + " ---");
        } else {
            System.out.println("\n--- Critiques de l'auteur " + nomAuteur + " (triées par date décroissante) ---");
            for (Review critique : critiquesFiltres) {
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

    }



    public void rechercherMeilleursLivresParGenre() {
        Map<Integer, Double> tableauIdNotesMoyennes = calculerLaMoyenneDesNotes();
        Map<Genre, Book> meilleursLivresParGenre = new HashMap<>();
        Map<Genre, Double> meilleuresMoyennesParGenre = new HashMap<>();


        for (Book livreActuel : this.books) {

            Genre genreDuLivre = livreActuel.getGenre();
            double moyenneDuLivreActuel = tableauIdNotesMoyennes.getOrDefault(livreActuel.getId(), 0.0);
            double meilleureMoyenneConnuePourCeGenre = meilleuresMoyennesParGenre.getOrDefault(genreDuLivre, 0.0);

            if (moyenneDuLivreActuel > meilleureMoyenneConnuePourCeGenre) {
                meilleursLivresParGenre.put(genreDuLivre, livreActuel);
                meilleuresMoyennesParGenre.put(genreDuLivre, moyenneDuLivreActuel);
            }
        }

        System.out.println("\n--- Meilleurs Livres par Genre (basé sur la meilleure moyenne) ---");

        for (Map.Entry<Genre, Book> entry : meilleursLivresParGenre.entrySet()) {
            Genre genre = entry.getKey();
            Book meilleurLivre = entry.getValue();
            double moyenneAffichee = meilleuresMoyennesParGenre.get(genre);

            System.out.println("\n--- Genre : " + genre + " ---");
            if (meilleurLivre != null && moyenneAffichee > 0.0) {
                System.out.println("  Titre: " + meilleurLivre.getTitle());
                System.out.println("  Auteur: " + meilleurLivre.getAuthor());
                System.out.println("  Moyenne des notes: " + String.format("%.2f", moyenneAffichee));
            } else {
                System.out.println("  Aucun livre avec des critiques significatives trouvé pour ce genre.");
            }
        }
        System.out.println("------------------------------------------------------------------");
    }

}
