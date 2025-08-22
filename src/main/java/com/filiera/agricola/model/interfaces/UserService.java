package com.filiera.agricola.model.interfaces;

import com.filiera.agricola.application.dto.RegistrazioneUtenteDTO;
import com.filiera.agricola.domain.DefaultUtente;
import com.filiera.agricola.model.enums.RuoloAziendale;

import java.util.UUID;

public interface UserService {

    DefaultUtente registraNuovoUtente(RegistrazioneUtenteDTO registrazioneUtenteDTO);

    void aggiungiAffiliazione(UUID utenteId, UUID aziendaId, RuoloAziendale ruolo);
}
