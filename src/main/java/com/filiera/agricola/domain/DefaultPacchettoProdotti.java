package com.filiera.agricola.domain;

import com.filiera.agricola.model.interfaces.ArticoloCatalogo;
import com.filiera.agricola.model.interfaces.ArticoloVendibile;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Rappresenta un pacchetto o "bundle" di prodotti, venduto come un unico articolo.
 * Tipicamente creato da un Distributore, raggruppa più {@link ArticoloCatalogo}.
 * Il suo prezzo è calcolato dinamicamente sulla base dei prodotti contenuti.
 */
public class DefaultPacchettoProdotti extends ArticoloCatalogo {

    private Map<ArticoloCatalogo, Integer> prodottiInclusi;

    public DefaultPacchettoProdotti(
            String nome,
            String descrizione,
            DefaultAzienda aziendaDistributrice
    ) {
        // Il prezzo di un pacchetto è calcolato dinamicamente, quindi si inizializza a 0.
        super(nome, descrizione, 0, aziendaDistributrice);
        this.prodottiInclusi = new HashMap<>();
    }

    /**
     * Calcola dinamicamente il prezzo di vendita del pacchetto.
     * È dato dalla somma dei prezzi dei singoli prodotti inclusi,
     * a cui viene eventualmente applicato uno sconto percentuale.
     * @return Il prezzo finale di vendita del pacchetto.
     */
    @Override
    public double getPrezzoVendita() {
        double prezzoTotale = prodottiInclusi.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getPrezzoVendita() * entry.getValue())
                .sum();

        this.setPrezzoUnitario(prezzoTotale);
        return prezzoTotale;
    }

    @Override
    public Map<String, String> getDatiPerValidazione() {
        Map<String, String> dati = new LinkedHashMap<>();
        dati.put("ID Pacchetto", this.id.toString());
        dati.put("Nome Pacchetto", this.nome);
        dati.put("Azienda (Distributore)", this.aziendaDiRiferimento.getRagioneSociale());
        dati.put("Descrizione", this.descrizione);

        String listaProdotti = this.prodottiInclusi.isEmpty()
                ? "Nessun prodotto incluso"
                : this.prodottiInclusi.entrySet().stream()
                .map(entry -> String.format("%dx %s (da %s)",
                        entry.getValue(),
                        entry.getKey().getNomeArticolo(),
                        entry.getKey().getAziendaDiRiferimento().getRagioneSociale()))
                .collect(Collectors.joining("; "));
        dati.put("Prodotti Inclusi", listaProdotti);

        dati.put("Prezzo Calcolato", String.format("%.2f €", getPrezzoVendita()));

        return dati;
    }

    public void aggiungiProdotto(ArticoloCatalogo articolo, int quantita) {
        if (articolo != null && quantita > 0) {
            this.prodottiInclusi.merge(articolo, quantita, Integer::sum);
        }
    }

    public void rimuoviProdotto(ArticoloCatalogo articolo) {
        this.prodottiInclusi.remove(articolo);
    }


    public Map<ArticoloCatalogo, Integer> getProdottiInclusi() {
        return Collections.unmodifiableMap(prodottiInclusi);
    }
}