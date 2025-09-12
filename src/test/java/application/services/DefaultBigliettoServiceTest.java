package application.services;

import com.filiera.agricola.application.services.DefaultBigliettoService;
import com.filiera.agricola.domain.DefaultBiglietto;
import com.filiera.agricola.domain.DefaultCoordinate;
import com.filiera.agricola.domain.DefaultEvento;
import com.filiera.agricola.domain.DefaultUtente;
import com.filiera.agricola.model.enums.RuoloPiattaforma;
import com.filiera.agricola.repository.DefaultBigliettoRepository;
import com.filiera.agricola.repository.DefaultEventoRepository;
import com.filiera.agricola.repository.DefaultUtenteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DefaultBigliettoServiceTest {

    private DefaultBigliettoService bigliettoService;
    private DefaultBigliettoRepository bigliettoRepository;
    private DefaultUtenteRepository utenteRepository;

    private DefaultUtente acquirente;
    private DefaultEvento eventoConPostiLimitati;
    private DefaultEvento eventoConPostiIllimitati;

    @BeforeEach
    void setUp() {
        utenteRepository = new DefaultUtenteRepository();
        DefaultEventoRepository eventoRepository = new DefaultEventoRepository();
        bigliettoRepository = new DefaultBigliettoRepository();

        bigliettoService = new DefaultBigliettoService(bigliettoRepository, utenteRepository, eventoRepository);

        acquirente = new DefaultUtente("Compratore", "Serio", "compratore@test.it", "Via Acquisto", "789", "pass");
        utenteRepository.save(acquirente);

        DefaultUtente organizzatore = new DefaultUtente("Organizzatore", "Capace", "org@test.it", "Via Eventi", "101", "pass");
        organizzatore.addRuolo(RuoloPiattaforma.ANIMATORE_FILIERA);
        utenteRepository.save(organizzatore);

        eventoConPostiLimitati = new DefaultEvento("Concerto Esclusivo", "Desc", LocalDateTime.now(), LocalDateTime.now(), organizzatore, "Addr", new DefaultCoordinate(1f, 1f));
        eventoConPostiLimitati.setPostiDisponibili(1); // Solo 1 posto!
        eventoRepository.save(eventoConPostiLimitati);

        eventoConPostiIllimitati = new DefaultEvento("Festa in Piazza", "Desc", LocalDateTime.now(), LocalDateTime.now(), organizzatore, "Addr", new DefaultCoordinate(2f, 2f));
        eventoConPostiIllimitati.setPostiDisponibili(0); // 0 = posti illimitati
        eventoRepository.save(eventoConPostiIllimitati);
    }

    @Test
    void acquistaBiglietto_perEventoConDisponibilita_creaCorrettamenteIlBiglietto() {
        // Azione
        DefaultBiglietto biglietto = bigliettoService.acquistaBiglietto(acquirente.getId(), eventoConPostiIllimitati.getId());

        // Verifica
        assertNotNull(biglietto);
        assertEquals(acquirente.getId(), biglietto.getIntestatario().getId());
        assertEquals(eventoConPostiIllimitati.getId(), biglietto.getEvento().getId());
        assertTrue(bigliettoRepository.findById(biglietto.getId()).isPresent());
    }

    @Test
    void acquistaBiglietto_perEventoAlCompleto_lanciaRuntimeException() {
        // Setup: occupo l'unico posto disponibile per l'evento limitato
        bigliettoService.acquistaBiglietto(acquirente.getId(), eventoConPostiLimitati.getId());

        // Azione e Verifica
        DefaultUtente altroAcquirente = new DefaultUtente("Altro", "Compratore", "altro@test.it", "Addr", "111", "pass");
        utenteRepository.save(altroAcquirente);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            bigliettoService.acquistaBiglietto(altroAcquirente.getId(), eventoConPostiLimitati.getId());
        });

        assertEquals("L'evento è al completo. Non ci sono più posti disponibili.", exception.getMessage());
    }
}
