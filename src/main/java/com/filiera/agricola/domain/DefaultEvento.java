package com.filiera.agricola.domain;

import com.filiera.agricola.model.enums.StatoValidazione;
import com.filiera.agricola.model.interfaces.PuntoMappabile;
import com.filiera.agricola.model.interfaces.Validabile;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class DefaultEvento extends PuntoMappabile implements Validabile {
    private final UUID id;
    private final String nome;
    private String descrizione;

    private LocalDateTime dataOraInizio;
    private LocalDateTime dataOraFine;

    private DefaultUtente organizzatore;
    private Set<DefaultAzienda> aziendePartecipanti;

    private int postiDisponibili;
    private double costoPartecipazione;
    private StatoValidazione statoValidazione;

    public DefaultEvento(
            String nome,
            String descrizione,
            LocalDateTime dataOraInizio,
            LocalDateTime dataOraFine,
            DefaultUtente organizzatore,
            String indirizzo,
            DefaultCoordinate coordinate
    ) {
        super(indirizzo, coordinate);
        this.id = UUID.randomUUID();
        this.nome = Objects.requireNonNull(nome, "Il nome dell'evento non può essere nullo.");;
        this.descrizione = descrizione;
        this.dataOraInizio = Objects.requireNonNull(dataOraInizio, "La data di inizio non può essere nulla.");
        this.dataOraFine = Objects.requireNonNull(dataOraFine, "La data di fine non può essere nulla.");

        this.organizzatore = Objects.requireNonNull(organizzatore, "L'organizzatore non può essere nullo.");
        this.statoValidazione = StatoValidazione.IN_ATTESA_DI_APPROVAZIONE;
        this.aziendePartecipanti = new HashSet<>();
        this.postiDisponibili = 0;
        this.costoPartecipazione = 0.0;
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public LocalDateTime getDataOraInizio() {
        return dataOraInizio;
    }

    public void setDataOraInizio(LocalDateTime dataOraInizio) {
        this.dataOraInizio = dataOraInizio;
    }

    public LocalDateTime getDataOraFine() {
        return dataOraFine;
    }

    public void setDataOraFine(LocalDateTime dataOraFine) {
        this.dataOraFine = dataOraFine;
    }

    public DefaultUtente getOrganizzatore() {
        return organizzatore;
    }

    public void setOrganizzatore(DefaultUtente organizzatore) {
        this.organizzatore = organizzatore;
    }

    public Set<DefaultAzienda> getAziendePartecipanti() {
        return aziendePartecipanti;
    }

    public void setAziendePartecipanti(Set<DefaultAzienda> aziendePartecipanti) {
        this.aziendePartecipanti = aziendePartecipanti;
    }

    public void rimuoviAziendaPartecipante(DefaultAzienda azienda) {
       this.aziendePartecipanti.remove(azienda);
    }

    public int getPostiDisponibili() {
        return postiDisponibili;
    }

    public void setPostiDisponibili(int postiDisponibili) {
        this.postiDisponibili = postiDisponibili;
    }

    public double getCostoPartecipazione() {
        return costoPartecipazione;
    }

    public void setCostoPartecipazione(double costoPartecipazione) {
        this.costoPartecipazione = costoPartecipazione;
    }

    @Override
    public Map<String, String> getDatiPerValidazione() {
        Map<String, String> dati = new LinkedHashMap<>();
        dati.put("ID Evento", this.id.toString());
        dati.put("Nome Evento", this.nome);
        dati.put("Organizzatore", this.organizzatore.getNome() + " " + this.organizzatore.getCognome());
        dati.put("Data Inizio", this.dataOraInizio.toString());
        dati.put("Luogo", this.getIndirizzo());
        String aziende = this.aziendePartecipanti.isEmpty()
                ? "Nessuna"
                : this.aziendePartecipanti.stream().map(DefaultAzienda::getRagioneSociale).collect(Collectors.joining(", "));
        dati.put("Aziende Partecipanti", aziende);
        return dati;
    }

    @Override
    public void setStatoValidazione(StatoValidazione stato) {
        this.statoValidazione = stato;
    }

    @Override
    public StatoValidazione getStatoValidazione() {
        return this.statoValidazione;
    }

    public void aggiungiAziendaPartecipante(DefaultAzienda azienda) {
        if (azienda != null) {
            this.aziendePartecipanti.add(azienda);
        }
    }
}
