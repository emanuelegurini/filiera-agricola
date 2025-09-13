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
import com.filiera.agricola.utils.ScorteInsufficientiException;
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
    private DefaultAziendaRepository aziendaRepository; // Reso accessibile a livello di classe

    // Entità di test
    private DefaultUtente utenteConPermessi;
    private DefaultUtente utenteSenzaPermessi;
    private DefaultAzienda azienda;
    private DefaultProdotto prodottoPerTest;

    @BeforeEach
    void setUp() {
        pacchettoRepository = new DefaultPacchettoProdottiRepository();
        DefaultProdottoRepository prodottoRepository = new DefaultProdottoRepository();
        DefaultUtenteRepository utenteRepository = new DefaultUtenteRepository();
        aziendaRepository = new DefaultAziendaRepository(); // Inizializzato qui

        pacchettoService = new DefaultPacchettoProdottiService(
                pacchettoRepository, prodottoRepository, utenteRepository, aziendaRepository
        );

        azienda = new DefaultAzienda("Azienda Pacchetti", "222", "Via Pacchetti", "pack@test.it", "789", "pack.it", new DefaultCoordinate(2f, 2f));

        utenteConPermessi = new DefaultUtente("Paolo", "Bianchi", "paolo@test.it", "Via Test", "111", "pass");
        utenteConPermessi.addAffiliazione(new DefaultAffiliazione(utenteConPermessi, azienda, RuoloAziendale.GESTORE_PRODOTTI));
        utenteRepository.save(utenteConPermessi);

        utenteSenzaPermessi = new DefaultUtente("Luca", "Neri", "luca@test.it", "Via Altra", "222", "pass");
        utenteRepository.save(utenteSenzaPermessi);

        prodottoPerTest = new DefaultProdotto("Olio EVO", "Olio extra vergine", 8.0, UnitaDiMisura.LT, azienda, TipoProdotto.TRASFORMATO, CategoriaProdotto.ORTOFRUTTA);
        prodottoRepository.save(prodottoPerTest);

        // --- SETUP CHIAVE: Aggiunta delle scorte iniziali al magazzino dell'azienda ---
        azienda.aggiungiScorte(prodottoPerTest, 100); // L'azienda parte con 100 unità di Olio
        aziendaRepository.save(azienda);
    }

    @Test
    void creaNuovoPacchetto_conUtenteAutorizzato_creaCorrettamenteIlPacchetto() {
        var pacchettoCreato = pacchettoService.creaNuovoPacchetto(
                utenteConPermessi.getId(), azienda.getId(), "Cesto Degustazione", "Un cesto con i nostri migliori prodotti."
        );

        assertNotNull(pacchettoCreato);
        assertEquals("Cesto Degustazione", pacchettoCreato.getNomeArticolo());
        assertTrue(pacchettoRepository.findById(pacchettoCreato.getId()).isPresent());
    }

    @Test
    void creaNuovoPacchetto_conUtenteNonAutorizzato_lanciaSecurityException() {
        assertThrows(SecurityException.class, () ->
                pacchettoService.creaNuovoPacchetto(
                        utenteSenzaPermessi.getId(), azienda.getId(), "Nome Pacchetto", "Descrizione"
                )
        );
    }

    // --- TEST MODIFICATO: Ora include la quantità e verifica il magazzino ---
    @Test
    void aggiungiProdottoAlPacchetto_conScorteSufficienti_aggiungeProdottoEScalaMagazzino() {
        var pacchetto = pacchettoService.creaNuovoPacchetto(utenteConPermessi.getId(), azienda.getId(), "Test", "Test");
        int quantitaDaAggiungere = 10;

        var pacchettoAggiornato = pacchettoService.aggiungiProdottoAlPacchetto(
                utenteConPermessi.getId(), pacchetto.getId(), prodottoPerTest.getId(), quantitaDaAggiungere
        );

        // Verifica pacchetto
        assertNotNull(pacchettoAggiornato);
        assertEquals(1, pacchettoAggiornato.getProdottiInclusi().size());
        assertTrue(pacchettoAggiornato.getProdottiInclusi().containsKey(prodottoPerTest));
        assertEquals(quantitaDaAggiungere, pacchettoAggiornato.getProdottiInclusi().get(prodottoPerTest));

        // Verifica magazzino: le scorte devono essere diminuite
        DefaultAzienda aziendaAggiornata = aziendaRepository.findById(azienda.getId()).orElseThrow();
        assertEquals(90, aziendaAggiornata.getDisponibilita(prodottoPerTest)); // 100 iniziali - 10
    }

    // --- NUOVO TEST: Verifica il caso di scorte insufficienti ---
    @Test
    void aggiungiProdottoAlPacchetto_conScorteInsufficienti_lanciaScorteInsufficientiException() {
        var pacchetto = pacchettoService.creaNuovoPacchetto(utenteConPermessi.getId(), azienda.getId(), "Test", "Test");
        int quantitaEccessiva = 101; // Più dei 100 disponibili

        assertThrows(ScorteInsufficientiException.class, () ->
                pacchettoService.aggiungiProdottoAlPacchetto(
                        utenteConPermessi.getId(), pacchetto.getId(), prodottoPerTest.getId(), quantitaEccessiva
                )
        );

        // Verifica magazzino: le scorte non devono essere state modificate
        DefaultAzienda aziendaNonModificata = aziendaRepository.findById(azienda.getId()).orElseThrow();
        assertEquals(100, aziendaNonModificata.getDisponibilita(prodottoPerTest));
    }


    @Test
    void aggiungiProdottoAlPacchetto_conProdottoNonEsistente_lanciaNoSuchElementException() {
        var pacchetto = pacchettoService.creaNuovoPacchetto(utenteConPermessi.getId(), azienda.getId(), "Test", "Test");
        UUID idProdottoInesistente = UUID.randomUUID();

        assertThrows(NoSuchElementException.class, () ->
                pacchettoService.aggiungiProdottoAlPacchetto(
                        utenteConPermessi.getId(), pacchetto.getId(), idProdottoInesistente, 5 // Quantità aggiunta
                )
        );
    }

    // --- TEST MODIFICATO: Ora verifica la restituzione delle scorte al magazzino ---
    @Test
    void rimuoviProdottoDalPacchetto_conDatiValidi_rimuoveProdottoERestituisceScorte() {
        var pacchetto = pacchettoService.creaNuovoPacchetto(utenteConPermessi.getId(), azienda.getId(), "Test", "Test");
        // Aggiungiamo 15 prodotti, portando le scorte da 100 a 85
        pacchettoService.aggiungiProdottoAlPacchetto(utenteConPermessi.getId(), pacchetto.getId(), prodottoPerTest.getId(), 15);

        // Verifica pre-condizione
        assertEquals(85, aziendaRepository.findById(azienda.getId()).get().getDisponibilita(prodottoPerTest));

        // Azione: rimuoviamo il prodotto dal pacchetto
        var pacchettoAggiornato = pacchettoService.rimuoviProdottoDalPacchetto(
                utenteConPermessi.getId(), pacchetto.getId(), prodottoPerTest.getId()
        );

        // Verifica pacchetto
        assertNotNull(pacchettoAggiornato);
        assertTrue(pacchettoAggiornato.getProdottiInclusi().isEmpty());

        // Verifica magazzino: le scorte devono essere tornate al valore iniziale
        DefaultAzienda aziendaAggiornata = aziendaRepository.findById(azienda.getId()).orElseThrow();
        assertEquals(100, aziendaAggiornata.getDisponibilita(prodottoPerTest)); // 85 + 15 restituiti
    }
}
