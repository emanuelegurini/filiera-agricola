package com.filiera.agricola.model.interfaces;

import com.filiera.agricola.domain.DefaultEvento;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventoRepository {
    void save(DefaultEvento evento);
    Optional<DefaultEvento> findById(UUID id);
    List<DefaultEvento> findAll();

    /**
     * Trova tutti gli eventi a cui una specifica azienda partecipa.
     * @param aziendaId L'ID dell'azienda per cui cercare gli eventi.
     * @return Una lista di eventi.
     */
    List<DefaultEvento> findByAziendaPartecipante(UUID aziendaId);
}
