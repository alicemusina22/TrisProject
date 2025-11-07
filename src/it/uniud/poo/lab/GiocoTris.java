package it.uniud.poo.lab;

import java.io.IOException;

/**
 * Interfaccia a caratteri per giocare a Tris.
 * 
 * Comandi:
 * - q, w, e, a, s, d, z, x, c: posizioni della griglia (come tastierino)
 *   q w e
 *   a s d
 *   z x c
 * - SPAZIO: ricomincia la partita
 * - ESC: esci dal gioco
 */
public class GiocoTris {
    
    private Tris partita;
    
    public GiocoTris() {
        partita = new Tris();
    }
    
    /**
     * Avvia il gioco.
     */
    public void gioca() {
        System.out.println("=== BENVENUTO AL GIOCO DEL TRIS ===");
        System.out.println("\nComandi:");
        System.out.println("  q w e");
        System.out.println("  a s d    <- Usa questi tasti per scegliere la posizione");
        System.out.println("  z x c");
        System.out.println("\nSPAZIO: Ricomincia");
        System.out.println("ESC: Esci");
        System.out.println("\nPremi un tasto per iniziare...");
        
        try {
            // Attiva la modalità raw (senza buffer)
            enableRawMode();
            
            boolean continua = true;
            
            while (continua) {
                // Mostra lo stato del gioco
                mostraGriglia();
                
                // Leggi input
                int input = System.in.read();
                
                // Gestisci ESC (27)
                if (input == 27) {
                    System.out.println("\n\nGrazie per aver giocato!");
                    continua = false;
                    continue;
                }
                
                // Gestisci SPAZIO (32)
                if (input == 32 || input == ' ') {
                    partita.reset();
                    System.out.println("\n=== NUOVA PARTITA ===");
                    continue;
                }
                
                // Converti il carattere in posizione sulla griglia
                int[] pos = charToPosition((char) input);
                
                if (pos != null) {
                    boolean mossaValida = partita.faiMossa(pos[0], pos[1]);
                    
                    if (!mossaValida) {
                        System.out.println("\nMossa non valida!");
                    } else {
                        // Controlla se c'è un vincitore o pareggio
                        char vincitore = partita.getVincitore();
                        if (vincitore != ' ') {
                            mostraGriglia();
                            if (vincitore == 'P') {
                                System.out.println("\n*** PAREGGIO! ***");
                            } else {
                                System.out.println("\n*** " + vincitore + " HA VINTO! ***");
                            }
                            System.out.println("Premi SPAZIO per giocare ancora o ESC per uscire");
                        }
                    }
                }
            }
            
        } catch (IOException e) {
            System.err.println("Errore di I/O: " + e.getMessage());
        } finally {
            // Ripristina la modalità normale del terminale
            disableRawMode();
        }
    }
    
    /**
     * Mostra la griglia di gioco.
     */
    private void mostraGriglia() {
        // Pulisci lo schermo (ANSI escape code)
        System.out.print("\033[H\033[2J");
        System.out.flush();
        
        System.out.println("=== TRIS ===");
        System.out.println("\nTurno di: " + partita.getGiocatoreCorrente());
        System.out.println();
        
        // Mostra la griglia
        System.out.println("  0   1   2");
        for (int i = 0; i < 3; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < 3; j++) {
                char cella = partita.getCella(i, j);
                System.out.print(cella == ' ' ? ' ' : cella);
                if (j < 2) System.out.print(" | ");
            }
            System.out.println();
            if (i < 2) System.out.println("  -----------");
        }
        System.out.println();
        
        // Mostra la mappa dei tasti
        System.out.println("Mappa tasti:");
        System.out.println("  q w e");
        System.out.println("  a s d");
        System.out.println("  z x c");
    }
    
    /**
     * Converte un carattere in coordinate della griglia.
     * @param c Il carattere premuto
     * @return Un array [riga, colonna] o null se il carattere non è valido
     */
    private int[] charToPosition(char c) {
        switch (Character.toLowerCase(c)) {
            case 'q': return new int[]{0, 0};
            case 'w': return new int[]{0, 1};
            case 'e': return new int[]{0, 2};
            case 'a': return new int[]{1, 0};
            case 's': return new int[]{1, 1};
            case 'd': return new int[]{1, 2};
            case 'z': return new int[]{2, 0};
            case 'x': return new int[]{2, 1};
            case 'c': return new int[]{2, 2};
            default: return null;
        }
    }
    
    /**
     * Abilita la modalità raw del terminale (input senza buffer).
     */
    private void enableRawMode() {
        try {
            // Per Windows
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                // Su Windows, usa questo comando per disabilitare il line buffering
                new ProcessBuilder("cmd", "/c", "mode con: cols=80 lines=25").inheritIO().start().waitFor();
            } else {
                // Per Unix/Linux/Mac
                String[] cmd = {"/bin/sh", "-c", "stty raw -echo < /dev/tty"};
                Runtime.getRuntime().exec(cmd).waitFor();
            }
        } catch (Exception e) {
            // Ignora gli errori, usa la modalità normale
        }
    }
    
    /**
     * Disabilita la modalità raw del terminale.
     */
    private void disableRawMode() {
        try {
            if (!System.getProperty("os.name").toLowerCase().contains("win")) {
                // Per Unix/Linux/Mac
                String[] cmd = {"/bin/sh", "-c", "stty sane < /dev/tty"};
                Runtime.getRuntime().exec(cmd).waitFor();
            }
        } catch (Exception e) {
            // Ignora gli errori
        }
    }
    
    /**
     * Main method per avviare il gioco.
     */
    public static void main(String[] args) {
        GiocoTris gioco = new GiocoTris();
        gioco.gioca();
    }
}
