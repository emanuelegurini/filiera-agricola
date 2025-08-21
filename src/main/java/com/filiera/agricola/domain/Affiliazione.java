package com.filiera.agricola.domain;

import com.filiera.agricola.model.enums.RuoloAziendale;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Affiliazione {
    private final UUID id;
    private final Utente utente;
    private final Azienda azienda;
    private RuoloAziendale ruoloAziendale;
    private final LocalDateTime dataAffiliazione;

    public Affiliazione(
            Utente utente,
            Azienda azienda,
            RuoloAziendale ruoloAziendale
    ) {
        this.id = UUID.randomUUID();
        this.utente = Objects.requireNonNull(utente, "L'utente non può essere nullo.");
        this.azienda = Objects.requireNonNull(azienda, "L'azienda non può essere nulla.");
        this.ruoloAziendale = Objects.requireNonNull(ruoloAziendale, "Il ruolo aziendale non può essere nullo.");
        this.dataAffiliazione = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public Utente getUtente() {
        return utente;
    }

    public Azienda getAzienda() {
        return azienda;
    }

    public RuoloAziendale getRuoloAziendale() {
        return ruoloAziendale;
    }

    public void setRuoloAziendale(RuoloAziendale ruoloAziendale) {
        this.ruoloAziendale = ruoloAziendale;
    }

    public LocalDateTime getDataAffiliazione() {
        return dataAffiliazione;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Affiliazione that = (Affiliazione) o;
        return Objects.equals(id, that.id) && Objects.equals(utente, that.utente) && Objects.equals(azienda, that.azienda) && ruoloAziendale == that.ruoloAziendale;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, utente, azienda, ruoloAziendale);
    }
}
