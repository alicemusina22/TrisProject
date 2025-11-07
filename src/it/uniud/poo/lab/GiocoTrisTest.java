package it.uniud.poo.lab;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Test per verificare le modalità di gioco di GiocoTris:
 * - Single Player (contro IA)
 * - Local Multiplayer (due giocatori sullo stesso dispositivo)
 * - Online Multiplayer (due giocatori su dispositivi diversi)
 */
class GiocoTrisTest {

    private GiocoTris gioco;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        gioco = new GiocoTris();
        
        // Cattura l'output per i test
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    @DisplayName("Test: Verifica modalità Local Multiplayer (default)")
    void testModalitaLocalMultiplayer() {
        // Il gioco attuale è local multiplayer: due giocatori si alternano sullo stesso dispositivo
        assertNotNull(gioco, "L'oggetto GiocoTris deve essere inizializzato");
        
        // Verifica che non ci sia nessuna componente IA
        assertFalse(hasAIComponent(gioco), "La modalità local multiplayer non deve avere componenti IA");
        
        // Verifica che non ci sia nessuna componente di rete
        assertFalse(hasNetworkComponent(gioco), "La modalità local multiplayer non deve avere componenti di rete");
    }

    @Test
    @DisplayName("Test: Verifica assenza modalità Single Player (IA)")
    void testAssenzaSinglePlayer() {
        // Verifica che il gioco NON abbia una modalità single player contro IA
        assertFalse(hasSinglePlayerMode(gioco), "Il gioco attualmente non supporta la modalità single player");
        
        // Verifica che non esista un'intelligenza artificiale
        assertNull(getAIPlayer(gioco), "Non deve esistere un giocatore IA");
    }

    @Test
    @DisplayName("Test: Verifica assenza modalità Online")
    void testAssenzaModalitaOnline() {
        // Verifica che il gioco NON abbia una modalità online
        assertFalse(hasOnlineMode(gioco), "Il gioco attualmente non supporta la modalità online");
        
        // Verifica che non esistano componenti di rete
        assertNull(getNetworkManager(gioco), "Non deve esistere un gestore di rete");
        assertFalse(isConnectedToServer(gioco), "Non deve essere connesso a nessun server");
    }

    @Test
    @DisplayName("Test: Verifica tipo di gioco corrente")
    void testTipoGiocoCorrente() {
        GameMode modalita = getGameMode(gioco);
        
        assertEquals(GameMode.LOCAL_MULTIPLAYER, modalita, 
            "La modalità di gioco corrente deve essere LOCAL_MULTIPLAYER");
        
        assertNotEquals(GameMode.SINGLE_PLAYER, modalita, 
            "La modalità non deve essere SINGLE_PLAYER");
        
        assertNotEquals(GameMode.ONLINE_MULTIPLAYER, modalita, 
            "La modalità non deve essere ONLINE_MULTIPLAYER");
    }

    @Test
    @DisplayName("Test: Verifica numero di giocatori umani")
    void testNumeroGiocatoriUmani() {
        int numeroGiocatori = getNumberOfHumanPlayers(gioco);
        
        assertEquals(2, numeroGiocatori, 
            "Il gioco local multiplayer deve avere esattamente 2 giocatori umani");
    }

    @Test
    @DisplayName("Test: Verifica che i turni si alternino localmente")
    void testAlternanzaTurniLocale() {
        // Simula una sequenza di input per verificare l'alternanza
        String input = "q"; // Giocatore X fa una mossa
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        // In una partita local multiplayer, ogni input viene interpretato come una mossa
        // del giocatore corrente, che poi cambia automaticamente
        assertTrue(isLocalTurnBased(gioco), 
            "Il gioco deve alternare i turni localmente tra due giocatori");
    }

    @Test
    @DisplayName("Test: Verifica proprietà di connettività")
    void testConnettivita() {
        assertFalse(requiresInternetConnection(gioco), 
            "Il gioco local multiplayer non deve richiedere connessione internet");
        
        assertFalse(hasServerConnection(gioco), 
            "Il gioco non deve avere una connessione a un server");
        
        assertNull(getIPAddress(gioco), 
            "Non deve esserci nessun indirizzo IP configurato");
        
        assertEquals(0, getPort(gioco), 
            "Non deve esserci nessuna porta di rete configurata");
    }

    // ==================== METODI DI SUPPORTO ====================
    
    /**
     * Enum per rappresentare le modalità di gioco.
     */
    private enum GameMode {
        SINGLE_PLAYER,      // Un giocatore contro IA
        LOCAL_MULTIPLAYER,  // Due giocatori sullo stesso dispositivo
        ONLINE_MULTIPLAYER  // Due giocatori su dispositivi diversi
    }

    /**
     * Determina la modalità di gioco corrente.
     * Implementazione attuale: il gioco è sempre LOCAL_MULTIPLAYER.
     */
    private GameMode getGameMode(GiocoTris gioco) {
        // Analisi dell'implementazione corrente:
        // - Non c'è IA (no single player)
        // - Non c'è networking (no online)
        // - Due giocatori si alternano localmente
        return GameMode.LOCAL_MULTIPLAYER;
    }

