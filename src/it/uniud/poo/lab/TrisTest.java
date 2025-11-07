package it.uniud.poo.lab;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TrisTest {

    private Tris partita;

    // Questo metodo viene eseguito prima di ogni test
    @BeforeEach
    void setUp() {
        partita = new Tris();
    }

    @Test
    void testNuovaPartita() {
        assertEquals('X', partita.getGiocatoreCorrente(), "Il giocatore iniziale deve essere X");
        assertEquals(' ', partita.getVincitore(), "Non ci deve essere nessun vincitore all'inizio");
        assertEquals(' ', partita.getCella(0, 0), "La cella (0,0) deve essere vuota");
    }

    @Test
    void testMossaValida() {
        assertTrue(partita.faiMossa(0, 0), "La mossa (0,0) dovrebbe essere valida");
        assertEquals('X', partita.getCella(0, 0), "La cella (0,0) dovrebbe contenere X");
        assertEquals('O', partita.getGiocatoreCorrente(), "Il giocatore corrente dovrebbe passare a O");
    }

    @Test
    void testMossaNonValidaCellaOccupata() {
        partita.faiMossa(0, 0); // X gioca in (0,0)
        assertFalse(partita.faiMossa(0, 0), "La mossa (0,0) dovrebbe essere non valida perché occupata");
        assertEquals('O', partita.getGiocatoreCorrente(), "Il giocatore corrente dovrebbe rimanere O");
    }

    @Test
    void testMossaNonValidaFuoriLimiti() {
        assertFalse(partita.faiMossa(-1, 0), "Mossa fuori limiti (-1,0) non valida");
        assertFalse(partita.faiMossa(3, 3), "Mossa fuori limiti (3,3) non valida");
        assertEquals('X', partita.getGiocatoreCorrente(), "Il giocatore corrente dovrebbe rimanere X");
    }

    @Test
    void testVittoriaOrizzontaleX() {
        partita.faiMossa(0, 0); // X
        partita.faiMossa(1, 0); // O
        partita.faiMossa(0, 1); // X
        partita.faiMossa(1, 1); // O
        partita.faiMossa(0, 2); // X vince
        
        assertEquals('X', partita.getVincitore(), "X dovrebbe vincere sulla riga 0");
        // Testa che il gioco si fermi
        assertFalse(partita.faiMossa(2, 2), "Nessuna mossa dovrebbe essere permessa dopo la vittoria");
    }

    @Test
    void testVittoriaVerticaleO() {
        partita.faiMossa(0, 0); // X
        partita.faiMossa(0, 1); // O
        partita.faiMossa(1, 0); // X
        partita.faiMossa(1, 1); // O
        partita.faiMossa(0, 2); // X
        partita.faiMossa(2, 1); // O vince
        
        assertEquals('O', partita.getVincitore(), "O dovrebbe vincere sulla colonna 1");
    }

    @Test
    void testVittoriaDiagonale() {
        partita.faiMossa(0, 0); // X
        partita.faiMossa(0, 1); // O
        partita.faiMossa(1, 1); // X
        partita.faiMossa(0, 2); // O
        partita.faiMossa(2, 2); // X vince
        
        assertEquals('X', partita.getVincitore(), "X dovrebbe vincere sulla diagonale principale");
    }

    @Test
    void testPareggio() {
        partita.faiMossa(0, 0); // X
        partita.faiMossa(1, 1); // O
        partita.faiMossa(0, 1); // X
        partita.faiMossa(0, 2); // O
        partita.faiMossa(2, 0); // X
        partita.faiMossa(1, 0); // O
        partita.faiMossa(1, 2); // X
        partita.faiMossa(2, 1); // O
        partita.faiMossa(2, 2); // X (Mossa finale, pareggio)
        
        assertEquals('P', partita.getVincitore(), "La partita dovrebbe finire in pareggio");
    }
    
    @Test
    void testReset() {
        partita.faiMossa(0, 0); // X
        partita.faiMossa(1, 0); // O
        partita.reset();
        
        assertEquals('X', partita.getGiocatoreCorrente(), "Dopo il reset, il giocatore deve essere X");
        assertEquals(' ', partita.getVincitore(), "Dopo il reset, non ci deve essere un vincitore");
        assertEquals(' ', partita.getCella(0, 0), "Dopo il reset, la cella (0,0) deve essere vuota");
    }
}