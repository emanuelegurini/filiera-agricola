package com.filiera.agricola.utils;

import java.util.Objects;
import java.util.regex.Pattern;

public final class ValidationUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    private  ValidationUtils(){}

    public static String validateEmail(String email) {
        Objects.requireNonNull(email, "Email non può essere null");
        String trimmedEmail = email.trim();

        if (trimmedEmail.isEmpty()) {
            throw new IllegalArgumentException("Email non può essere vuota");
        }
        if (!trimmedEmail.contains("@")) {
            throw new IllegalArgumentException("Il formato dell'email non è valido");
        }

        if (!EMAIL_PATTERN.matcher(trimmedEmail).matches()) {
            throw new IllegalArgumentException("Il formato dell'email non è valido");
        }

        return trimmedEmail;
    }

}
