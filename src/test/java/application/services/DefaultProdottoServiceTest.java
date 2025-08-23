package application.services;

import com.filiera.agricola.application.dto.CreazioneProdottoDTO;
import com.filiera.agricola.application.services.DefaultProdottoService;
import com.filiera.agricola.domain.DefaultAffiliazione;
import com.filiera.agricola.domain.DefaultAzienda;
import com.filiera.agricola.domain.DefaultCoordinate;
import com.filiera.agricola.domain.DefaultUtente;
import com.filiera.agricola.model.enums.CategoriaProdotto;
import com.filiera.agricola.model.enums.RuoloAziendale;
import com.filiera.agricola.model.enums.TipoProdotto;
import com.filiera.agricola.model.enums.UnitaDiMisura;
import com.filiera.agricola.repository.DefaultAziendaRepository;
import com.filiera.agricola.repository.DefaultProdottoRepository;
import com.filiera.agricola.repository.DefaultUtenteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultProdottoServiceTest {

    private DefaultProdottoService prodottoService;
    private DefaultProdottoRepository prodottoRepository;

    private DefaultUtente utenteConPermessi;
    private DefaultUtente utenteSenzaPermessi;
    private DefaultAzienda azienda;

    @BeforeEach
    void setUp() {
        prodottoRepository = new DefaultProdottoRepository();
        DefaultUtenteRepository utenteRepository = new DefaultUtenteRepository();
        DefaultAziendaRepository aziendaRepository = new DefaultAziendaRepository();

        prodottoService = new DefaultProdottoService(prodottoRepository, utenteRepository, aziendaRepository);

        azienda = new DefaultAzienda("Azienda Test", "111", "Via Test", "az@test.it", "123", "test.it", new DefaultCoordinate(1f, 1f));
        aziendaRepository.save(azienda);

        utenteConPermessi = new DefaultUtente("Mario", "Rossi", "mario@test.it", "Via Roma", "333", "pass");
        DefaultAffiliazione affiliazioneCorretta = new DefaultAffiliazione(utenteConPermessi, azienda, RuoloAziendale.GESTORE_PRODOTTI);
        utenteConPermessi.addAffiliazione(affiliazioneCorretta);
        utenteRepository.save(utenteConPermessi);

        utenteSenzaPermessi = new DefaultUtente("Luigi", "Verdi", "luigi@test.it", "Via Milano", "444", "pass");
        utenteRepository.save(utenteSenzaPermessi);
    }

    @Test
    void creaNuovoProdotto_conUtenteAutorizzato_creaCorrettamenteIlProdotto() {
        var datiProdotto = new CreazioneProdottoDTO(
                "Mela", "Descrizione mela", 1.0,
                UnitaDiMisura.KG, CategoriaProdotto.ORTOFRUTTA, TipoProdotto.MATERIA_PRIMA,
                null, null, Collections.emptyList()
        );

        var nuovoProdotto = prodottoService
                .creaNuovoProdotto(
                utenteConPermessi.getId(),
                azienda.getId(),
                datiProdotto
        );

        assertNotNull(nuovoProdotto);
        assertTrue(prodottoRepository.findById(nuovoProdotto.getId()).isPresent());
    }

    @Test
    void creaNuovoProdotto_conUtenteNonAutorizzato_lanciaSecurityException() {
        var datiProdotto = new CreazioneProdottoDTO("Pera", "Descrizione pera", 1.2, UnitaDiMisura.KG, CategoriaProdotto.ORTOFRUTTA, TipoProdotto.MATERIA_PRIMA, null, null, Collections.emptyList());
        assertThrows(SecurityException.class, () -> prodottoService.creaNuovoProdotto(utenteSenzaPermessi.getId(), azienda.getId(), datiProdotto));
    }
}
