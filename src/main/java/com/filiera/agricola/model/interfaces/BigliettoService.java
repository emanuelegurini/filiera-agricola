package com.filiera.agricola.model.interfaces;

import com.filiera.agricola.domain.DefaultBiglietto;

import java.util.List;
import java.util.UUID;

public interface BigliettoService {

    DefaultBiglietto acquistaBiglietto(UUID utenteId, UUID eventoId);

    void annullaBiglietto(UUID bigliettoId);

    List<DefaultBiglietto> trovaBigliettiPerEvento(UUID eventoId);
}
