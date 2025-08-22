package com.filiera.agricola.domain;

import com.filiera.agricola.model.enums.RuoloAziendale;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class DefaultAffiliazione {
    private final UUID id;
    private final DefaultUtente defaultUtente;
    private final DefaultAzienda defaultAzienda;
    private RuoloAziendale ruoloAziendale;
    private final LocalDateTime dataAffiliazione;

    public DefaultAffiliazione(
            DefaultUtente defaultUtente,
            DefaultAzienda defaultAzienda,
            RuoloAziendale ruoloAziendale
    ) {
        this.id = UUID.randomUUID();
        this.defaultUtente = Objects.requireNonNull(defaultUtente, "L'utente non può essere nullo.");
        this.defaultAzienda = Objects.requireNonNull(defaultAzienda, "L'azienda non può essere nulla.");
        this.ruoloAziendale = Objects.requireNonNull(ruoloAziendale, "Il ruolo aziendale non può essere nullo.");
        this.dataAffiliazione = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public DefaultUtente getUtente() {
        return defaultUtente;
    }

    public DefaultAzienda getAzienda() {
        return defaultAzienda;
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
        DefaultAffiliazione that = (DefaultAffiliazione) o;
        return Objects.equals(defaultUtente, that.defaultUtente)
                && Objects.equals(defaultAzienda, that.defaultAzienda);
    }

    @Override
    public int hashCode() {
        return Objects.hash(defaultUtente, defaultAzienda);
    }
}
