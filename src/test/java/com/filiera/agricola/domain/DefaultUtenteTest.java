package com.filiera.agricola.domain;
import static org.junit.jupiter.api.Assertions.*;

import com.filiera.agricola.model.enums.RuoloAziendale;
import com.filiera.agricola.model.enums.RuoloPiattaforma;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class DefaultUtenteTest {
    private DefaultUtente utente;

    @BeforeEach
    public void setUp() {
        utente = new DefaultUtente(
                "Ippo",
                "Castagno",
                "ippo.castagno@gmail.com",
                "Via della quecia",
                "3339933999",
                "abcde1100"
        );
    }

    @Test
    void setPassword_verificaCheLaPasswordVengaCorrettamenteCriptata() {
        String plainPassword = "PasswordSuperSicura!@#";

        utente.setPassword(plainPassword);

        String hashedPassword = utente.getPasswordHash();

        assertNotNull(hashedPassword);
        assertFalse(hashedPassword.isEmpty());

        assertNotEquals(plainPassword, hashedPassword);

        assertTrue(BCrypt.checkpw(plainPassword, hashedPassword));
    }

    @Test
    void addRuolo_aggiungeCorrettamenteUnNuovoRuolo() {
        assertEquals(1, utente.getRuoli().size());
        assertTrue(utente.getRuoli().contains(RuoloPiattaforma.ACQUIRENTE));

        boolean risultatoAggiunta = utente.addRuolo(RuoloPiattaforma.CURATORE);

        assertTrue(risultatoAggiunta, "Il metodo dovrebbe restituire true quando un ruolo viene aggiunto.");
        assertEquals(2, utente.getRuoli().size(), "Il numero di ruoli dovrebbe essere 2.");
        assertTrue(utente.getRuoli().contains(RuoloPiattaforma.CURATORE), "Il nuovo ruolo CURATORE dovrebbe essere presente.");
    }

    @Test
    void addAffiliazione_e_removeAffiliazione_gestisconoCorrettamenteLeAffiliazioni() {
        DefaultAzienda azienda = new DefaultAzienda("Azienda Agricola Verdi", "12345678901", "Via Verdi 10", "info@verdi.it", "071123456", "www.verdi.it", new DefaultCoordinate(43.0f, 13.0f));
        DefaultAffiliazione affiliazione = new DefaultAffiliazione(utente, azienda, RuoloAziendale.ADMIN);

        assertTrue(utente.getAffiliazioni().isEmpty(), "Inizialmente l'utente non dovrebbe avere affiliazioni.");

        utente.addAffiliazione(affiliazione);

        assertEquals(1, utente.getAffiliazioni().size(), "Ci dovrebbe essere 1 affiliazione.");
        assertTrue(utente.getAffiliazioni().contains(affiliazione), "L'affiliazione aggiunta dovrebbe essere presente.");

        utente.removeAffiliazione(affiliazione);

        assertTrue(utente.getAffiliazioni().isEmpty(), "L'utente non dovrebbe avere più affiliazioni dopo la rimozione.");
    }

}
