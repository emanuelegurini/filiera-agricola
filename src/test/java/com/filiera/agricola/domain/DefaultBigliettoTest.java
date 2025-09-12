package com.filiera.agricola.domain;

import com.filiera.agricola.model.enums.StatoBiglietto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultBigliettoTest {
    private DefaultUtente utente;
    private DefaultEvento evento;

    @BeforeEach
    public void setUp() {
        utente = new DefaultUtente("Mario", "Rossi", "mario.rossi@test.it", "Via Roma 1", "123", "pass");
        DefaultUtente organizzatore = new DefaultUtente("Luca", "Verdi", "luca.verdi@test.it", "Via Milano 2", "456", "pass");

        evento = new DefaultEvento(
                "Sagra del Vino", "Degustazione vini locali",
                LocalDateTime.now().plusDays(10),
                LocalDateTime.now().plusDays(11),
                organizzatore, "Piazza del Popolo",
                new DefaultCoordinate(43f, 13f)
        );

        evento.setCostoPartecipazione(25.50);
    }

    @Test
    void costruttore_creaBigliettoConStatoValidoEPrezzoCorretto() {
        DefaultBiglietto biglietto = new DefaultBiglietto(evento, utente);
        assertNotNull(biglietto.getId());
        assertEquals(evento, biglietto.getEvento());
        assertEquals(utente, biglietto.getIntestatario());
        assertEquals(StatoBiglietto.VALIDO, biglietto.getStato());
        assertEquals(25.50, biglietto.getPrezzoPagato());
        assertTrue(biglietto.getDataEmissione().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void setStato_aggiornaCorrettamenteLoStatoDelBiglietto() {
        DefaultBiglietto biglietto = new DefaultBiglietto(evento, utente);
        assertEquals(StatoBiglietto.VALIDO, biglietto.getStato());
        biglietto.setStato(StatoBiglietto.ANNULLATO);
        assertEquals(StatoBiglietto.ANNULLATO, biglietto.getStato());
    }

}
