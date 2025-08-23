package repository;

import com.filiera.agricola.domain.DefaultAzienda;
import com.filiera.agricola.domain.DefaultCoordinate;
import com.filiera.agricola.domain.DefaultProdotto;
import com.filiera.agricola.model.enums.CategoriaProdotto;
import com.filiera.agricola.model.enums.TipoProdotto;
import com.filiera.agricola.model.enums.UnitaDiMisura;
import com.filiera.agricola.repository.DefaultProdottoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

public class DefaultProdottoRepositoryTest {

    private DefaultProdottoRepository prodottoRepository;
    private DefaultProdotto prodotto1;
    private DefaultProdotto prodotto2;

    @BeforeEach
    void setUp() {
        prodottoRepository = new DefaultProdottoRepository();
        DefaultAzienda azienda = new DefaultAzienda(
                "Azienda Prova",
                "111",
                "Via Prova",
                "prova@prova.it",
                "111",
                "prova.it",
                new DefaultCoordinate(1f, 1f)
        );

        prodotto1 = new DefaultProdotto(
                "Mela Golden",
                "Mela succosa",
                1.5,
                UnitaDiMisura.KG,
                azienda,
                TipoProdotto.MATERIA_PRIMA,
                CategoriaProdotto.ORTOFRUTTA
        );

        prodotto2 = new DefaultProdotto(
                "Formaggio Stagionato",
                "Formaggio di mucca",
                12.0,
                UnitaDiMisura.ETTO,
                azienda,
                TipoProdotto.TRASFORMATO,
                CategoriaProdotto.CASEARI
        );
    }

    @Test
    void save_e_findById_salvaERecuperaCorrettamenteProdotto() {
        prodottoRepository.save(prodotto1);
        Optional<DefaultProdotto> prodottoTrovato = prodottoRepository.findById(prodotto1.getId());

        assertTrue(prodottoTrovato.isPresent());
        assertEquals(prodotto1.getId(), prodottoTrovato.get().getId());
    }

    @Test
    void findAll_restituisceTuttiIProdottiSalvati() {
        assertTrue(prodottoRepository.findAll().isEmpty(), "Il repository dovrebbe essere vuoto all'inizio.");

        prodottoRepository.save(prodotto1);
        prodottoRepository.save(prodotto2);

        List<DefaultProdotto> tuttiIProdotti = prodottoRepository.findAll();

        assertEquals(2, tuttiIProdotti.size());
        assertTrue(tuttiIProdotti.stream().anyMatch(p -> p.getId().equals(prodotto1.getId())));
        assertTrue(tuttiIProdotti.stream().anyMatch(p -> p.getId().equals(prodotto2.getId())));
    }

}
