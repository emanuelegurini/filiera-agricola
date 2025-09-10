package com.filiera.agricola.model.interfaces;

import com.filiera.agricola.domain.DefaultCoordinate;

import java.util.Objects;

public abstract class PuntoMappabile {

    protected String indirizzo;
    protected DefaultCoordinate coordinate;

    public PuntoMappabile(String indirizzo, DefaultCoordinate coordinate) {
        this.indirizzo = Objects.requireNonNull(indirizzo, "L'indirizzo non può essere nullo.");
        this.coordinate = Objects.requireNonNull(coordinate, "Le coordinate non possono essere nulle.");
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = Objects.requireNonNull(indirizzo, "L'indirizzo non può essere nullo.");
    }

    public DefaultCoordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(DefaultCoordinate coordinate) {
        this.coordinate = Objects.requireNonNull(coordinate, "Le coordinate non possono essere nulle.");
    }
}
