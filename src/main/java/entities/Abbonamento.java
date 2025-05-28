package entities;

import enums.Periodicita;
import enums.TipoMezzo;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity(name = "abbonamenti")
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

    // relazione tra abbonamento e tessera
    @ManyToOne
    @JoinColumn(name = "tessera_id")
    private Tessera tessera;


    public Abbonamento() {}

    public Abbonamento(String codice, LocalDate dataEmissione, PuntoVendita puntoVendita, Utente utente, Periodicita periodicita, TipoMezzo tipo, LocalDate validoDa, LocalDate validoA,Tessera tessera) {
        super(codice, dataEmissione, puntoVendita, utente);
        this.periodicita = periodicita;
        this.tipo = tipo;
        this.validoDa = validoDa;
        this.validoA = validoA;
        this.tessera=tessera;
    }

    public Periodicita getPeriodicita() {return periodicita;}
    public void setPeriodicita(Periodicita periodicita) {this.periodicita = periodicita;}

    public TipoMezzo getTipo() {return tipo;}
    public void setTipo(TipoMezzo tipo) {this.tipo = tipo;}

    public LocalDate getValidoDa() {return validoDa;}
    public void setValidoDa(LocalDate validoDa) {this.validoDa = validoDa;}

    public LocalDate getValidoA() {return validoA;}
    public void setValidoA(LocalDate validoA) {this.validoA = validoA;}

    public Tessera getTessera() {
        return tessera;
    }

    public void setTessera(Tessera tessera) {
        this.tessera = tessera;
    }

    // Metodo creazione dell abbonamento
    public  static Abbonamento creaAbbonamento(Utente utente,Periodicita periodicita,TipoMezzo tipoMezzo,PuntoVendita puntoVendita,Tessera tessera){
        Abbonamento abbonamento= new Abbonamento();
        abbonamento.setCodice("AB-"+ UUID.randomUUID());
        abbonamento.setDataEmissione(LocalDate.now());
        abbonamento.setPuntoVendita(puntoVendita);
        abbonamento.setUtente(utente);
        abbonamento.setPeriodicita(periodicita);
        abbonamento.setTipo(tipoMezzo);
        abbonamento.setValidoDa(LocalDate.now());
        abbonamento.setValidoA(abbonamento.getValidoDa().plusYears(1));
        abbonamento.setTessera(tessera);
        return abbonamento;
    }

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
