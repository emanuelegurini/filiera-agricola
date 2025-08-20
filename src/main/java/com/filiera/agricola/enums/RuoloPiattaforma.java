package com.filiera.agricola.enums;

/*
* Questi sono i ruoli che un utente registrato può avere all'interno della piattaforma.
* Un utente può avere anche più ruoli all'interno della piattaforma.
* Il ruolo di base è comunque quello di acquirente. Quindi ogni utente registrato,
* è almeno un acquirente, con annesse proprietà e metodi.
 */
public enum RuoloPiattaforma {
    ACQUIRENTE,
    CURATORE,
    ANIMATORE_FILIERA,
    GESTORE_PIATTAFORMA
}
