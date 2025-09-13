package application.services;


import com.filiera.agricola.application.services.DefaultPacchettoProdottiService;
import com.filiera.agricola.domain.*;
import com.filiera.agricola.model.enums.CategoriaProdotto;
import com.filiera.agricola.model.enums.RuoloAziendale;
import com.filiera.agricola.model.enums.TipoProdotto;
import com.filiera.agricola.model.enums.UnitaDiMisura;
import com.filiera.agricola.repository.DefaultAziendaRepository;
import com.filiera.agricola.repository.DefaultPacchettoProdottiRepository;
import com.filiera.agricola.repository.DefaultProdottoRepository;
import com.filiera.agricola.repository.DefaultUtenteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultPacchettoProdottiServiceTest {

    // Service da testare
    private DefaultPacchettoProdottiService pacchettoService;

    // Repository in-memory
    private DefaultPacchettoProdottiRepository pacchettoRepository;

    // Entità di test
    private DefaultUtente utenteConPermessi;
    private DefaultUtente utenteSenzaPermessi;
    private DefaultAzienda azienda;
    private DefaultProdotto prodottoPerTest;

    @BeforeEach
    void setUp() {
        // Inizializzazione dei repository
        pacchettoRepository = new DefaultPacchettoProdottiRepository();
        DefaultProdottoRepository prodottoRepository = new DefaultProdottoRepository();
        DefaultUtenteRepository utenteRepository = new DefaultUtenteRepository();
        DefaultAziendaRepository aziendaRepository = new DefaultAziendaRepository();

        // Inizializzazione del service
        pacchettoService = new DefaultPacchettoProdottiService(
                pacchettoRepository,
                prodottoRepository,
                utenteRepository,
                aziendaRepository
        );

        // Creazione dati di base per i test
        azienda = new DefaultAzienda("Azienda Pacchetti", "222", "Via Pacchetti", "pack@test.it", "789", "pack.it", new DefaultCoordinate(2f, 2f));
        aziendaRepository.save(azienda);

        utenteConPermessi = new DefaultUtente("Paolo", "Bianchi", "paolo@test.it", "Via Test", "111", "pass");
        utenteConPermessi.addAffiliazione(new DefaultAffiliazione(utenteConPermessi, azienda, RuoloAziendale.GESTORE_PRODOTTI));
        utenteRepository.save(utenteConPermessi);

        utenteSenzaPermessi = new DefaultUtente("Luca", "Neri", "luca@test.it", "Via Altra", "222", "pass");
        utenteRepository.save(utenteSenzaPermessi);

        prodottoPerTest = new DefaultProdotto("Olio EVO", "Olio extra vergine", 8.0, UnitaDiMisura.LT, azienda, TipoProdotto.TRASFORMATO, CategoriaProdotto.ORTOFRUTTA);
        prodottoRepository.save(prodottoPerTest);
    }

    @Test
    void creaNuovoPacchetto_conUtenteAutorizzato_creaCorrettamenteIlPacchetto() {
        var pacchettoCreato = pacchettoService.creaNuovoPacchetto(
                utenteConPermessi.getId(),
                azienda.getId(),
                "Cesto Degustazione",
                "Un cesto con i nostri migliori prodotti."
        );

        assertNotNull(pacchettoCreato);
        assertEquals("Cesto Degustazione", pacchettoCreato.getNomeArticolo());
        assertTrue(pacchettoRepository.findById(pacchettoCreato.getId()).isPresent());
    }

    @Test
    void creaNuovoPacchetto_conUtenteNonAutorizzato_lanciaSecurityException() {
        assertThrows(SecurityException.class, () ->
                pacchettoService.creaNuovoPacchetto(
                        utenteSenzaPermessi.getId(),
                        azienda.getId(),
                        "Nome Pacchetto",
                        "Descrizione"
                )
        );
    }

    @Test
    void aggiungiProdottoAlPacchetto_conDatiValidi_aggiungeCorrettamente() {
        var pacchetto = pacchettoService.creaNuovoPacchetto(utenteConPermessi.getId(), azienda.getId(), "Test", "Test");

        var pacchettoAggiornato = pacchettoService.aggiungiProdottoAlPacchetto(
                utenteConPermessi.getId(),
                pacchetto.getId(),
                prodottoPerTest.getId()
        );

        assertNotNull(pacchettoAggiornato);
        assertEquals(1, pacchettoAggiornato.getProdottiInclusi().size());
        assertEquals(prodottoPerTest.getId(), pacchettoAggiornato.getProdottiInclusi().get(0).getId());
    }

    @Test
    void aggiungiProdottoAlPacchetto_conProdottoNonEsistente_lanciaNoSuchElementException() {
        var pacchetto = pacchettoService.creaNuovoPacchetto(utenteConPermessi.getId(), azienda.getId(), "Test", "Test");
        UUID idProdottoInesistente = UUID.randomUUID();

        assertThrows(NoSuchElementException.class, () ->
                pacchettoService.aggiungiProdottoAlPacchetto(
                        utenteConPermessi.getId(),
                        pacchetto.getId(),
                        idProdottoInesistente
                )
        );
    }

    @Test
    void rimuoviProdottoDalPacchetto_conDatiValidi_rimuoveCorrettamente() {
        var pacchetto = pacchettoService.creaNuovoPacchetto(utenteConPermessi.getId(), azienda.getId(), "Test", "Test");
        pacchettoService.aggiungiProdottoAlPacchetto(utenteConPermessi.getId(), pacchetto.getId(), prodottoPerTest.getId());

        // Verifica pre-condizione
        assertEquals(1, pacchettoRepository.findById(pacchetto.getId()).get().getProdottiInclusi().size());

        var pacchettoAggiornato = pacchettoService.rimuoviProdottoDalPacchetto(
                utenteConPermessi.getId(),
                pacchetto.getId(),
                prodottoPerTest.getId()
        );

        assertNotNull(pacchettoAggiornato);
        assertTrue(pacchettoAggiornato.getProdottiInclusi().isEmpty());
    }
}