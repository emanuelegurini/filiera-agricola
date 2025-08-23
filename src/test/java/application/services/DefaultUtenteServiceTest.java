package application.services;

import com.filiera.agricola.application.dto.RegistrazioneUtenteDTO;
import com.filiera.agricola.application.services.DefaultUtenteService;
import com.filiera.agricola.domain.DefaultAffiliazione;
import com.filiera.agricola.domain.DefaultAzienda;
import com.filiera.agricola.domain.DefaultCoordinate;
import com.filiera.agricola.domain.DefaultUtente;
import com.filiera.agricola.model.enums.RuoloAziendale;
import com.filiera.agricola.repository.DefaultAziendaRepository;
import com.filiera.agricola.repository.DefaultUtenteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultUtenteServiceTest {

    private DefaultUtenteService utenteService;
    private DefaultUtenteRepository utenteRepository;
    private DefaultAziendaRepository aziendaRepository;

    private DefaultUtente utente;
    private DefaultAzienda azienda;

    @BeforeEach
    void setUp() {
        utenteRepository = new DefaultUtenteRepository();
        aziendaRepository = new DefaultAziendaRepository();

        utenteService = new DefaultUtenteService(utenteRepository, aziendaRepository);

        utente = new DefaultUtente("Test", "User", "test@user.it", "Addr", "123", "pass");
        utenteRepository.save(utente);

        azienda = new DefaultAzienda("Test Azienda", "12345", "Addr", "test@az.it", "456", "www.test.it", new DefaultCoordinate(1f, 1f));
        aziendaRepository.save(azienda);
    }

    @Test
    void registraNuovoUtente_conDatiValidi_salvaUtenteNelRepository() {

        assertNotNull(utente);
        assertTrue(utenteRepository.findById(utente.getId()).isPresent());
        assertEquals("test@user.it", utenteRepository.findById(utente.getId()).get().getEmail());
    }

    @Test
    void registraNuovoUtente_conEmailDuplicata_lanciaIllegalArgumentException() {

        var datiUtente2 = new RegistrazioneUtenteDTO("Test", "User", "test@user.it", "Addr", "123", "pass");

        assertThrows(IllegalArgumentException.class, () -> {
            utenteService.registraNuovoUtente(datiUtente2);
        });
    }

    @Test
    void aggiungiAffiliazione_conDatiValidi_aggiungeAffiliazioneCorrettamente() {
        utenteService.aggiungiAffiliazione(utente.getId(), azienda.getId(), RuoloAziendale.ADMIN);

        DefaultUtente utenteAggiornato = utenteRepository.findById(utente.getId())
                .orElseThrow(() -> new AssertionError("L'utente salvato non è stato trovato nel repository."));

        assertEquals(1, utenteAggiornato.getAffiliazioni().size());

        DefaultAffiliazione affiliazione = utenteAggiornato.getAffiliazioni().iterator().next();
        assertEquals(azienda.getId(), affiliazione.getAzienda().getId());
        assertEquals(RuoloAziendale.ADMIN, affiliazione.getRuoloAziendale());
    }

    @Test
    void aggiornaRuoloAffiliazione_conDatiValidi_aggiornaCorrettamenteIlRuolo() {
        utenteService.aggiungiAffiliazione(utente.getId(), azienda.getId(), RuoloAziendale.VISUALIZZATORE);

        utenteService.aggiornaRuoloAffiliazione(utente.getId(), azienda.getId(), RuoloAziendale.ADMIN);

        DefaultUtente utenteDopoUpdate = utenteRepository.findById(utente.getId())
                .orElseThrow(() -> new AssertionError("L'utente salvato non è stato trovato nel repository."));
        assertEquals(1, utenteDopoUpdate.getAffiliazioni().size());
        assertEquals(RuoloAziendale.ADMIN, utenteDopoUpdate.getAffiliazioni().iterator().next().getRuoloAziendale());
    }

    @Test
    void aggiornaRuoloAffiliazione_conAziendaNonEsistente_lanciaRuntimeException() {
        utenteService.aggiungiAffiliazione(utente.getId(), azienda.getId(), RuoloAziendale.VISUALIZZATORE);

        UUID idAziendaInesistente = UUID.randomUUID();

        Exception exception = assertThrows(RuntimeException.class, () -> {
            utenteService.aggiornaRuoloAffiliazione(utente.getId(), idAziendaInesistente, RuoloAziendale.ADMIN);
        });

        assertEquals("Azienda non trovata con ID: " + idAziendaInesistente, exception.getMessage());
    }

    @Test
    void aggiornaRuoloAffiliazione_conUtenteSenzaAffiliazione_lanciaRuntimeException() {
        DefaultAzienda altraAzienda = new DefaultAzienda("Altra Azienda", "999", "Altro Indirizzo", "altra@az.it", "999", "www.altra.it", new DefaultCoordinate(3f, 3f));
        aziendaRepository.save(altraAzienda);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            utenteService.aggiornaRuoloAffiliazione(utente.getId(), altraAzienda.getId(), RuoloAziendale.ADMIN);
        });

        assertEquals("L'utente non ha un'affiliazione con l'azienda specificata.", exception.getMessage());
    }

}
