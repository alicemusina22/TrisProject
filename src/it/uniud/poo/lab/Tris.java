package it.uniud.poo.lab;

public class Tris {

    private final char[][] griglia;
    private char giocatoreCorrente;
    private char vincitore;
    private int mosseFatte;

    /**
     * Costruttore: inizializza una nuova partita.
     */
    public Tris() {
        griglia = new char[3][3];
        giocatoreCorrente = 'X'; // X inizia sempre
        vincitore = ' '; // ' ' = partita in corso, 'X' = vince X, 'O' = vince O, 'P' = Pareggio
        mosseFatte = 0;
        inizializzaGriglia();
    }

    /**
     * Resetta la griglia e lo stato del gioco per una nuova partita.
     */
    public void reset() {
        giocatoreCorrente = 'X';
        vincitore = ' ';
        mosseFatte = 0;
        inizializzaGriglia();
    }

    /**
     * Riempie la griglia con caratteri vuoti (' ').
     */
    private void inizializzaGriglia() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                griglia[i][j] = ' ';
            }
        }
    }

    /**
     * Tenta di effettuare una mossa nella cella specificata.
     *
     * @param riga    La riga (0-2)
     * @param colonna La colonna (0-2)
     * @return true se la mossa è valida e stata eseguita, false altrimenti.
     */
    public boolean faiMossa(int riga, int colonna) {
        // Controlla se la partita è già finita o se la mossa non è valida
        if (vincitore != ' ' || riga < 0 || riga >= 3 || colonna < 0 || colonna >= 3 || griglia[riga][colonna] != ' ') {
            return false;
        }

        // Esegui la mossa
        griglia[riga][colonna] = giocatoreCorrente;
        mosseFatte++;

        // Controlla lo stato del gioco dopo la mossa
        if (controllaVittoria(giocatoreCorrente)) {
            vincitore = giocatoreCorrente;
        } else if (mosseFatte == 9) {
            vincitore = 'P'; // P per Pareggio
        } else {
            // Passa il turno all'altro giocatore
            giocatoreCorrente = (giocatoreCorrente == 'X') ? 'O' : 'X';
        }
        
        return true;
    }

    /**
     * Controlla se il giocatore specificato ha vinto.
     *
     * @param giocatore 'X' o 'O'
     * @return true se il giocatore ha vinto, false altrimenti.
     */
    private boolean controllaVittoria(char giocatore) {
        // Controllo righe
        for (int i = 0; i < 3; i++) {
            if (griglia[i][0] == giocatore && griglia[i][1] == giocatore && griglia[i][2] == giocatore) {
                return true;
            }
        }
        // Controllo colonne
        for (int j = 0; j < 3; j++) {
            if (griglia[0][j] == giocatore && griglia[1][j] == giocatore && griglia[2][j] == giocatore) {
                return true;
            }
        }
        // Controllo diagonali
        if (griglia[0][0] == giocatore && griglia[1][1] == giocatore && griglia[2][2] == giocatore) {
            return true;
        }
        if (griglia[0][2] == giocatore && griglia[1][1] == giocatore && griglia[2][0] == giocatore) {
            return true;
        }
        return false;
    }

    /**
     * Restituisce il giocatore corrente.
     * @return 'X' o 'O'
     */
    public char getGiocatoreCorrente() {
        return giocatoreCorrente;
    }

    /**
     * Restituisce il vincitore.
     * @return ' ' (in corso), 'X', 'O', o 'P' (pareggio).
     */
    public char getVincitore() {
        return vincitore;
    }

    /**
     * Restituisce lo stato di una cella specifica.
     * @param riga La riga (0-2)
     * @param colonna La colonna (0-2)
     * @return Il carattere nella cella (' ', 'X', 'O')
     */
    public char getCella(int riga, int colonna) {
        return griglia[riga][colonna];
    }

    /**
     * (Opzionale) Stampa la griglia sulla console per il debug.
     */
    public void stampaGriglia() {
        System.out.println("-------");
        for (int i = 0; i < 3; i++) {
            System.out.print("|");
            for (int j = 0; j < 3; j++) {
                System.out.print(griglia[i][j] + "|");
            }
            System.out.println("\n-------");
        }
    }
}
