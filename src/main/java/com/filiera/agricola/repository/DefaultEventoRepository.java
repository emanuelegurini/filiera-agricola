package com.filiera.agricola.repository;

import com.filiera.agricola.domain.DefaultEvento;
import com.filiera.agricola.model.interfaces.EventoRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DefaultEventoRepository implements EventoRepository {

    private final Map<UUID, DefaultEvento> database = new ConcurrentHashMap<>();

    @Override
    public void save(DefaultEvento evento) {
        if (database.containsKey(evento.getId())) {
            System.out.println("INFO: Aggiornamento dell'evento '" + evento.getNome() + "' in memoria.");
        } else {
            System.out.println("INFO: Creazione del nuovo evento '" + evento.getNome() + "' in memoria.");
        }
        database.put(evento.getId(), evento);
    }

    @Override
    public Optional<DefaultEvento> findById(UUID id) {
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public List<DefaultEvento> findAll() {
        return List.copyOf(database.values());
    }

    @Override
    public List<DefaultEvento> findByAziendaPartecipante(UUID aziendaId) {
        // Scorre tutti gli eventi nel database...
        return database.values().stream()
                // ...filtra tenendo solo quelli che nella loro lista di partecipanti...
                .filter(evento -> evento.getAziendePartecipanti().stream()
                        // ...hanno almeno un'azienda con l'ID richiesto.
                        .anyMatch(azienda -> azienda.getId().equals(aziendaId)))
                .collect(Collectors.toList());
    }
}