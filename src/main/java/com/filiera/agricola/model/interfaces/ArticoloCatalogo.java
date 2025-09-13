package com.filiera.agricola.model.interfaces;

import com.filiera.agricola.domain.DefaultAzienda;
import com.filiera.agricola.model.enums.StatoValidazione;

import java.util.Objects;
import java.util.UUID;

public abstract class ArticoloCatalogo implements ArticoloVendibile, Validabile {

    protected final UUID id;
    protected String nome;
    protected String descrizione;
    private double prezzoUnitario;
    protected double quantitaDisponibile;
    protected final DefaultAzienda aziendaDiRiferimento;
    protected StatoValidazione stato;

    public ArticoloCatalogo(String nome, String descrizione, double prezzoUnitario, DefaultAzienda azienda) {
        this.id = UUID.randomUUID();
        this.nome = Objects.requireNonNull(nome, "Il nome non può essere nullo.");
        this.descrizione = descrizione;
        this.prezzoUnitario = prezzoUnitario;
        this.aziendaDiRiferimento = Objects.requireNonNull(azienda, "L'azienda di riferimento non può essere nulla.");
        this.stato = StatoValidazione.IN_ATTESA_DI_APPROVAZIONE;
        this.quantitaDisponibile = 0;
    }

    @Override
    public String getNomeArticolo() {
        return this.nome;
    }

    @Override
    public void setNomeArticolo(String nomeArticolo) {
        this.nome = nomeArticolo;
    }

    @Override
    public String getDescrizioneArticolo() {
        return this.descrizione;
    }

    @Override
    public void setDescrizioneArticolo(String descrizione) {
        this.descrizione = descrizione;
    }

    @Override
    public void setStatoValidazione(StatoValidazione stato) {
        this.stato = stato;
    }

    @Override
    public StatoValidazione getStatoValidazione() {
        return this.stato;
    }

    @Override
    public UUID getId() { return id; }

    public double getQuantitaDisponibile() {
        return quantitaDisponibile;
    }

    public void setQuantitaDisponibile(double quantitaDisponibile) {
        this.quantitaDisponibile = quantitaDisponibile;
    }

    public DefaultAzienda getAziendaDiRiferimento() {
        return aziendaDiRiferimento;
    }

    public double getPrezzoUnitario() {
        return prezzoUnitario;
    }

    public void setPrezzoUnitario(double prezzoUnitario) {
        this.prezzoUnitario = prezzoUnitario;
    }
}