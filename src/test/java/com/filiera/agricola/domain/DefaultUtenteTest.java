package com.filiera.agricola.domain;
import static org.junit.jupiter.api.Assertions.*;

import com.filiera.agricola.model.enums.RuoloAziendale;
import com.filiera.agricola.model.enums.RuoloPiattaforma;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class DefaultUtenteTest {
    private DefaultUtente utente;
    private DefaultAzienda azienda;

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

        azienda = new DefaultAzienda(
                "Azienda Agricola Verdi",
                "12345678901",
                "Via Verdi 10",
                "info@verdi.it",
                "071123456",
                "www.verdi.it",
                new DefaultCoordinate(43.0f, 13.0f)
        );
    }

    /*
    * Questo Test verifica che la password sia correttamente criptata.
    * Ma non solo, di fatti, dopo aver verificato che la password inserita non sia null
    * e dopo aver controllato che la password iniziale sia differente dalla password criptata,
    * con l'ulttimo assert viene verificato che la password criptata, una volta decriptata,
    * sia identica alla stringa iniziale. Questo ultimo passaggio viene svolto attraverso il metodo
    * checkpw che si trova nel pacchetto BCrypt.
    */
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

    /*
    * Ogni nuovo utente che si registra alla piattaforma acqusisce di default il ruolo di AQUIRENTE.
    * In questo modo ogni utente registrato ha accesso a determinate operazioni di base, tra cui, appunto,
    * l'acquisto di prodotti dal market. In questo test viene verificato che una volta iscritto, l'utente
    * acquisista almeno un ruolo, quello appunto di acquirente. In ultimo verifichiamo se sia possibile aggiungere
    * nuovi ruoli all'utente, in questo caso proviamo con il ruolo di CURATORE.
    */
    @Test
    void addRuolo_aggiungeCorrettamenteUnNuovoRuolo() {
        assertEquals(1, utente.getRuoli().size());
        assertTrue(utente.getRuoli().contains(RuoloPiattaforma.ACQUIRENTE));

        boolean risultatoAggiunta = utente.addRuolo(RuoloPiattaforma.CURATORE);

        assertTrue(risultatoAggiunta, "Il metodo dovrebbe restituire true quando un ruolo viene aggiunto.");
        assertEquals(2, utente.getRuoli().size(), "Il numero di ruoli dovrebbe essere 2.");
        assertTrue(utente.getRuoli().contains(RuoloPiattaforma.CURATORE), "Il nuovo ruolo CURATORE dovrebbe essere presente.");
    }

    /*
    * Questo test serve per verificare che una volta creato l'utente (e una volta creata l'azienda, ma la sua creazione
    * la valuteremo in test dedicato) sarà possibile associargli un ruolo specifico all'interno di un'azienda. L'utente
    * infatti viene creato senza affiliazioni di alcun tipo, solo con il ruolo di ACQUIRENTE, ma successivamente gli possiamo
    * associare un'affiliazione per azienda. Possiamo eliminare le affiliazioni.
    */
    @Test
    void addAffiliazione_e_removeAffiliazione_gestisconoCorrettamenteLeAffiliazioni() {
        DefaultAffiliazione affiliazione = new DefaultAffiliazione(utente, azienda, RuoloAziendale.ADMIN);

        assertTrue(utente.getAffiliazioni().isEmpty(), "Inizialmente l'utente non dovrebbe avere affiliazioni.");

        utente.addAffiliazione(affiliazione);

        assertEquals(1, utente.getAffiliazioni().size(), "Ci dovrebbe essere 1 affiliazione.");
        assertTrue(utente.getAffiliazioni().contains(affiliazione), "L'affiliazione aggiunta dovrebbe essere presente.");

        utente.removeAffiliazione(affiliazione);

        assertTrue(utente.getAffiliazioni().isEmpty(), "L'utente non dovrebbe avere più affiliazioni dopo la rimozione.");
    }

    /*
    * Con questo test verifichiamo che non sia possibile associare due volte lo stesso ruolo aziendale
    * a un utente.
    */
    @Test
    void addAffiliazione_conLaStessaAffiliazionePiuVolte_nonAggiungeDuplicati() {
        DefaultAffiliazione affiliazione = new DefaultAffiliazione(utente, azienda, RuoloAziendale.ADMIN);

        utente.addAffiliazione(affiliazione);
        utente.addAffiliazione(affiliazione);

        assertEquals(1, utente.getAffiliazioni().size(), "Aggiungere la stessa istanza di affiliazione più volte non deve creare duplicati.");
    }

    /*
    * Con questo test verifichiamo che non sia possibile associare allo stesso utente più ruoli
    * della stessa azienda.
    */
    @Test
    void addAffiliazione_conAffiliazioniDiversePerLaStessaAzienda_nonAggiungeDuplicati() {
        DefaultAffiliazione affiliazioneAdmin = new DefaultAffiliazione(utente, azienda, RuoloAziendale.ADMIN);
        DefaultAffiliazione affiliazioneGestore = new DefaultAffiliazione(utente, azienda, RuoloAziendale.GESTORE_PRODOTTI);

        utente.addAffiliazione(affiliazioneAdmin);
        utente.addAffiliazione(affiliazioneGestore);

        assertEquals(1, utente.getAffiliazioni().size(), "Non dovrebbe essere possibile avere più affiliazioni per la stessa azienda.");

        assertEquals(RuoloAziendale.ADMIN, utente.getAffiliazioni().iterator().next().getRuoloAziendale());
    }

}
