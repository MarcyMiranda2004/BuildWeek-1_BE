package entities;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class TitoloViaggio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String codice;

    @Column(nullable = false)
    private LocalDate dataEmissione;

    @ManyToOne(optional = false)
    @JoinColumn(name = "punto_vendita_id", nullable = false)
    private PuntoVendita puntoVendita;

    @ManyToOne(optional = false)
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;

    public TitoloViaggio() {}

    public TitoloViaggio(String codice, LocalDate dataEmissione, PuntoVendita puntoVendita, Utente utente) {
        this.codice = codice;
        this.dataEmissione = dataEmissione;
        this.puntoVendita = puntoVendita;
        this.utente = utente;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodice() { return codice; }
    public void setCodice(String codice) { this.codice = codice; }

    public LocalDate getDataEmissione() { return dataEmissione; }
    public void setDataEmissione(LocalDate dataEmissione) { this.dataEmissione = dataEmissione; }

    public PuntoVendita getPuntoVendita() { return puntoVendita; }
    public void setPuntoVendita(PuntoVendita puntoVendita) { this.puntoVendita = puntoVendita; }

    public Utente getUtente() { return utente; }
    public void setUtente(Utente utente) { this.utente = utente; }

    @Override
    public String toString() {
        return "TitoloViaggio{" +
                "id=" + id +
                ", codice='" + codice + '\'' +
                ", dataEmissione=" + dataEmissione +
                ", puntoVenditaId=" + (puntoVendita != null ? puntoVendita.getId() : null) +
                ", utenteId=" + (utente != null ? utente.getId() : null) +
                '}';
    }
}
