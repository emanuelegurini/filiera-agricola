package application.services;

import com.filiera.agricola.application.dto.CoordinateDTO;
import com.filiera.agricola.application.dto.CreazioneAziendaDTO;
import com.filiera.agricola.application.services.DefaultAziendaService;
import com.filiera.agricola.repository.DefaultAziendaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultAziendaServiceTest {

    private DefaultAziendaService aziendaService;

    @BeforeEach
    void setUp() {
        DefaultAziendaRepository aziendaRepository = new DefaultAziendaRepository();
        aziendaService = new DefaultAziendaService(aziendaRepository);
    }

    @Test
    void creaNuovaAzienda_conPartitaIvaDuplicata_lanciaIllegalArgumentException() {
        var datiAzienda1 = new CreazioneAziendaDTO(
                "Azienda 1", "P_IVA_UNICA", "Indirizzo 1", "email1@test.it", "111", "sito1.it", new CoordinateDTO(1f, 1f), Collections.emptySet()
        );
        aziendaService.creaNuovaAzienda(datiAzienda1);

        var datiAzienda2 = new CreazioneAziendaDTO(
                "Azienda 2", "P_IVA_UNICA", "Indirizzo 2", "email2@test.it", "222", "sito2.it", new CoordinateDTO(2f, 2f), Collections.emptySet()
        );

        assertThrows(IllegalArgumentException.class, () -> aziendaService.creaNuovaAzienda(datiAzienda2));
    }
}
