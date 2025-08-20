package com.filiera.agricola.enums;

/**
 * Definisce i ruoli specifici che un Utente può avere
 * all'interno di una singola Azienda a cui è affiliato.
 */
public enum RuoloAziendale {
    /**
     * Massimi privilegi. Può gestire il profilo aziendale e i permessi degli altri utenti.
     * Ha implicitamente tutti i permessi sottostanti.
     */
    ADMIN,

    /**
     * Ruolo operativo per la gestione del catalogo. Può creare e modificare
     * prodotti, pacchetti e gestire la tracciabilità.
     */
    GESTORE_PRODOTTI,

    /**
     * Ruolo operativo per la gestione delle vendite. Può visualizzare
     * e aggiornare lo stato degli ordini.
     */
    GESTORE_ORDINI,

    /**
     * Ruolo passivo. Può visualizzare tutti i dati dell'azienda ma non
     * può apportare alcuna modifica.
     */
    VISUALIZZATORE
}
