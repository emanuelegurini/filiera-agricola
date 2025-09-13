package com.filiera.agricola.application.services;

import com.filiera.agricola.domain.DefaultAzienda;
import com.filiera.agricola.domain.DefaultPacchettoProdotti;
import com.filiera.agricola.domain.DefaultProdotto;
import com.filiera.agricola.domain.DefaultUtente;
import com.filiera.agricola.model.enums.RuoloAziendale;
import com.filiera.agricola.repository.DefaultAziendaRepository;
import com.filiera.agricola.repository.DefaultPacchettoProdottiRepository;
import com.filiera.agricola.repository.DefaultProdottoRepository;
import com.filiera.agricola.repository.DefaultUtenteRepository;
import com.filiera.agricola.utils.ScorteInsufficientiException;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

public class DefaultPacchettoProdottiService {

    private final DefaultPacchettoProdottiRepository pacchettoRepository;
    private final DefaultProdottoRepository prodottoRepository;
    private final DefaultUtenteRepository utenteRepository;
    private final DefaultAziendaRepository aziendaRepository;

    public DefaultPacchettoProdottiService(
            DefaultPacchettoProdottiRepository pacchettoRepository,
            DefaultProdottoRepository prodottoRepository,
            DefaultUtenteRepository utenteRepository,
            DefaultAziendaRepository aziendaRepository
    ) {
        this.pacchettoRepository = Objects.requireNonNull(pacchettoRepository);
        this.prodottoRepository = Objects.requireNonNull(prodottoRepository);
        this.utenteRepository = Objects.requireNonNull(utenteRepository);
        this.aziendaRepository = Objects.requireNonNull(aziendaRepository);
    }

    /**
     * Crea un nuovo pacchetto vuoto associato a un'azienda.
     */
    public DefaultPacchettoProdotti creaNuovoPacchetto(UUID utenteId, UUID aziendaId, String nome, String descrizione) {
        DefaultAzienda azienda = checkUserPermissionAndGetAzienda(utenteId, aziendaId);
        DefaultPacchettoProdotti nuovoPacchetto = new DefaultPacchettoProdotti(nome, descrizione, azienda);
        return pacchettoRepository.save(nuovoPacchetto);
    }

    /**
     * Aggiunge una data quantità di un prodotto a un pacchetto.
     * Controlla la disponibilità nel magazzino e aggiorna le scorte dell'azienda.
     */
    public DefaultPacchettoProdotti aggiungiProdottoAlPacchetto(UUID utenteId, UUID pacchettoId, UUID prodottoId, int quantita) {
        if (quantita <= 0) {
            throw new IllegalArgumentException("La quantità deve essere positiva.");
        }

        DefaultPacchettoProdotti pacchetto = pacchettoRepository.findById(pacchettoId)
                .orElseThrow(() -> new NoSuchElementException("Pacchetto non trovato con ID: " + pacchettoId));

        DefaultAzienda azienda = checkUserPermissionAndGetAzienda(utenteId, pacchetto.getAziendaDiRiferimento().getId());

        DefaultProdotto prodottoDaAggiungere = prodottoRepository.findById(prodottoId)
                .orElseThrow(() -> new NoSuchElementException("Prodotto non trovato con ID: " + prodottoId));

        // --- LOGICA DI CONTROLLO E AGGIORNAMENTO MAGAZZINO ---
        // 1. Verifica disponibilità scorte
        if (azienda.getDisponibilita(prodottoDaAggiungere) < quantita) {
            throw new ScorteInsufficientiException(
                    "Scorte insufficienti per il prodotto '" + prodottoDaAggiungere.getNomeArticolo() +
                            "'. Richiesti: " + quantita + ", Disponibili: " + azienda.getDisponibilita(prodottoDaAggiungere)
            );
        }

        // 2. Rimuovi le scorte dal magazzino dell'azienda
        azienda.rimuoviScorte(prodottoDaAggiungere, quantita);

        // 3. Aggiungi il prodotto al pacchetto (con la sua quantità)
        pacchetto.aggiungiProdotto(prodottoDaAggiungere, quantita);

        // 4. Salva entrambe le entità modificate
        aziendaRepository.save(azienda);
        return pacchettoRepository.save(pacchetto);
    }

    /**
     * Rimuove un prodotto da un pacchetto e restituisce le scorte al magazzino dell'azienda.
     */
    public DefaultPacchettoProdotti rimuoviProdottoDalPacchetto(UUID utenteId, UUID pacchettoId, UUID prodottoId) {
        DefaultPacchettoProdotti pacchetto = pacchettoRepository.findById(pacchettoId)
                .orElseThrow(() -> new NoSuchElementException("Pacchetto non trovato con ID: " + pacchettoId));

        DefaultAzienda azienda = checkUserPermissionAndGetAzienda(utenteId, pacchetto.getAziendaDiRiferimento().getId());

        DefaultProdotto prodottoDaRimuovere = prodottoRepository.findById(prodottoId)
                .orElseThrow(() -> new NoSuchElementException("Prodotto non trovato con ID: " + prodottoId));

        // --- LOGICA DI RESTITUZIONE SCORTE AL MAGAZZINO ---
        // 1. Verifica quante unità di quel prodotto sono nel pacchetto
        int quantitaDaRestituire = pacchetto.getProdottiInclusi().getOrDefault(prodottoDaRimuovere, 0);

        if (quantitaDaRestituire > 0) {
            // 2. Rimuovi il prodotto dal pacchetto
            pacchetto.rimuoviProdotto(prodottoDaRimuovere);

            // 3. Aggiungi nuovamente le scorte al magazzino dell'azienda
            azienda.aggiungiScorte(prodottoDaRimuovere, quantitaDaRestituire);

            // 4. Salva entrambe le entità
            aziendaRepository.save(azienda);
            return pacchettoRepository.save(pacchetto);
        }

        // Se il prodotto non era nel pacchetto, non fare nulla e restituisci il pacchetto com'è
        return pacchetto;
    }

    /**
     * Controlla che l'utente abbia il ruolo GESTORE_PRODOTTI per l'azienda specificata.
     */
    private DefaultAzienda checkUserPermissionAndGetAzienda(UUID utenteId, UUID aziendaId) {
        DefaultUtente utente = utenteRepository.findById(utenteId)
                .orElseThrow(() -> new SecurityException("Utente non trovato con ID: " + utenteId));

        boolean isAuthorized = utente.getAffiliazioni().stream()
                .anyMatch(aff -> aff.getAzienda().getId().equals(aziendaId) &&
                        (aff.getRuoloAziendale() == RuoloAziendale.GESTORE_PRODOTTI || aff.getRuoloAziendale() == RuoloAziendale.ADMIN));

        if (!isAuthorized) {
            throw new SecurityException("L'utente " + utenteId + " non ha i permessi per gestire i prodotti dell'azienda " + aziendaId);
        }

        return aziendaRepository.findById(aziendaId)
                .orElseThrow(() -> new NoSuchElementException("Azienda non trovata con ID: " + aziendaId));
    }
}

