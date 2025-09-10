package com.filiera.agricola.model.interfaces;

import com.filiera.agricola.application.dto.CreazioneEventoDTO;
import com.filiera.agricola.domain.DefaultEvento;

import java.util.List;
import java.util.UUID;

public interface EventoService {

    DefaultEvento creaNuovoEvento(UUID organizzatoreId, CreazioneEventoDTO datiEvento);

    void aggiungiAziendaAdEvento(UUID eventoId, UUID aziendaId);

    void rimuoviAziendaDaEvento(UUID eventoId, UUID aziendaId);

    List<DefaultEvento> trovaEventiPerAzienda(UUID aziendaId);
}

