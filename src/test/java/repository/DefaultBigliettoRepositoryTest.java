package repository;

import com.filiera.agricola.domain.DefaultBiglietto;
import com.filiera.agricola.domain.DefaultCoordinate;
import com.filiera.agricola.domain.DefaultEvento;
import com.filiera.agricola.domain.DefaultUtente;
import com.filiera.agricola.repository.DefaultBigliettoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.AssertionsKt.assertNotNull;

class DefaultBigliettoRepositoryTest {
    private DefaultBigliettoRepository bigliettoRepository;
    private DefaultEvento evento1;
    private DefaultEvento evento2;
    private DefaultUtente utente;

    @BeforeEach
    void setUp() {
        bigliettoRepository = new DefaultBigliettoRepository();
        utente = new DefaultUtente("Acquirente", "Test", "acquirente@test.it", "Addr", "123", "pass");
        DefaultUtente organizzatore = new DefaultUtente("Org", "Test", "org@test.it", "Addr", "456", "pass");

        evento1 = new DefaultEvento("Evento A", "Desc A", LocalDateTime.now(), LocalDateTime.now(), organizzatore, "Via A", new DefaultCoordinate(1f, 1f));
        evento2 = new DefaultEvento("Evento B", "Desc B", LocalDateTime.now(), LocalDateTime.now(), organizzatore, "Via B", new DefaultCoordinate(2f, 2f));
    }

    @Test
    void save_e_findById_salvaERecuperaCorrettamenteIlBiglietto() {
        // Setup
        DefaultBiglietto biglietto = new DefaultBiglietto(evento1, utente);
        bigliettoRepository.save(biglietto);
        DefaultBiglietto bigliettoTrovato = bigliettoRepository.findById(biglietto.getId())
                .orElseThrow(() -> new AssertionError("Il biglietto con ID " + biglietto.getId() + " non è stato trovato."));
        assertEquals(biglietto.getId(), bigliettoTrovato.getId());
    }

    @Test
    void findByEventoId_restituisceSoloBigliettiPerL_eventoSpecificato() {
        DefaultBiglietto b1_e1 = new DefaultBiglietto(evento1, utente);
        DefaultBiglietto b2_e1 = new DefaultBiglietto(evento1, utente);
        DefaultBiglietto b1_e2 = new DefaultBiglietto(evento2, utente);
        bigliettoRepository.save(b1_e1);
        bigliettoRepository.save(b2_e1);
        bigliettoRepository.save(b1_e2);
        List<DefaultBiglietto> bigliettiPerEvento1 = bigliettoRepository.findByEventoId(evento1.getId());
        List<DefaultBiglietto> bigliettiPerEvento2 = bigliettoRepository.findByEventoId(evento2.getId());
        assertEquals(2, bigliettiPerEvento1.size());
        assertEquals(1, bigliettiPerEvento2.size());
        assertTrue(bigliettiPerEvento1.contains(b1_e1));
        assertTrue(bigliettiPerEvento2.contains(b1_e2));
    }
}
