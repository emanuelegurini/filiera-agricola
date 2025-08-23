package com.filiera.agricola.repository;

import com.filiera.agricola.domain.DefaultUtente;
import com.filiera.agricola.model.interfaces.UtenteRepository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultUtenteRepository implements UtenteRepository {

    private final Map<UUID, DefaultUtente> database = new ConcurrentHashMap<>();

    @Override
    public void save(DefaultUtente utente) {
        boolean isUpdate = database.containsKey(utente.getId());

        if (isUpdate) {
            System.out.println("INFO: Aggiornamento dell'utente '" + utente.getEmail() + "' in memoria.");
        } else {
            System.out.println("INFO: Creazione del nuovo utente '" + utente.getEmail() + "' in memoria.");
        }

        database.put(utente.getId(), utente);
    }

    @Override
    public Optional<DefaultUtente> findById(UUID id) {
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public Optional<DefaultUtente> findByEmail(String email) {
        return database.values().stream()
                .filter(utente -> utente.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

}
