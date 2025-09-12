package com.filiera.agricola.repository;

import com.filiera.agricola.domain.DefaultBiglietto;
import com.filiera.agricola.model.interfaces.BigliettoRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DefaultBigliettoRepository implements BigliettoRepository {

    private final Map<UUID, DefaultBiglietto> database = new ConcurrentHashMap<>();

    @Override
    public void save (DefaultBiglietto biglietto) {
        System.out.println("INFO: Salvataggio del biglietto '" + biglietto.getEvento().getId() + "' in memoria.");
        database.put(biglietto.getId(), biglietto);
    }

    @Override
    public Optional<DefaultBiglietto> findById(UUID id) {
        // Usa Optional.ofNullable per creare correttamente la "scatola"
        // - Se database.get(id) trova un biglietto, restituisce un Optional che lo contiene.
        // - Se database.get(id) restituisce null, restituisce un Optional vuoto.
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public List<DefaultBiglietto> findByEventoId(UUID eventoId) {
        return database.values().stream()
                .filter(biglietto ->
                        biglietto.getEvento().getId()
                                .equals(eventoId)
                )
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        database.remove(id);
    }

}
