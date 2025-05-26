package entities;

import enums.TipoMezzo;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "biglietti")
public class Biglietto extends TitoloViaggio {
    @Column(name = "validato", nullable = false)
    private boolean validato;

    @ManyToOne(optional = false)
    @JoinColumn(name = "mezzo_validato_id", nullable = false)
    private Mezzo mezzoValidato;

    @Column(name = "data_validazione", nullable = false)
    private LocalDateTime dataValidazione;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_mezzo", nullable = false)
    private TipoMezzo tipo;

    public Biglietto() {}

    public Biglietto(Long id, String codice, LocalDate dataEmissione, PuntoVendita puntoVendita, Utente utente,
                     boolean validato, Mezzo mezzoValidato, LocalDateTime dataValidazione, TipoMezzo tipo) {
        super(id, codice, dataEmissione, puntoVendita, utente);
        this.validato = validato;
        this.mezzoValidato = mezzoValidato;
        this.dataValidazione = dataValidazione;
        this.tipo = tipo;
    }

    public boolean isValidato() {return validato;}
    public void setValidato(boolean validato) {this.validato = validato;}

    public Mezzo getMezzoValidato() {return mezzoValidato;}
    public void setMezzoValidato(Mezzo mezzoValidato) {this.mezzoValidato = mezzoValidato;}

    public LocalDateTime getDataValidazione() {return dataValidazione;}
    public void setDataValidazione(LocalDateTime dataValidazione) {this.dataValidazione = dataValidazione;}

    public TipoMezzo getTipo() {return tipo;}
    public void setTipo(TipoMezzo tipo) {this.tipo = tipo;}

    @Override
    public String toString() {
        return "Biglietto{" +
                "validato=" + validato +
                ", mezzoValidato=" + mezzoValidato +
                ", dataValidazione=" + dataValidazione +
                ", tipo=" + tipo +
                "} " + super.toString();
    }
}
