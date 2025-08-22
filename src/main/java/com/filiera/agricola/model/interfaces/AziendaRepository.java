package com.filiera.agricola.model.interfaces;

import com.filiera.agricola.domain.DefaultAzienda;

import java.util.Optional;
import java.util.UUID;

public interface AziendaRepository {
    void save(DefaultAzienda defaultAzienda);

    Optional<DefaultAzienda> findById(UUID id);
}
