package com.filiera.agricola.utils;

/**
 * Eccezione lanciata quando si tenta di eseguire un'operazione
 * ma le scorte di un prodotto in magazzino non sono sufficienti.
 */
public class ScorteInsufficientiException extends RuntimeException {

    public ScorteInsufficientiException(String message) {
        super(message);
    }
}