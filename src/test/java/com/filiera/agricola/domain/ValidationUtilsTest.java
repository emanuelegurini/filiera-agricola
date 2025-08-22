package com.filiera.agricola.domain;

import static org.junit.jupiter.api.Assertions.*;

import com.filiera.agricola.utils.ValidationUtils;
import org.junit.jupiter.api.Test;

public class ValidationUtilsTest {
    @Test
    public void validateEmail_conEmailValida_restituisceEmail() {
        String email = "test@gmail.com";
        String result = ValidationUtils.validateEmail(email);

        assertEquals(email, result);
    }

    @Test
    public void validateEmail_conEmailNonValida_lanciaIllegalArgumentException() {
        String emailNonValida = "testemail@";
        assertThrows(IllegalArgumentException.class, () -> ValidationUtils.validateEmail(emailNonValida));
    }

    @Test
    public void validateEmail_conEmailNulla_lanciaNullPointerException() {
        assertThrows(NullPointerException.class, () -> ValidationUtils.validateEmail(null));
    }
}
