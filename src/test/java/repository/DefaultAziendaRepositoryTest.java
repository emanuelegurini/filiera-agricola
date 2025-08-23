package repository;

import com.filiera.agricola.domain.DefaultAzienda;
import com.filiera.agricola.domain.DefaultCoordinate;
import com.filiera.agricola.repository.DefaultAziendaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultAziendaRepositoryTest {

    private DefaultAziendaRepository aziendaRepository;
    private DefaultAzienda azienda;

    @BeforeEach
    void setUp() {
        aziendaRepository = new DefaultAziendaRepository();
        azienda = new DefaultAzienda(
                "Bio Orto Felice",
                "11223344556",
                "Contrada Campagna 123",
                "info@bioorto.it",
                "0721123456",
                "www.bioorto.it",
                new DefaultCoordinate(43.0f, 13.0f)
        );
    }

    @Test
    void save_e_findById_salvaERecuperaCorrettamenteAzienda() {
        aziendaRepository.save(azienda);
        Optional<DefaultAzienda> aziendaTrovata = aziendaRepository.findById(azienda.getId());

        assertTrue(aziendaTrovata.isPresent());
        assertEquals(azienda.getId(), aziendaTrovata.get().getId());
        assertEquals("Bio Orto Felice", aziendaTrovata.get().getRagioneSociale());
    }

    @Test
    void findByPartitaIva_trovaCorrettamenteAziendaEsistente() {
        aziendaRepository.save(azienda);

        Optional<DefaultAzienda> aziendaTrovata = aziendaRepository.findByPartitaIva("11223344556");

        assertTrue(aziendaTrovata.isPresent());
        assertEquals(azienda.getId(), aziendaTrovata.get().getId());
    }

    @Test
    void findByPartitaIva_conPartitaIvaNonEsistente_restituisceOptionalVuoto() {
        aziendaRepository.save(azienda);

        Optional<DefaultAzienda> aziendaTrovata = aziendaRepository.findByPartitaIva("00000000000");

        assertFalse(aziendaTrovata.isPresent());
    }
}
