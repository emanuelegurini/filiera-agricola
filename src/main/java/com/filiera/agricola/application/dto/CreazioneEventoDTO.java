package com.filiera.agricola.application.dto;

import java.time.LocalDateTime;

public record CreazioneEventoDTO(
        String nome,
        String descrizione,
        LocalDateTime dataOraInizio,
        LocalDateTime dataOraFine,
        String indirizzo,
        CoordinateDTO coordinate,
        int postiDisponibili,
        double costoPartecipazione
) {}