package com.filiera.agricola.application.services;

import com.filiera.agricola.application.dto.CreazioneProdottoDTO;
import com.filiera.agricola.domain.DefaultAzienda;
import com.filiera.agricola.domain.DefaultProdotto;
import com.filiera.agricola.domain.DefaultUtente;
import com.filiera.agricola.model.enums.RuoloAziendale;
import com.filiera.agricola.model.enums.TipoProdotto;
import com.filiera.agricola.model.interfaces.AziendaRepository;
import com.filiera.agricola.model.interfaces.ProdottoRepository;
import com.filiera.agricola.model.interfaces.ProdottoService;
import com.filiera.agricola.model.interfaces.UtenteRepository;

import java.util.List;
import java.util.UUID;

public class DefaultProdottoService implements ProdottoService {

    private final ProdottoRepository prodottoRepository;
    private final UtenteRepository utenteRepository;
    private final AziendaRepository aziendaRepository;

    public DefaultProdottoService(
            ProdottoRepository pr,
            UtenteRepository ur,
            AziendaRepository ar
    ) {
        this.prodottoRepository = pr;
        this.utenteRepository = ur;
        this.aziendaRepository = ar;
    }

    public DefaultProdotto creaNuovoProdotto(
            UUID idUtente,
            UUID idAzienda,
            CreazioneProdottoDTO dati
    ) {
        DefaultUtente utente = utenteRepository.findById(idUtente)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
        DefaultAzienda azienda = aziendaRepository.findById(idAzienda)
                .orElseThrow(() -> new RuntimeException("Azienda non trovata"));

        verificaPermessoCreazione(utente, azienda);

        if (dati.tipoProdotto() == TipoProdotto.MATERIA_PRIMA && dati.ingredientiIds() != null && !dati.ingredientiIds().isEmpty()) {
            throw new IllegalArgumentException("Una materia prima non può avere ingredienti.");
        }

        DefaultProdotto nuovoProdotto = new DefaultProdotto(
                dati.nome(),
                dati.descrizione(),
                dati.prezzoUnitario(),
                dati.unitaDiMisura(),
                azienda,
                dati.tipoProdotto(),
                dati.categoria()
        );

        nuovoProdotto.setMetodoColtivazione(dati.metodoColtivazione());
        nuovoProdotto.setMetodoTrasformazione(dati.metodoTrasformazione());

        if (dati.tipoProdotto() == TipoProdotto.TRASFORMATO && dati.ingredientiIds() != null) {
            List<DefaultProdotto> ingredienti = dati.ingredientiIds().stream()
                    .map(ingId -> prodottoRepository.findById(ingId)
                            .orElseThrow(() -> new RuntimeException("Ingrediente non trovato con ID: " + ingId)))
                    .toList();

            ingredienti.forEach(nuovoProdotto::aggiungiIngrediente);
        }

        prodottoRepository.save(nuovoProdotto);
        System.out.println("INFO: Creato nuovo prodotto '" + nuovoProdotto.getNome() + "' per azienda " + azienda.getRagioneSociale());

        return nuovoProdotto;
    }

    private void verificaPermessoCreazione(DefaultUtente utente, DefaultAzienda azienda) {
        boolean haPermesso = utente.getAffiliazioni().stream()
                .anyMatch(aff ->
                        aff.getAzienda().equals(azienda) &&
                                (aff.getRuoloAziendale() == RuoloAziendale.ADMIN ||
                                        aff.getRuoloAziendale() == RuoloAziendale.GESTORE_PRODOTTI)
                );

        if (!haPermesso) {
            throw new SecurityException("L'utente " + utente.getNome() + " non ha i permessi per creare prodotti per l'azienda " + azienda.getRagioneSociale());
        }
    }
}
