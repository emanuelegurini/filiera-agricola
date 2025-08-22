package repository;

import com.filiera.agricola.domain.DefaultUtente;
import com.filiera.agricola.repository.DefaultUtenteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultUtenteRepositoryTest {

    private DefaultUtenteRepository utenteRepository;
    private DefaultUtente utente;

    @BeforeEach
    void setUp() {
        // Ogni test partirà con un repository pulito e un utente di test
        utenteRepository = new DefaultUtenteRepository();
        utente = new DefaultUtente(
                "Luigi",
                "Verdi",
                "luigi.verdi@test.it",
                "Via Milano 5",
                "3387654321",
                "PasswordForte#1"
        );
    }

    @Test
    void save_e_findById_salvaERecuperaCorrettamenteUtente() {
        utenteRepository.save(utente);

        Optional<DefaultUtente> utenteTrovato = utenteRepository.findById(utente.getId());

        assertTrue(utenteTrovato.isPresent(), "L'utente dovrebbe essere trovato nel repository.");
        assertEquals(utente.getId(), utenteTrovato.get().getId(), "L'ID dell'utente trovato deve corrispondere.");
        assertEquals("luigi.verdi@test.it", utenteTrovato.get().getEmail());
    }

    @Test
    void findById_conIdNonEsistente_restituisceOptionalVuoto() {
        utenteRepository.save(utente);
        Optional<DefaultUtente> utenteTrovato = utenteRepository.findById(java.util.UUID.randomUUID());

        assertFalse(utenteTrovato.isPresent(), "Non dovrebbe essere trovato nessun utente.");
    }

    @Test
    void findByEmail_trovaCorrettamenteUtenteEsistente() {
        utenteRepository.save(utente);

        Optional<DefaultUtente> utenteTrovato = utenteRepository.findByEmail("luigi.verdi@test.it");

        assertTrue(utenteTrovato.isPresent());
        assertEquals(utente.getId(), utenteTrovato.get().getId());
    }

    @Test
    void findByEmail_conEmailMaiuscolaMinuscola_trovaComunqueUtente() {
        utenteRepository.save(utente);

        Optional<DefaultUtente> utenteTrovato = utenteRepository.findByEmail("Luigi.Verdi@TEST.it");

        assertTrue(utenteTrovato.isPresent());
        assertEquals(utente.getId(), utenteTrovato.get().getId());
    }
}
