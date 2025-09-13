package com.filiera.agricola.domain;

import com.filiera.agricola.model.enums.CategoriaProdotto;
import com.filiera.agricola.model.enums.TipoAzienda;
import com.filiera.agricola.model.enums.TipoProdotto;
import com.filiera.agricola.model.enums.UnitaDiMisura;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DefaultAziendaTest {
    private DefaultAzienda azienda;
    // Aggiunto un prodotto di test per le operazioni di magazzino
    private DefaultProdotto prodottoDiTest;

    @BeforeEach
    void setUp() {
        azienda = new DefaultAzienda(
                "Azienda Agricola Rossi",
                "12345678901",
                "Via Garibaldi 1",
                "info@rossi.it",
                "071123456",
                "www.rossi.it",
                new DefaultCoordinate(43.5f, 13.5f)
        );

        prodottoDiTest = new DefaultProdotto(
                "Olio Extra Vergine", "Descrizione olio", 12.0,
                UnitaDiMisura.LT, azienda, TipoProdotto.TRASFORMATO,
                CategoriaProdotto.ORTOFRUTTA
        );
    }

    // --- TEST ESISTENTI (ANCORA VALIDI) ---

    @Test
    void costruttore_creaAziendaConDatiCorretti() {
        assertNotNull(azienda.getId());
        assertEquals("Azienda Agricola Rossi", azienda.getRagioneSociale());
        assertEquals("12345678901", azienda.getPartitaIva());

        assertNotNull(azienda.getTipoAzienda());
        assertTrue(azienda.getTipoAzienda().isEmpty(), "Appena creata, un'azienda non dovrebbe avere tipi specificati.");
    }

    @Test
    void aggiungiTipoAzienda_e_removeTipoAzienda_gestisconoCorrettamenteITipi() {
        assertTrue(azienda.getTipoAzienda().isEmpty());
        azienda.aggiungiTipoAzienda(TipoAzienda.PRODUTTORE);
        assertEquals(1, azienda.getTipoAzienda().size());
        assertTrue(azienda.getTipoAzienda().contains(TipoAzienda.PRODUTTORE));

        azienda.aggiungiTipoAzienda(TipoAzienda.TRASFORMATORE);
        assertEquals(2, azienda.getTipoAzienda().size());
        assertTrue(azienda.getTipoAzienda().contains(TipoAzienda.TRASFORMATORE));

        azienda.removeTipoAzienda(TipoAzienda.PRODUTTORE);
        assertEquals(1, azienda.getTipoAzienda().size());
        assertFalse(azienda.getTipoAzienda().contains(TipoAzienda.PRODUTTORE));
        assertTrue(azienda.getTipoAzienda().contains(TipoAzienda.TRASFORMATORE));
    }

    @Test
    void aggiungiTipoAzienda_conTipoDuplicato_nonAggiungeDuplicati() {
        azienda.aggiungiTipoAzienda(TipoAzienda.DISTRIBUTORE);
        azienda.aggiungiTipoAzienda(TipoAzienda.DISTRIBUTORE);
        assertEquals(1, azienda.getTipoAzienda().size());
    }


    // --- NUOVI TEST PER LA GESTIONE DEL MAGAZZINO ---

    @Test
    void costruttore_inizializzaMagazzinoVuoto() {
        assertNotNull(azienda.getMagazzino());
        assertTrue(azienda.getMagazzino().isEmpty(), "Il magazzino dovrebbe essere vuoto alla creazione dell'azienda.");
    }

    @Test
    void aggiungiScorte_conQuantitaPositiva_aggiornaCorrettamenteIlMagazzino() {
        azienda.aggiungiScorte(prodottoDiTest, 50);
        assertEquals(50, azienda.getDisponibilita(prodottoDiTest));

        // Aggiungiamo altre scorte dello stesso prodotto
        azienda.aggiungiScorte(prodottoDiTest, 25);
        assertEquals(75, azienda.getDisponibilita(prodottoDiTest));
    }

    @Test
    void aggiungiScorte_conQuantitaNegativaOZero_nonModificaIlMagazzino() {
        azienda.aggiungiScorte(prodottoDiTest, -10);
        assertTrue(azienda.getMagazzino().isEmpty());

        azienda.aggiungiScorte(prodottoDiTest, 0);
        assertTrue(azienda.getMagazzino().isEmpty());
    }

    @Test
    void rimuoviScorte_conDisponibilitaSufficiente_aggiornaLeScorteERestituisceTrue() {
        azienda.aggiungiScorte(prodottoDiTest, 100);

        boolean risultato = azienda.rimuoviScorte(prodottoDiTest, 40);

        assertTrue(risultato);
        assertEquals(60, azienda.getDisponibilita(prodottoDiTest));
    }

    @Test
    void rimuoviScorte_conDisponibilitaInsufficiente_nonModificaLeScorteERestituisceFalse() {
        azienda.aggiungiScorte(prodottoDiTest, 30);

        boolean risultato = azienda.rimuoviScorte(prodottoDiTest, 31);

        assertFalse(risultato);
        assertEquals(30, azienda.getDisponibilita(prodottoDiTest));
    }

    @Test
    void rimuoviScorte_esatte_svuotaLaScortaERestituisceTrue() {
        azienda.aggiungiScorte(prodottoDiTest, 50);

        boolean risultato = azienda.rimuoviScorte(prodottoDiTest, 50);

        assertTrue(risultato);
        assertEquals(0, azienda.getDisponibilita(prodottoDiTest));
        assertFalse(azienda.getMagazzino().containsKey(prodottoDiTest), "Il prodotto dovrebbe essere rimosso dalla mappa se la scorta è zero.");
    }

    @Test
    void getDisponibilita_perProdottoNonInMagazzino_restituisceZero() {
        assertEquals(0, azienda.getDisponibilita(prodottoDiTest));
    }
}