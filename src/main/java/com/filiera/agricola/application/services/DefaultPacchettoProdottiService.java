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

    public DefaultPacchettoProdotti creaNuovoPacchetto(UUID utenteId, UUID aziendaId, String nome, String descrizione) {
        DefaultAzienda azienda = checkUserPermissionAndGetAzienda(utenteId, aziendaId);

        DefaultPacchettoProdotti nuovoPacchetto = new DefaultPacchettoProdotti(nome, descrizione, azienda);

        return pacchettoRepository.save(nuovoPacchetto);
    }

    public DefaultPacchettoProdotti aggiungiProdottoAlPacchetto(UUID utenteId, UUID pacchettoId, UUID prodottoId) {
        DefaultPacchettoProdotti pacchetto = pacchettoRepository.findById(pacchettoId)
                .orElseThrow(() -> new NoSuchElementException("Pacchetto non trovato con ID: " + pacchettoId));

        checkUserPermissionAndGetAzienda(utenteId, pacchetto.getAziendaDiRiferimento().getId());

        DefaultProdotto prodottoDaAggiungere = prodottoRepository.findById(prodottoId)
                .orElseThrow(() -> new NoSuchElementException("Prodotto non trovato con ID: " + prodottoId));

        pacchetto.aggiungiProdotto(prodottoDaAggiungere);

        return pacchettoRepository.save(pacchetto);
    }

    public DefaultPacchettoProdotti rimuoviProdottoDalPacchetto(UUID utenteId, UUID pacchettoId, UUID prodottoId) {
        DefaultPacchettoProdotti pacchetto = pacchettoRepository.findById(pacchettoId)
                .orElseThrow(() -> new NoSuchElementException("Pacchetto non trovato con ID: " + pacchettoId));

        checkUserPermissionAndGetAzienda(utenteId, pacchetto.getAziendaDiRiferimento().getId());

        DefaultProdotto prodottoDaRimuovere = prodottoRepository.findById(prodottoId)
                .orElseThrow(() -> new NoSuchElementException("Prodotto non trovato con ID: " + prodottoId));

        pacchetto.rimuoviProdotto(prodottoDaRimuovere);

        return pacchettoRepository.save(pacchetto);
    }

    private DefaultAzienda checkUserPermissionAndGetAzienda(UUID utenteId, UUID aziendaId) {
        DefaultUtente utente = utenteRepository.findById(utenteId)
                .orElseThrow(() -> new SecurityException("Utente non trovato con ID: " + utenteId));

        boolean isAuthorized = utente.getAffiliazioni().stream()
                .anyMatch(aff -> aff.getAzienda().getId().equals(aziendaId) &&
                        aff.getRuoloAziendale() == RuoloAziendale.GESTORE_PRODOTTI);

        if (!isAuthorized) {
            throw new SecurityException("L'utente " + utenteId + " non ha i permessi per gestire i prodotti dell'azienda " + aziendaId);
        }

        return aziendaRepository.findById(aziendaId)
                .orElseThrow(() -> new NoSuchElementException("Azienda non trovata con ID: " + aziendaId));
    }
}
