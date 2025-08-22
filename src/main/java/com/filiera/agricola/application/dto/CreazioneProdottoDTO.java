package com.filiera.agricola.application.dto;

import com.filiera.agricola.model.enums.CategoriaProdotto;
import com.filiera.agricola.model.enums.MetodoColtivazione;
import com.filiera.agricola.model.enums.TipoProdotto;
import com.filiera.agricola.model.enums.UnitaDiMisura;

import java.util.List;
import java.util.UUID;

public record CreazioneProdottoDTO(
        String nome,
        String descrizione,
        double prezzoUnitario,
        UnitaDiMisura unitaDiMisura,
        CategoriaProdotto categoria,
        TipoProdotto tipoProdotto,

        MetodoColtivazione metodoColtivazione, // Solo per MATERIA_PRIMA
        String metodoTrasformazione,          // Solo per TRASFORMATO
        List<UUID> ingredientiIds             // Solo per TRASFORMATO
) {}
