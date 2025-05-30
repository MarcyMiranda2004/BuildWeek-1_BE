package entities;

import enums.StatoMezzo;
import enums.TipoMezzo;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "mezzi")
public class Mezzo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mezzo", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMezzo tipo;

    @Column(nullable = false)
    private int capienza;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoMezzo stato;

    @OneToMany(mappedBy = "mezzo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PeriodoStato> storicoStati = new ArrayList<>();

    @OneToMany(mappedBy = "mezzo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PercorrenzaTratta> trattePercorse = new ArrayList<>();

    public Mezzo() {}

    //Costruttore per il settaggio dei mezzi di base
    public Mezzo(TipoMezzo tipo, int capienza, StatoMezzo stato) {
        this.tipo = tipo;
        this.capienza = capienza;
        this.stato = stato;
        this.storicoStati = new ArrayList<>();
        this.trattePercorse = new ArrayList<>();
    }

    //costruttore con recupero degli stai del mezzo come lista
    public Mezzo(TipoMezzo tipo, int capienza, StatoMezzo stato, List<PeriodoStato> storicoStati, List<PercorrenzaTratta> trattePercorse) {
        this.tipo = tipo;
        this.capienza = capienza;
        this.stato = stato;
        this.storicoStati = storicoStati != null ? storicoStati : new ArrayList<>();
        this.trattePercorse = trattePercorse != null ? trattePercorse : new ArrayList<>();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public TipoMezzo getTipo() { return tipo; }
    public void setTipo(TipoMezzo tipo) { this.tipo = tipo; }

    public int getCapienza() { return capienza; }
    public void setCapienza(int capienza) { this.capienza = capienza; }

    public StatoMezzo getStato() { return stato; }
    public void setStato(StatoMezzo stato) { this.stato = stato; }

    public List<PeriodoStato> getStoricoStati() { return storicoStati; }
    public void setStoricoStati(List<PeriodoStato> storicoStati) { this.storicoStati = storicoStati; }

    public List<PercorrenzaTratta> getTrattePercorse() { return trattePercorse; }
    public void setTrattePercorse(List<PercorrenzaTratta> trattePercorse) { this.trattePercorse = trattePercorse; }

    public void addPeriodoStato(PeriodoStato periodo) {
        storicoStati.add(periodo);
        periodo.setMezzo(this);
    }

    public void removePeriodoStato(PeriodoStato periodo) {
        storicoStati.remove(periodo);
        periodo.setMezzo(null);
    }

    public void addPercorrenzaTratta(PercorrenzaTratta percorrenza) {
        trattePercorse.add(percorrenza);
        percorrenza.setMezzo(this);
    }

    public void removePercorrenzaTratta(PercorrenzaTratta percorrenza) {
        trattePercorse.remove(percorrenza);
        percorrenza.setMezzo(null);
    }

    @Override
    public String toString() {
        return "Mezzo{" +
                "id=" + id +
                ", tipo=" + tipo +
                ", capienza=" + capienza +
                ", stato=" + stato +
                '}';
    }
}
