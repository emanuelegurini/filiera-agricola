package com.filiera.agricola.application.services;

import com.filiera.agricola.domain.DefaultBiglietto;
import com.filiera.agricola.domain.DefaultEvento;
import com.filiera.agricola.domain.DefaultUtente;
import com.filiera.agricola.model.enums.StatoBiglietto;
import com.filiera.agricola.model.interfaces.BigliettoRepository;
import com.filiera.agricola.model.interfaces.BigliettoService;
import com.filiera.agricola.model.interfaces.EventoRepository;
import com.filiera.agricola.model.interfaces.UtenteRepository;

import java.util.List;
import java.util.UUID;

public class DefaultBigliettoService implements BigliettoService {

    private final BigliettoRepository bigliettoRepository;
    private final UtenteRepository utenteRepository;
    private final EventoRepository eventoRepository;

    public DefaultBigliettoService(BigliettoRepository bigliettoRepository, UtenteRepository utenteRepository, EventoRepository eventoRepository) {
        this.bigliettoRepository = bigliettoRepository;
        this.utenteRepository = utenteRepository;
        this.eventoRepository = eventoRepository;
    }

    @Override
    public DefaultBiglietto acquistaBiglietto(UUID utenteId, UUID eventoId) {
        // 1. Recupera le entità necessarie dai repository
        DefaultUtente utente = utenteRepository.findById(utenteId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato con ID: " + utenteId));
        DefaultEvento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento non trovato con ID: " + eventoId));

        // 2. Logica di business: controlla la disponibilità dei posti
        int postiDisponibili = evento.getPostiDisponibili();
        if (postiDisponibili > 0) { // Un valore > 0 indica posti limitati
            // Chiediamo al repository quanti biglietti sono già stati emessi per questo evento
            List<DefaultBiglietto> bigliettiEmessi = bigliettoRepository.findByEventoId(eventoId);
            if (bigliettiEmessi.size() >= postiDisponibili) {
                throw new RuntimeException("L'evento è al completo. Non ci sono più posti disponibili.");
            }
        }

        // 3. Se tutti i controlli passano, crea e salva il nuovo biglietto
        DefaultBiglietto nuovoBiglietto = new DefaultBiglietto(evento, utente);
        bigliettoRepository.save(nuovoBiglietto);

        System.out.println("INFO: Emesso biglietto " + nuovoBiglietto.getId() + " per utente " + utenteId + " per l'evento '" + evento.getNome() + "'");
        return nuovoBiglietto;
    }

    @Override
    public void annullaBiglietto(UUID bigliettoId) {
        DefaultBiglietto biglietto = bigliettoRepository.findById(bigliettoId)
                .orElseThrow(() -> new RuntimeException("Biglietto non trovato con ID: " + bigliettoId));

        // Modifica lo stato dell'entità
        biglietto.setStato(StatoBiglietto.ANNULLATO);

        // Salva l'entità aggiornata
        bigliettoRepository.save(biglietto);
        System.out.println("INFO: Annullato biglietto " + bigliettoId);
    }

    @Override
    public List<DefaultBiglietto> trovaBigliettiPerEvento(UUID eventoId) {
        // Delega la logica di ricerca al repository, come da nostro design
        return bigliettoRepository.findByEventoId(eventoId);
    }
}
