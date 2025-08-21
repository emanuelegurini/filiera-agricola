package com.filiera.agricola.model.interfaces;

import com.filiera.agricola.domain.Prodotto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProdottoRepository {
    void Save(Prodotto prodotto);

    Optional<Prodotto> findById(UUID id);

    List<Prodotto> findAll();
}
