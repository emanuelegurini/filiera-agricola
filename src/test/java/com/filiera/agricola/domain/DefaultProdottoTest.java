package com.filiera.agricola.domain;

import com.filiera.agricola.model.enums.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultProdottoTest {

    private DefaultAzienda azienda;
    private DefaultProdotto materiaPrima; // Pomodoro
    private DefaultProdotto prodottoTrasformato; // Passata di pomodoro

    @BeforeEach
    void setUp() {
        azienda = new DefaultAzienda("Azienda Agricola Sole", "98765432109", "Via del Sole 1", "info@sole.it", "071987654", "www.sole.it", new DefaultCoordinate(43.1f, 13.1f));

        materiaPrima = new DefaultProdotto(
                "Pomodoro San Marzano",
                "Pomodoro fresco di campo",
                2.50,
                UnitaDiMisura.KG,
                azienda,
                TipoProdotto.MATERIA_PRIMA,
                CategoriaProdotto.ORTOFRUTTA
        );

        prodottoTrasformato = new DefaultProdotto(
                "Passata di Pomodoro",
                "Passata artigianale",
                3.80,
                UnitaDiMisura.PEZZO,
                azienda,
                TipoProdotto.TRASFORMATO,
                CategoriaProdotto.CONSERVE_E_MARMELLATE
        );
    }

    @Test
    void costruttore_impostaValoriDiDefaultCorretti() {
        assertEquals(StatoValidazione.IN_ATTESA_DI_APPROVAZIONE, materiaPrima.getStatoValidazione());

        assertNotNull(materiaPrima.getIngredienti());
        assertTrue(materiaPrima.getIngredienti().isEmpty());
        assertNotNull(materiaPrima.getCertificazioni());
        assertTrue(materiaPrima.getCertificazioni().isEmpty());
    }

    @Test
    void aggiungiIngrediente_aProdottoTrasformato_funzionaCorrettamente() {
        prodottoTrasformato.aggiungiIngrediente(materiaPrima);

        assertEquals(1, prodottoTrasformato.getIngredienti().size());
        assertTrue(prodottoTrasformato.getIngredienti().contains(materiaPrima));
    }

    @Test
    void aggiungiIngrediente_aMateriaPrima_lanciaIllegalStateException() {
        assertThrows(IllegalStateException.class, () -> {
            materiaPrima.aggiungiIngrediente(prodottoTrasformato);
        }, "Non dovrebbe essere possibile aggiungere ingredienti a una materia prima.");
    }

    @Test
    void setMetodoColtivazione_suMateriaPrima_funzionaCorrettamente() {
        materiaPrima.setMetodoColtivazione(MetodoColtivazione.BIOLOGICO);

        assertEquals(MetodoColtivazione.BIOLOGICO, materiaPrima.getMetodoColtivazione());
    }

    @Test
    void setMetodoColtivazione_suProdottoTrasformato_lanciaIllegalStateException() {
        assertThrows(IllegalStateException.class, () -> {
            prodottoTrasformato.setMetodoColtivazione(MetodoColtivazione.BIOLOGICO);
        });
    }

    @Test
    void setMetodoTrasformazione_suProdottoTrasformato_funzionaCorrettamente() {
        prodottoTrasformato.setMetodoTrasformazione("Cottura lenta a bagnomaria");

        assertEquals("Cottura lenta a bagnomaria", prodottoTrasformato.getMetodoTrasformazione());
    }

    @Test
    void setMetodoTrasformazione_suMateriaPrima_lanciaIllegalStateException() {
        assertThrows(IllegalStateException.class, () -> {
            materiaPrima.setMetodoTrasformazione("Non applicabile");
        });
    }

    @Test
    void getDatiPerValidazione_perMateriaPrima_restituisceDatiCorretti() {
        materiaPrima.setMetodoColtivazione(MetodoColtivazione.AGRICOLTURA_INTEGRATA);

        Map<String, String> dati = materiaPrima.getDatiPerValidazione();

        assertTrue(dati.containsKey("Metodo di Coltivazione"));
        assertEquals("AGRICOLTURA_INTEGRATA", dati.get("Metodo di Coltivazione"));
        assertFalse(dati.containsKey("Metodo di Trasformazione"));
        assertFalse(dati.containsKey("Ingredienti"));
    }

    @Test
    void getDatiPerValidazione_perProdottoTrasformato_restituisceDatiCorretti() {
        prodottoTrasformato.aggiungiIngrediente(materiaPrima);

        Map<String, String> dati = prodottoTrasformato.getDatiPerValidazione();

        assertFalse(dati.containsKey("Metodo di Coltivazione"));
        assertTrue(dati.containsKey("Metodo di Trasformazione"));
        assertTrue(dati.containsKey("Ingredienti"));
        assertEquals("Pomodoro San Marzano", dati.get("Ingredienti"));
    }
}
