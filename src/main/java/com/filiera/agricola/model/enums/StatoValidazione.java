package com.filiera.agricola.model.enums;

public enum StatoValidazione {
    IN_ATTESA_DI_APPROVAZIONE, // Appena creato, in attesa di revisione
    IN_REVISIONE,              // Un Curatore ha preso in carico la pratica
    APPROVATO,                 // Contenuto valido e pubblicato
    RESPINTO,                  // Contenuto non valido
    MODIFICHE_RICHIESTE        // Il Curatore richiede modifiche all'autore
}
