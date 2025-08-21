package com.filiera.agricola.repository;

import com.filiera.agricola.domain.Prodotto;
import com.filiera.agricola.model.interfaces.ProdottoRepository;

import java.util.*;

/*
* Questa classe implementa il DB in memory.
* In futuro la struttura dati in memory verrà sostituita con la connessione al DB vera e propria.
* Per simulare una vera tabella del DB, che può ricevere accessi multipli nello stesso momento,
* stiamo implementando una ConcurrentHashMap. Questa struttura dati ci permetterà di simulare
* un DB prima di procedere con l'implementazione di SpringBoot, che avverra solo quando avremo
* definito correttamente tutte le varie componenti dell'applicativo.
*
* Per approfondire meglio questa struttura dati: https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentHashMap.html
*/
public class DefaultProdottoRepository implements ProdottoRepository {
    /*
    * Questa Map rappresenta la tabella prodotti che implementeremo successivamente nel DB.
    * L'ID di ogni prodotto è l'ID che si trova tra le proprietà del Prodotto stesso.
    *
    * Per ottenere l'ID di ogni prodotto:
    * Prodotto prod1 = new Prodotto()
    * prod1.getId()
    */
    private final Map<UUID, Prodotto> database = new HashMap<>();

    @Override
    public void Save(Prodotto prodotto) {
        System.out.println("INFO: Salvataggio del prodotto '" + prodotto.getNome() + "' in memoria.");
        database.put(prodotto.getId(), prodotto);
    }

    @Override
    public Optional<Prodotto> findById(UUID id) {
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public List<Prodotto> findAll() {
        return new ArrayList<>(database.values());
    }

}
