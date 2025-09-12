package com.filiera.agricola.domain;

import com.filiera.agricola.model.enums.StatoBiglietto;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class DefaultBiglietto {
    private final UUID id;
    private final DefaultEvento evento;
    private final DefaultUtente intestatario;
    private final LocalDateTime dataEmissione;
    private final double prezzoPagato;
    private StatoBiglietto stato;

    public DefaultBiglietto(DefaultEvento evento, DefaultUtente intestatario) {
        this.id = UUID.randomUUID();
        this.evento = Objects.requireNonNull(evento, "L'evento non può essere nullo.");
        this.intestatario = Objects.requireNonNull(intestatario, "L'intestatario non può essere nullo.");

        this.dataEmissione = LocalDateTime.now();
        this.prezzoPagato = evento.getCostoPartecipazione(); // Prende il prezzo dall'evento
        this.stato = StatoBiglietto.VALIDO;
    }

    public UUID getId() { return id; }
    public DefaultEvento getEvento() { return evento; }
    public DefaultUtente getIntestatario() { return intestatario; }
    public LocalDateTime getDataEmissione() { return dataEmissione; }
    public double getPrezzoPagato() { return prezzoPagato; }
    public StatoBiglietto getStato() { return stato; }

    public void setStato(StatoBiglietto stato) {
        this.stato = stato;
    }
}
