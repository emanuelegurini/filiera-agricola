package com.filiera.agricola.repository;

import com.filiera.agricola.domain.DefaultAzienda;
import com.filiera.agricola.model.interfaces.AziendaRepository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultAziendaRepository implements AziendaRepository {

    private final Map<UUID, DefaultAzienda> database = new ConcurrentHashMap<>();

    @Override
    public void save (DefaultAzienda defaultAzienda) {
        System.out.println("INFO: Salvataggio del azienda '" + defaultAzienda.getRagioneSociale() + "' in memoria.");
        database.put(defaultAzienda.getId(), defaultAzienda);
    }

    @Override
    public Optional<DefaultAzienda> findById(UUID id) {
        return Optional.ofNullable(database.get(id));
    }
}
