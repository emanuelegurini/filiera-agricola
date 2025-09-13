package application.services;

import com.filiera.agricola.application.dto.CreazioneProdottoDTO;
import com.filiera.agricola.application.services.DefaultProdottoService;
import com.filiera.agricola.domain.*;
import com.filiera.agricola.model.enums.*;
import com.filiera.agricola.repository.DefaultAziendaRepository;
import com.filiera.agricola.repository.DefaultProdottoRepository;
import com.filiera.agricola.repository.DefaultUtenteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultProdottoServiceTest {

    private DefaultProdottoService prodottoService;
    private DefaultProdottoRepository prodottoRepository;

    private DefaultUtente utenteConPermessi;
    private DefaultUtente utenteSenzaPermessi;
    private DefaultAzienda azienda;

    @BeforeEach
    void setUp() {
        // Inizializzazione dei repository in-memory per il test
        prodottoRepository = new DefaultProdottoRepository();
        DefaultUtenteRepository utenteRepository = new DefaultUtenteRepository();
        DefaultAziendaRepository aziendaRepository = new DefaultAziendaRepository();

        // Inizializzazione del service con i repository
        prodottoService = new DefaultProdottoService(prodottoRepository, utenteRepository, aziendaRepository);

        // Creazione di un'azienda di test
        azienda = new DefaultAzienda("Azienda Test", "111", "Via Test", "az@test.it", "123", "test.it", new DefaultCoordinate(1f, 1f));
        aziendaRepository.save(azienda);

        // Creazione di un utente con i permessi per gestire i prodotti
        utenteConPermessi = new DefaultUtente("Mario", "Rossi", "mario@test.it", "Via Roma", "333", "pass");
        DefaultAffiliazione affiliazioneCorretta = new DefaultAffiliazione(utenteConPermessi, azienda, RuoloAziendale.GESTORE_PRODOTTI);
        utenteConPermessi.addAffiliazione(affiliazioneCorretta);
        utenteRepository.save(utenteConPermessi);

        // Creazione di un utente senza affiliazioni o permessi
        utenteSenzaPermessi = new DefaultUtente("Luigi", "Verdi", "luigi@test.it", "Via Milano", "444", "pass");
        utenteRepository.save(utenteSenzaPermessi);
    }

    // --- TEST MIGLIORATO ---
    @Test
    void creaNuovoProdotto_conUtenteAutorizzato_creaCorrettamenteIlProdotto() {
        var datiProdotto = new CreazioneProdottoDTO(
                "Mela Golden", "Descrizione mela", 1.5,
                UnitaDiMisura.KG, CategoriaProdotto.ORTOFRUTTA, TipoProdotto.MATERIA_PRIMA,
                MetodoColtivazione.BIOLOGICO, null, Collections.emptyList()
        );

        var nuovoProdotto = prodottoService
                .creaNuovoProdotto(
                        utenteConPermessi.getId(),
                        azienda.getId(),
                        datiProdotto
                );

        // Asserzione di base: l'oggetto non è nullo
        assertNotNull(nuovoProdotto);

        // Asserzione di persistenza: l'oggetto è stato salvato nel repository
        var prodottoSalvatoOpt = prodottoRepository.findById(nuovoProdotto.getId());
        assertTrue(prodottoSalvatoOpt.isPresent());

        // Asserzioni di dettaglio: i dati sono stati mappati correttamente
        var prodottoSalvato = prodottoSalvatoOpt.get();
        assertEquals("Mela Golden", prodottoSalvato.getNomeArticolo());
        assertEquals(1.5, prodottoSalvato.getPrezzoUnitario());
        assertEquals(TipoProdotto.MATERIA_PRIMA, prodottoSalvato.getTipoProdotto());
        assertEquals(MetodoColtivazione.BIOLOGICO, prodottoSalvato.getMetodoColtivazione());
        assertTrue(prodottoSalvato.getIngredienti().isEmpty());
    }

    @Test
    void creaNuovoProdotto_conUtenteNonAutorizzato_lanciaSecurityException() {
        var datiProdotto = new CreazioneProdottoDTO("Pera", "Descrizione pera", 1.2, UnitaDiMisura.KG, CategoriaProdotto.ORTOFRUTTA, TipoProdotto.MATERIA_PRIMA, null, null, Collections.emptyList());

        assertThrows(SecurityException.class, () -> prodottoService.creaNuovoProdotto(utenteSenzaPermessi.getId(), azienda.getId(), datiProdotto));
    }

    // --- NUOVO TEST: VALIDAZIONE LOGICA ---
    @Test
    void creaNuovoProdotto_conDatiInconsistenti_lanciaIllegalArgumentException() {
        // DTO invalido: MATERIA_PRIMA ma con una lista di ingredienti
        var datiInvalidi = new CreazioneProdottoDTO(
                "Mela Strana", "Descrizione", 1.0,
                UnitaDiMisura.KG, CategoriaProdotto.ORTOFRUTTA, TipoProdotto.MATERIA_PRIMA,
                MetodoColtivazione.BIOLOGICO,
                null,
                List.of(UUID.randomUUID()) // Lista ingredienti non vuota, il che è sbagliato
        );

        // Verifichiamo che il servizio lanci un'eccezione per dati logicamente inconsistenti
        assertThrows(IllegalArgumentException.class,
                () -> prodottoService.creaNuovoProdotto(
                        utenteConPermessi.getId(),
                        azienda.getId(),
                        datiInvalidi
                )
        );
    }

    // --- NUOVO TEST: CREAZIONE PRODOTTO TRASFORMATO ---
    @Test
    void creaNuovoProdotto_trasformatoConIngredienti_creaCorrettamente() {
        // 1. Creiamo un ingrediente (materia prima) e salviamolo per avere un ID valido
        var pomodoro = new DefaultProdotto(
                "Pomodoro San Marzano", "Ingrediente base per sughi", 2.0, UnitaDiMisura.KG,
                azienda, TipoProdotto.MATERIA_PRIMA, CategoriaProdotto.ORTOFRUTTA
        );
        prodottoRepository.save(pomodoro);

        // 2. Prepariamo il DTO per il prodotto trasformato usando l'ID dell'ingrediente
        var datiPassata = new CreazioneProdottoDTO(
                "Passata di Pomodoro", "Fatta con pomodori locali", 3.5,
                UnitaDiMisura.LT, CategoriaProdotto.CONSERVE_E_MARMELLATE, TipoProdotto.TRASFORMATO,
                null, // Nessun metodo di coltivazione per un trasformato
                "Cottura lenta e invasettamento a caldo", // Metodo di trasformazione
                List.of(pomodoro.getId()) // ID dell'ingrediente creato
        );

        // 3. Eseguiamo la chiamata al servizio
        var passataCreata = prodottoService.creaNuovoProdotto(
                utenteConPermessi.getId(),
                azienda.getId(),
                datiPassata
        );

        // 4. Eseguiamo asserzioni specifiche e approfondite
        assertNotNull(passataCreata);
        assertEquals("Passata di Pomodoro", passataCreata.getNomeArticolo());
        assertEquals(TipoProdotto.TRASFORMATO, passataCreata.getTipoProdotto());
        assertEquals("Cottura lenta e invasettamento a caldo", passataCreata.getMetodoTrasformazione());

        // Verifichiamo che gli ingredienti siano stati collegati correttamente
        assertFalse(passataCreata.getIngredienti().isEmpty());
        assertEquals(1, passataCreata.getIngredienti().size());
        assertEquals(pomodoro.getId(), passataCreata.getIngredienti().getFirst().getId());
        assertEquals("Pomodoro San Marzano", passataCreata.getIngredienti().getFirst().getNomeArticolo());
    }
}
