package com.filiera.agricola.domain;

import com.filiera.agricola.model.enums.*;
import com.filiera.agricola.model.interfaces.ArticoloVendibile;
import com.filiera.agricola.model.interfaces.Validabile;

import java.util.*;
import java.util.stream.Collectors;

public class DefaultProdotto implements ArticoloVendibile, Validabile {

    private final UUID id;
    private String nome;
    private String descrizione;
    private double prezzoUnitario;
    private UnitaDiMisura unitaDiMisura;
    private double quantitaDisponibile;
    private CategoriaProdotto categoriaProdotto;
    private List<String> certificazioni;

    private final DefaultAzienda defaultAziendaProduttrice;
    private final List<DefaultProdotto> ingredienti;

    private StatoValidazione stato;

    private final TipoProdotto tipoProdotto;

    private MetodoColtivazione metodoColtivazione;

    private String metodoTrasformazione;

    public DefaultProdotto(
            String nome,
            String descrizione,
            double prezzoUnitario,
            UnitaDiMisura unitaDiMisura,
            DefaultAzienda defaultAziendaProduttrice,
            TipoProdotto tipoProdotto,
            CategoriaProdotto categoriaProdotto
    ) {
        this.id = UUID.randomUUID();
        this.nome = Objects.requireNonNull(nome, "Il nome non può essere nullo");
        this.descrizione = descrizione;
        this.prezzoUnitario = prezzoUnitario;
        this.unitaDiMisura = unitaDiMisura;
        this.defaultAziendaProduttrice = Objects.requireNonNull(defaultAziendaProduttrice, "L'azienda produttrice non può essere nulla");
        this.tipoProdotto = Objects.requireNonNull(tipoProdotto, "Il tipo di prodotto non può essere nullo");

        this.ingredienti = new ArrayList<>();
        this.certificazioni = new ArrayList<>();
        this.stato = StatoValidazione.IN_ATTESA_DI_APPROVAZIONE;
        this.quantitaDisponibile = 0;
        this.categoriaProdotto = Objects.requireNonNull(categoriaProdotto, "La categoria non può essere nulla");
    }

    @Override
    public double getPrezzoVendita() {
        return this.prezzoUnitario;
    }

    @Override
    public String getNomeArticolo() {
        return this.nome;
    }

    @Override
    public String getDescrizioneArticolo() {
        return this.descrizione;
    }

    @Override
    public Map<String, String> getDatiPerValidazione() {
        Map<String, String> dati = new LinkedHashMap<>();

        dati.put("ID Prodotto", this.id.toString());
        dati.put("Nome Prodotto", this.nome);
        dati.put("Azienda", this.defaultAziendaProduttrice.getRagioneSociale());
        dati.put("Categoria", this.categoriaProdotto.toString());
        dati.put("Tipo Prodotto", this.tipoProdotto.toString());

        if (this.tipoProdotto == TipoProdotto.MATERIA_PRIMA) {
            dati.put("Metodo di Coltivazione", this.metodoColtivazione != null ? this.metodoColtivazione.toString() : "Non specificato");
        } else {
            dati.put("Metodo di Trasformazione", this.metodoTrasformazione != null ? this.metodoTrasformazione : "Non specificato");

            String listaIngredienti = this.ingredienti.isEmpty()
                    ? "Nessuno specificato"
                    : this.ingredienti.stream()
                    .map(DefaultProdotto::getNome)
                    .collect(Collectors.joining(", "));
            dati.put("Ingredienti", listaIngredienti);
        }

        dati.put("Descrizione", this.descrizione);
        dati.put("Prezzo", String.format("%.2f € / %s", this.prezzoUnitario, this.unitaDiMisura));

        return dati;
    }

    @Override
    public void setStatoValidazione(StatoValidazione stato) {
        this.stato = stato;
    }

    @Override
    public StatoValidazione getStatoValidazione() {
        return this.stato;
    }

    /**
     * Aggiunge un ingrediente a questo prodotto, utile per i prodotti trasformati.
     * @param ingrediente Il prodotto da aggiungere come ingrediente.
     */
    public void aggiungiIngrediente(DefaultProdotto ingrediente) {
        if (this.tipoProdotto == TipoProdotto.MATERIA_PRIMA) {
            throw new IllegalStateException("Non è possibile aggiungere ingredienti a una materia prima.");
        }
        if (ingrediente != null) {
            this.ingredienti.add(ingrediente);
        }
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public double getPrezzoUnitario() {
        return prezzoUnitario;
    }

    public void setPrezzoUnitario(double prezzoUnitario) {
        this.prezzoUnitario = prezzoUnitario;
    }

    public UnitaDiMisura getUnitaDiMisura() {
        return unitaDiMisura;
    }

    public void setUnitaDiMisura(UnitaDiMisura unitaDiMisura) {
        this.unitaDiMisura = unitaDiMisura;
    }

    public double getQuantitaDisponibile() {
        return quantitaDisponibile;
    }

    public void setQuantitaDisponibile(double quantitaDisponibile) {
        this.quantitaDisponibile = quantitaDisponibile;
    }

    public CategoriaProdotto getCategoriaProdotto() {
        return categoriaProdotto;
    }

    public void setCategoriaProdotto(CategoriaProdotto categoriaProdotto) {
        this.categoriaProdotto = categoriaProdotto;
    }

    public List<String> getCertificazioni() {
        return Collections.unmodifiableList(this.certificazioni);
    }

    public void setCertificazioni(List<String> certificazioni) {
        this.certificazioni = (certificazioni == null) ? new ArrayList<>() : new ArrayList<>(certificazioni);
    }

    public DefaultAzienda getAziendaProduttrice() {
        return this.defaultAziendaProduttrice;
    }

    public List<DefaultProdotto> getIngredienti() {
        // Restituisce una vista non modificabile della lista originale.
        // È la soluzione più efficiente.
        return Collections.unmodifiableList(this.ingredienti);
    }

    public TipoProdotto getTipoProdotto() {
        return this.tipoProdotto;
    }

    public MetodoColtivazione getMetodoColtivazione() {
        return this.metodoColtivazione;
    }

    public void setMetodoColtivazione(MetodoColtivazione metodoColtivazione) {
        if (this.tipoProdotto == TipoProdotto.TRASFORMATO) {
            throw new IllegalStateException("Non è possibile definire un metodo di coltivazione per un prodotto trasformato.");
        }
        this.metodoColtivazione = metodoColtivazione;
    }

    public String getMetodoTrasformazione() {
        return metodoTrasformazione;
    }

    public void setMetodoTrasformazione(String metodoTrasformazione) {
        if (this.tipoProdotto == TipoProdotto.MATERIA_PRIMA) {
            throw new IllegalStateException("Non è possibile definire un metodo di trasformazione per una materia prima.");
        }
        this.metodoTrasformazione = metodoTrasformazione;
    }
}
