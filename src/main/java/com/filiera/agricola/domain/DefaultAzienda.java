package com.filiera.agricola.domain;

import com.filiera.agricola.model.enums.TipoAzienda;
import com.filiera.agricola.model.interfaces.PuntoMappabile;

import java.time.LocalDateTime;
import java.util.*;

import static com.filiera.agricola.utils.ValidationUtils.validateEmail;

public class DefaultAzienda extends PuntoMappabile {
    protected UUID id;
    protected String ragioneSociale;
    protected String partitaIva;
    protected String email;
    protected String numeroTelefono;
    protected String sitoWeb;
    protected LocalDateTime registrationDate;
    private final Map<DefaultProdotto, Integer> magazzino;

    /**
     * Insieme dei ruoli che l'azienda ricopre nella filiera.
     * Determina le funzionalità a cui ha accesso (es. creare prodotti trasformati).
     */
    private final Set<TipoAzienda> tipiAzienda;

    public DefaultAzienda(
            String ragioneSociale,
            String partitaIva,
            String indirizzo,
            String email,
            String numeroTelefono,
            String sitoWeb,
            DefaultCoordinate coordinate
    ) {
        super(indirizzo, coordinate);

        this.id = UUID.randomUUID();
        this.ragioneSociale = Objects.requireNonNull(ragioneSociale,"Company name cannot be null");
        this.partitaIva = Objects.requireNonNull(partitaIva, "VAT number cannot be null");
        this.email = validateEmail(email);
        this.numeroTelefono = Objects.requireNonNull(numeroTelefono,   "Phone number cannot be null");
        this.sitoWeb = Objects.requireNonNull(sitoWeb,   "Sito web cannot be null");
        this.tipiAzienda = new HashSet<>();
        this.magazzino = new HashMap<>();

    }

    public UUID getId() {
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
        this.email = validateEmail(email);
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

    public void setCoordinate(DefaultCoordinate coordinate) {
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

    public Map<DefaultProdotto, Integer> getMagazzino() {
        return Collections.unmodifiableMap(this.magazzino);
    }

    public void aggiungiScorte(DefaultProdotto prodotto, int quantita) {
        if (prodotto == null || quantita <= 0) {
            return;
        }
        this.magazzino.merge(prodotto, quantita, Integer::sum);
    }

    public boolean rimuoviScorte(DefaultProdotto prodotto, int quantita) {
        if (prodotto == null || quantita <= 0) {
            return false;
        }

        int disponibilitaAttuale = getDisponibilita(prodotto);

        if (disponibilitaAttuale >= quantita) {

            if (disponibilitaAttuale == quantita) {
                this.magazzino.remove(prodotto);
            } else {
                this.magazzino.merge(prodotto, -quantita, Integer::sum);
            }
            return true;
        }
        return false;
    }

    public int getDisponibilita(DefaultProdotto prodotto) {
        return this.magazzino.getOrDefault(prodotto, 0);
    }
}
