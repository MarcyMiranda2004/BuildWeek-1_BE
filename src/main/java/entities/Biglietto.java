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

    @Column(name = "data_validazione")
    private LocalDateTime dataValidazione;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_mezzo", nullable = false)
    private TipoMezzo tipo;

    public Biglietto() {}

    public Biglietto(String codice, LocalDate dataEmissione, PuntoVendita puntoVendita, Utente utente, boolean validato, Mezzo mezzoValidato, LocalDateTime dataValidazione, TipoMezzo tipo) {
        super(codice, dataEmissione, puntoVendita, utente);
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

    // Metodo Creazione Biglietti
    public static Biglietto creaBiglietto(Mezzo mezzo, Utente utente, PuntoVendita puntoVendita) {
        Biglietto biglietto = new Biglietto();
        biglietto.setCodice("B-" + java.util.UUID.randomUUID());
        biglietto.setDataEmissione(LocalDate.now());
        biglietto.setUtente(utente);
        biglietto.setPuntoVendita(puntoVendita);
        biglietto.setValidato(false);
        biglietto.setMezzoValidato(mezzo);
        biglietto.setTipo(mezzo.getTipo());

        return biglietto;
    }

    // Metodo validazione Biglietti
    public static Biglietto validaBiglietto(Biglietto biglietto) {
        if (!biglietto.isValidato()) {
            biglietto.setValidato(true);
            biglietto.setDataValidazione(LocalDateTime.now());
        } else {
            System.out.println("Biglietto già validato o non valido.");
        }

        return biglietto;
    }


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
