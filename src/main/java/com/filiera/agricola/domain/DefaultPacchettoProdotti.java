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

    private final List<ArticoloCatalogo> prodottiInclusi;

    public DefaultPacchettoProdotti(
            String nome,
            String descrizione,
            DefaultAzienda aziendaDistributrice
    ) {
        // Il prezzo di un pacchetto è calcolato dinamicamente, quindi si inizializza a 0.
        super(nome, descrizione, 0, aziendaDistributrice);
        this.prodottiInclusi = new ArrayList<>();
    }

    /**
     * Calcola dinamicamente il prezzo di vendita del pacchetto.
     * È dato dalla somma dei prezzi dei singoli prodotti inclusi,
     * a cui viene eventualmente applicato uno sconto percentuale.
     * @return Il prezzo finale di vendita del pacchetto.
     */
    @Override
    public double getPrezzoVendita() {
        double prezzoTotale = prodottiInclusi.stream()
                .mapToDouble(ArticoloVendibile::getPrezzoVendita)
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
                : this.prodottiInclusi.stream()
                .map(p -> String.format("%s (da %s)", p.getNomeArticolo(), p.getAziendaDiRiferimento().getRagioneSociale()))
                .collect(Collectors.joining("; "));
        dati.put("Prodotti Inclusi", listaProdotti);

        dati.put("Prezzo Calcolato", String.format("%.2f €", getPrezzoVendita()));

        return dati;
    }

    /**
     * Aggiunge un articolo al pacchetto.
     * @param articolo L'articolo da includere nel pacchetto.
     */
    public void aggiungiProdotto(ArticoloCatalogo articolo) {
        if (articolo != null) {
            this.prodottiInclusi.add(articolo);
        }
    }

    /**
     * Rimuove un articolo dal pacchetto.
     * @param articolo L'articolo da rimuovere.
     */
    public void rimuoviProdotto(ArticoloCatalogo articolo) {
        this.prodottiInclusi.remove(articolo);
    }

    /**
     * Restituisce una lista non modificabile dei prodotti inclusi nel pacchetto.
     * @return La lista dei prodotti.
     */
    public List<ArticoloCatalogo> getProdottiInclusi() {
        return Collections.unmodifiableList(prodottiInclusi);
    }
}