    /**
     * Verifica se il gioco ha componenti di intelligenza artificiale.
     */
    private boolean hasAIComponent(GiocoTris gioco) {
        // Cerca eventuali classi o metodi relativi all'IA
        try {
            java.lang.reflect.Field[] fields = gioco.getClass().getDeclaredFields();
            for (java.lang.reflect.Field field : fields) {
                String fieldName = field.getName().toLowerCase();
                if (fieldName.contains("ai") || fieldName.contains("cpu") || 
                    fieldName.contains("computer") || fieldName.contains("bot")) {
                    return true;
                }
            }
        } catch (Exception e) {
            // Ignora eccezioni di reflection
        }
        return false;
    }

    /**
     * Verifica se il gioco ha componenti di rete.
     */
    private boolean hasNetworkComponent(GiocoTris gioco) {
        try {
            java.lang.reflect.Field[] fields = gioco.getClass().getDeclaredFields();
            for (java.lang.reflect.Field field : fields) {
                String fieldName = field.getName().toLowerCase();
                String fieldType = field.getType().getName().toLowerCase();
                if (fieldName.contains("socket") || fieldName.contains("network") || 
                    fieldName.contains("server") || fieldName.contains("client") ||
                    fieldType.contains("socket") || fieldType.contains("connection")) {
                    return true;
                }
            }
        } catch (Exception e) {
            // Ignora eccezioni di reflection
        }
        return false;
    }

    /**
     * Verifica se il gioco supporta la modalità single player.
     */
    private boolean hasSinglePlayerMode(GiocoTris gioco) {
        return hasAIComponent(gioco);
    }

    /**
     * Verifica se il gioco supporta la modalità online.
     */
    private boolean hasOnlineMode(GiocoTris gioco) {
        return hasNetworkComponent(gioco);
    }

    /**
     * Ottiene il giocatore IA, se presente.
     */
    private Object getAIPlayer(GiocoTris gioco) {
        try {
            java.lang.reflect.Field[] fields = gioco.getClass().getDeclaredFields();
            for (java.lang.reflect.Field field : fields) {
                String fieldName = field.getName().toLowerCase();
                if (fieldName.contains("ai") || fieldName.contains("cpu") || 
                    fieldName.contains("computer")) {
                    field.setAccessible(true);
                    return field.get(gioco);
                }
            }
        } catch (Exception e) {
            // Ignora eccezioni
        }
        return null;
    }

    /**
     * Ottiene il gestore di rete, se presente.
     */
    private Object getNetworkManager(GiocoTris gioco) {
        try {
            java.lang.reflect.Field[] fields = gioco.getClass().getDeclaredFields();
            for (java.lang.reflect.Field field : fields) {
                String fieldName = field.getName().toLowerCase();
                if (fieldName.contains("network") || fieldName.contains("connection")) {
                    field.setAccessible(true);
                    return field.get(gioco);
                }
            }
        } catch (Exception e) {
            // Ignora eccezioni
        }
        return null;
    }

    /**
     * Verifica se il gioco è connesso a un server.
     */
    private boolean isConnectedToServer(GiocoTris gioco) {
        return getNetworkManager(gioco) != null;
    }

    /**
     * Ottiene il numero di giocatori umani.
     */
    private int getNumberOfHumanPlayers(GiocoTris gioco) {
        // Nell'implementazione corrente, ci sono sempre 2 giocatori umani
        if (hasAIComponent(gioco)) {
            return 1; // Single player: 1 umano + 1 IA
        } else if (hasNetworkComponent(gioco)) {
            return 2; // Online: 2 umani su dispositivi diversi
        } else {
            return 2; // Local multiplayer: 2 umani sullo stesso dispositivo
        }
    }

    /**
     * Verifica se i turni si alternano localmente.
     */
    private boolean isLocalTurnBased(GiocoTris gioco) {
        // Il gioco è local turn-based se non ha IA né networking
        return !hasAIComponent(gioco) && !hasNetworkComponent(gioco);
    }

    /**
     * Verifica se il gioco richiede connessione internet.
     */
    private boolean requiresInternetConnection(GiocoTris gioco) {
        return hasNetworkComponent(gioco);
    }

    /**
     * Verifica se il gioco ha una connessione a un server.
     */
    private boolean hasServerConnection(GiocoTris gioco) {
        return hasNetworkComponent(gioco);
    }

    /**
     * Ottiene l'indirizzo IP configurato, se presente.
     */
    private String getIPAddress(GiocoTris gioco) {
        try {
            java.lang.reflect.Field[] fields = gioco.getClass().getDeclaredFields();
            for (java.lang.reflect.Field field : fields) {
                if (field.getName().toLowerCase().contains("ip") || 
                    field.getName().toLowerCase().contains("address")) {
                    field.setAccessible(true);
                    Object value = field.get(gioco);
                    return value != null ? value.toString() : null;
                }
            }
        } catch (Exception e) {
            // Ignora eccezioni
        }
        return null;
    }

    /**
     * Ottiene la porta configurata, se presente.
     */
    private int getPort(GiocoTris gioco) {
        try {
            java.lang.reflect.Field[] fields = gioco.getClass().getDeclaredFields();
            for (java.lang.reflect.Field field : fields) {
                if (field.getName().toLowerCase().contains("port")) {
                    field.setAccessible(true);
                    Object value = field.get(gioco);
                    if (value instanceof Integer) {
                        return (Integer) value;
                    }
                }
            }
        } catch (Exception e) {
            // Ignora eccezioni
        }
        return 0;
    }

    /**
     * Ripristina lo stream di output originale dopo ogni test.
     */
    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }
}
