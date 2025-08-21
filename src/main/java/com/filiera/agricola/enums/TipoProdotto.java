package com.filiera.agricola.enums;

/**
 * Distingue tra una materia prima e un prodotto risultato di una trasformazione.
 */
public enum TipoProdotto {
    /**
     * Un prodotto agricolo non processato, alla base della filiera.
     * Es: Grano, Pomodori, Latte.
     */
    MATERIA_PRIMA,

    /**
     * Un prodotto ottenuto dalla lavorazione di una o più materie prime.
     * Es: Pane, Passata di pomodoro, Formaggio.
     */
    TRASFORMATO
}
