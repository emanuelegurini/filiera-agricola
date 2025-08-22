package com.filiera.agricola.model.interfaces;

import com.filiera.agricola.application.dto.CreazioneProdottoDTO;
import com.filiera.agricola.domain.DefaultProdotto;

import java.util.UUID;

public interface ProdottoService {
    DefaultProdotto creaNuovoProdotto(UUID idAttivo, UUID idAzienda, CreazioneProdottoDTO datiProdotto);
}
