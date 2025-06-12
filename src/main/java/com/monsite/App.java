package com.monsite;


import com.monsite.service.BookService;


public class App {
    public static void main(String[] args) {
        BookService book = new BookService();
        book.afficherLivresTrieParMoyenneNotes();
        book.afficherAuteurTrieParCritique("Frank Herbert");
        book.rechercherMeilleursLivresParGenre();
        book.afficherCritiquesLivresAnciens(1998);
        book.afficherStatistiquesParGenreSimplifie();
    }
}
