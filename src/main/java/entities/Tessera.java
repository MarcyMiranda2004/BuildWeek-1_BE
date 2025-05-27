package entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
public class Tessera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private UUID codice;

    @Column(name = "data_emissione", nullable = false)
    private LocalDate dataEmissione;

    @Column(name = "data_scadenza", nullable = false)
    private LocalDate dataScadenza;

    @OneToOne(optional = false)
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;

    public Tessera() {
        this.codice = UUID.randomUUID();
    }

    public Tessera(LocalDate dataEmissione, LocalDate dataScadenza, Utente utente) {
        this.codice = UUID.randomUUID();
        this.dataEmissione = dataEmissione;
        this.dataScadenza = dataScadenza;
        this.utente = utente;
    }

    public Long getId() { return id; }

    public UUID getCodice() { return codice; }

    public LocalDate getDataEmissione() { return dataEmissione; }
    public void setDataEmissione(LocalDate dataEmissione) { this.dataEmissione = dataEmissione; }

    public LocalDate getDataScadenza() { return dataScadenza; }
    public void setDataScadenza(LocalDate dataScadenza) { this.dataScadenza = dataScadenza; }

    public Utente getUtente() { return utente; }
    public void setUtente(Utente utente) { this.utente = utente; }

    @Override
    public String toString() {
        return "Tessera{" +
                "id=" + id +
                ", codice='" + codice + '\'' +
                ", dataEmissione=" + dataEmissione +
                ", dataScadenza=" + dataScadenza +
                ", utenteId=" + (utente != null ? utente.getId() : null) +
                '}';
    }
}

