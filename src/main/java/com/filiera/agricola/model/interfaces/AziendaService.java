package com.filiera.agricola.model.interfaces;

import com.filiera.agricola.application.dto.CreazioneAziendaDTO;
import com.filiera.agricola.domain.DefaultAzienda;
import com.filiera.agricola.model.enums.TipoAzienda;

import java.util.UUID;

public interface AziendaService {

    DefaultAzienda creaNuovaAzienda(CreazioneAziendaDTO datiAzienda);

    void aggiungiTipoAzienda(UUID aziendaId, TipoAzienda tipoAzienda);
}
