package com.filiera.agricola.domain;

import com.filiera.agricola.enums.RuoloPiattaforma;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.HashSet;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class Utente {
    private final UUID id;
    private String nome;
    private String cognome;
    private String email;
    private String address;
    private String phoneNumber;
    private String passwordHash;

    private Set<RuoloPiattaforma> ruoli;

    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";

    public Utente(
            String nome,
            String cognome,
            String email,
            String address,
            String phoneNumber,
            String password
    ) {
        this.id = UUID.randomUUID();
        this.nome = Objects.requireNonNull(nome,"Nome non può essere null");
        this.cognome = Objects.requireNonNull(cognome, "Cognome non può essere null");
        this.address = Objects.requireNonNull(address, "L'indirizzo non può essere null");
        this.email = validateEmail(email);
        this.phoneNumber = Objects.requireNonNull(phoneNumber,   "Il numero di telefono non può essere null");
        setPassword(password);

        this.ruoli = new HashSet<>();
        this.ruoli.add(RuoloPiattaforma.ACQUIRENTE);
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

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPassword(String password) {
        Objects.requireNonNull(password, "Password non può essere null");
        this.passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public void setPasswordHash(String password) {
        setPassword(password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = validateEmail(email);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<RuoloPiattaforma> getRuoli() {
        return Collections.unmodifiableSet(this.ruoli);
    }

     public void addRuolo(RuoloPiattaforma ruolo) {
         if (this.ruoli.contains(ruolo)) {
             throw new IllegalArgumentException("Il ruolo è già presente nel set.");
         }

        this.ruoli.add(ruolo);
    }

    private String validateEmail(String email) {
        Objects.requireNonNull(email, "Email non può essere null");
        String trimmed = email.trim();

        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Email non può essere vuota");
        }
        if (!trimmed.contains("@")) {
            throw new IllegalArgumentException("Il formato dell'email non è valido");
        }

        if (!EMAIL_REGEX.matches(trimmed)) {
            throw new IllegalArgumentException("Il formato dell'email non è valido");
        }

        return trimmed;
    }
}
