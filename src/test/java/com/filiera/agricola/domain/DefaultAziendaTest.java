package com.filiera.agricola.domain;

import com.filiera.agricola.model.enums.TipoAzienda;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DefaultAziendaTest {
    private DefaultAzienda azienda;

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
    }

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

        assertEquals(1, azienda.getTipoAzienda().size(), "Dovrebbe esserci 1 tipo di azienda.");
        assertTrue(azienda.getTipoAzienda().contains(TipoAzienda.PRODUTTORE), "Il tipo PRODUTTORE dovrebbe essere presente.");

        azienda.aggiungiTipoAzienda(TipoAzienda.TRASFORMATORE);

        assertEquals(2, azienda.getTipoAzienda().size(), "Dovrebbero esserci 2 tipi di azienda.");
        assertTrue(azienda.getTipoAzienda().contains(TipoAzienda.TRASFORMATORE), "Il tipo TRASFORMATORE dovrebbe essere presente.");

        azienda.removeTipoAzienda(TipoAzienda.PRODUTTORE);

        assertEquals(1, azienda.getTipoAzienda().size(), "Dovrebbe rimanere 1 tipo di azienda.");
        assertFalse(azienda.getTipoAzienda().contains(TipoAzienda.PRODUTTORE), "Il tipo PRODUTTORE dovrebbe essere stato rimosso.");
        assertTrue(azienda.getTipoAzienda().contains(TipoAzienda.TRASFORMATORE), "Il tipo TRASFORMATORE dovrebbe essere ancora presente.");
    }

    @Test
    void aggiungiTipoAzienda_conTipoDuplicato_nonAggiungeDuplicati() {
        assertTrue(azienda.getTipoAzienda().isEmpty());

        azienda.aggiungiTipoAzienda(TipoAzienda.DISTRIBUTORE);
        azienda.aggiungiTipoAzienda(TipoAzienda.DISTRIBUTORE);

        assertEquals(1, azienda.getTipoAzienda().size(), "Aggiungere lo stesso tipo più volte non deve creare duplicati.");
        assertTrue(azienda.getTipoAzienda().contains(TipoAzienda.DISTRIBUTORE));
    }

}
