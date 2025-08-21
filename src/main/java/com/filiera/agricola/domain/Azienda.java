package com.filiera.agricola.domain;

import com.filiera.agricola.model.enums.TipoAzienda;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static com.filiera.agricola.utils.ValidationUtils.validateEmail;

public class Azienda {
    protected String id;
    protected String ragioneSociale;
    protected String partitaIva;
    protected String indirizzo;
    protected String email;
    protected String numeroTelefono;
    protected String sitoWeb;
    protected LocalDateTime registrationDate;
    protected DefaultCoordinates coordinate;

    /**
     * Insieme dei ruoli che l'azienda ricopre nella filiera.
     * Determina le funzionalità a cui ha accesso (es. creare prodotti trasformati).
     */
    private final Set<TipoAzienda> tipiAzienda;

    public Azienda(
            String ragioneSociale,
            String partitaIva,
            String indirizzo,
            String email,
            String numeroTelefono,
            String sitoWeb,
            DefaultCoordinates coordinate
    ) {
        this.id = UUID.randomUUID().toString();
        this.ragioneSociale = Objects.requireNonNull(ragioneSociale,"Company name cannot be null");
        this.partitaIva = Objects.requireNonNull(partitaIva, "VAT number cannot be null");
        this.indirizzo = Objects.requireNonNull(indirizzo, "Address cannot be null");
        this.email = validateEmail(email);
        this.numeroTelefono = Objects.requireNonNull(numeroTelefono,   "Phone number cannot be null");
        this.sitoWeb = Objects.requireNonNull(sitoWeb,   "Sito web cannot be null");
        this.coordinate = Objects.requireNonNull(coordinate, "Coordinates cannot be null");
        this.tipiAzienda = new HashSet<>();

    }

    public String getId() {
        return id;
    }

    public String getRagioneSociale() {
        return ragioneSociale;
    }

    public void setRagioneSociale(String ragioneSociale) {
        this.ragioneSociale = ragioneSociale;
    }

    public String getPartitaIva() {
        return partitaIva;
    }

    public void setPartitaIva(String partitaIva) {
        this.partitaIva = partitaIva;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public String getSitoWeb() {
        return sitoWeb;
    }

    public void setSitoWeb(String sitoWeb) {
        this.sitoWeb = sitoWeb;
    }

    public String getCoordinate() {
        return "{lat: " + coordinate.getLat() + ", lng: " + coordinate.getLng() + "}";
    }

    public void setCoordinate(DefaultCoordinates coordinate) {
        this.coordinate = coordinate;
    }

    public void aggiungiTipoAzienda(TipoAzienda tipoAzienda) {
        this.tipiAzienda.add(tipoAzienda);
    }

    public void removeTipoAzienda(TipoAzienda tipoAzienda) {
        this.tipiAzienda.remove(tipoAzienda);
    }

    public Set<TipoAzienda> getTipoAzienda() {
        return this.tipiAzienda;
    }
}
