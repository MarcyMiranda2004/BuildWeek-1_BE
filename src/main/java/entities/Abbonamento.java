package entities;

import enums.Periodicita;
import enums.TipoMezzo;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Abbonamento extends TitoloViaggio {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Periodicita periodicita;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMezzo tipo;

    @Column(name = "valido_da", nullable = false)
    private LocalDate validoDa;

    @Column(name = "valido_a", nullable = false)
    private LocalDate validoA;

    public Abbonamento() {}

    public Abbonamento(String codice, LocalDate dataEmissione, PuntoVendita puntoVendita, Utente utente, Periodicita periodicita, TipoMezzo tipo, LocalDate validoDa, LocalDate validoA) {
        super(codice, dataEmissione, puntoVendita, utente);
        this.periodicita = periodicita;
        this.tipo = tipo;
        this.validoDa = validoDa;
        this.validoA = validoA;
    }

    public Periodicita getPeriodicita() {return periodicita;}
    public void setPeriodicita(Periodicita periodicita) {this.periodicita = periodicita;}

    public TipoMezzo getTipo() {return tipo;}
    public void setTipo(TipoMezzo tipo) {this.tipo = tipo;}

    public LocalDate getValidoDa() {return validoDa;}
    public void setValidoDa(LocalDate validoDa) {this.validoDa = validoDa;}

    public LocalDate getValidoA() {return validoA;}
    public void setValidoA(LocalDate validoA) {this.validoA = validoA;}

    @Override
    public String toString() {
        return "Abbonamento{" +
                "periodicita=" + periodicita +
                ", tipo=" + tipo +
                ", validoDa=" + validoDa +
                ", validoA=" + validoA +
                "} " + super.toString();
    }
}
