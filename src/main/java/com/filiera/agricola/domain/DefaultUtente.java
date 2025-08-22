package com.filiera.agricola.domain;

import com.filiera.agricola.model.enums.RuoloPiattaforma;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.HashSet;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static com.filiera.agricola.utils.ValidationUtils.validateEmail;

public class DefaultUtente {
    private final UUID id;
    private String nome;
    private String cognome;
    private String email;
    private String address;
    private String phoneNumber;
    private String passwordHash;

    private Set<RuoloPiattaforma> ruoli;

    private final Set<DefaultAffiliazione> affiliazioni;

    public DefaultUtente(
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

        this.affiliazioni = new HashSet<>();
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

     public boolean addRuolo(RuoloPiattaforma ruolo) {
        return this.ruoli.add(ruolo);
    }

    /**
     * Aggiunge una nuova affiliazione all'utente.
     * Grazie a equals/hashCode, impedisce di aggiungere una seconda affiliazione
     * per la stessa azienda.
     * @param defaultAffiliazione L'affiliazione da aggiungere.
     */
    public void addAffiliazione(DefaultAffiliazione defaultAffiliazione) {
        // Controlla che l'affiliazione riguardi questo specifico utente
        if (!defaultAffiliazione.getUtente().equals(this)) {
            throw new IllegalArgumentException("L'affiliazione non appartiene a questo utente.");
        }
        this.affiliazioni.add(defaultAffiliazione);
    }

    /**
     * Rimuove un'affiliazione (es. l'utente non lavora più per quell'azienda).
     * @param defaultAffiliazione L'affiliazione da rimuovere.
     */
    public void removeAffiliazione(DefaultAffiliazione defaultAffiliazione) {
        this.affiliazioni.remove(defaultAffiliazione);
    }

    /**
     * Restituisce una vista non modificabile delle affiliazioni dell'utente.
     * @return Un Set non modificabile di Affiliazione.
     */
    public Set<DefaultAffiliazione> getAffiliazioni() {
        return Collections.unmodifiableSet(this.affiliazioni);
    }
}
