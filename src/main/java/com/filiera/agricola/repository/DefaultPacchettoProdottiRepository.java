package com.filiera.agricola.repository;

import com.filiera.agricola.domain.DefaultPacchettoProdotti;

import java.util.*;

public class DefaultPacchettoProdottiRepository {

    private final Map<UUID, DefaultPacchettoProdotti> database = new HashMap<>();

    public DefaultPacchettoProdotti save(DefaultPacchettoProdotti pacchetto) {
        database.put(pacchetto.getId(), pacchetto);
        return pacchetto;
    }

    public Optional<DefaultPacchettoProdotti> findById(UUID id) {
        return Optional.ofNullable(database.get(id));
    }

    public List<DefaultPacchettoProdotti> findAll() {
        return List.copyOf(database.values());
    }

    public void deleteById(UUID id) {
        database.remove(id);
    }
}