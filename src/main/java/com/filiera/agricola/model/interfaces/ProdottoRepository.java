package com.filiera.agricola.model.interfaces;

import com.filiera.agricola.domain.DefaultProdotto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProdottoRepository {
    void Save(DefaultProdotto defaultProdotto);

    Optional<DefaultProdotto> findById(UUID id);

    List<DefaultProdotto> findAll();
}
