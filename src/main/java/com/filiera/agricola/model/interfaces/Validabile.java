package com.filiera.agricola.model.interfaces;

import com.filiera.agricola.model.enums.StatoValidazione;

import java.util.Map;

/**
 * Definisce il contratto per qualsiasi oggetto del dominio che deve essere
 * sottoposto a un processo di validazione da parte di un Curatore.
 *
 * L'implementazione di questa interfaccia permette a un'entità di essere gestita
 * dal sistema di pratiche di validazione in modo standardizzato.
 */
public interface Validabile {

    /**
     * Raccoglie un riassunto dei dati più importanti dell'oggetto
     * da presentare al Curatore per la valutazione.
     *
     * @return una Mappa di (etichetta, valore) rappresentante i dati salienti.
     */
    Map<String, String> getDatiPerValidazione();

    /**
     * Imposta lo stato di validazione corrente dell'oggetto.
     * Questo metodo viene tipicamente chiamato dal servizio di validazione
     * al cambio di stato di una pratica.
     *
     * @param stato il nuovo stato di validazione.
     */
    void setStatoValidazione(StatoValidazione stato);

    /**
     * Restituisce lo stato di validazione attuale dell'oggetto.
     *
     * @return lo StatoValidazione corrente.
     */
    StatoValidazione getStatoValidazione();

    void sottomettiPerValidazione();
}
