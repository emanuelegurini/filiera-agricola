package com.filiera.agricola.application.services;

import com.filiera.agricola.application.dto.CreazioneAziendaDTO;
import com.filiera.agricola.domain.DefaultAzienda;
import com.filiera.agricola.domain.DefaultCoordinate;
import com.filiera.agricola.model.enums.TipoAzienda;
import com.filiera.agricola.model.interfaces.AziendaRepository;
import com.filiera.agricola.model.interfaces.AziendaService;

import java.util.UUID;

public class DefaultAziendaService implements AziendaService {

    AziendaRepository aziendaRepository;

    public DefaultAziendaService(AziendaRepository aziendaRepository) {
        this.aziendaRepository = aziendaRepository;
    }

    @Override
    public DefaultAzienda creaNuovaAzienda(CreazioneAziendaDTO dati) {

        aziendaRepository.findByPartitaIva(dati.partitaIva()).ifPresent(a -> {
            throw new IllegalArgumentException("Un'azienda con questa Partita IVA è già registrata.");
        });


        DefaultCoordinate coordinate = new DefaultCoordinate(
                dati.coordinate().latitude(),
                dati.coordinate().longitude()
        );

        DefaultAzienda nuovaAzienda = new DefaultAzienda(
                dati.ragioneSociale(),
                dati.partitaIva(),
                dati.indirizzo(),
                dati.email(),
                dati.numeroTelefono(),
                dati.sitoWeb(),
                coordinate
        );

        if (dati.tipiAzienda() != null) {
            dati.tipiAzienda().forEach(nuovaAzienda::aggiungiTipoAzienda);
        }

        aziendaRepository.save(nuovaAzienda);
        System.out.println("INFO: Nuova azienda registrata con ID: " + nuovaAzienda.getId() + ": " + nuovaAzienda.getRagioneSociale());

        return nuovaAzienda;
    }

    @Override
    public void aggiungiTipoAzienda(UUID aziendaId, TipoAzienda tipoAzienda) {
        DefaultAzienda azienda = aziendaRepository.findById(aziendaId)
                .orElseThrow(() -> new RuntimeException("Azienda non trovata con ID: " + aziendaId));

        azienda.aggiungiTipoAzienda(tipoAzienda);

        aziendaRepository.save(azienda);
        System.out.println("INFO: Aggiunto tipo " + tipoAzienda + " ad azienda " + aziendaId);
    }

}
