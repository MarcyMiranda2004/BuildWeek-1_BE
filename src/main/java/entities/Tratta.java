package entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Tratta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String zonaPartenza;

    @Column(nullable = false)
    private String capolinea;

    @Column(nullable = false)
    private int tempoPrevistoMinuti;

    // Lista delle percorrenze di questa tratta
    @OneToMany(mappedBy = "tratta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PercorrenzaTratta> percorrenze = new ArrayList<>();

    public Tratta() {}

    public Tratta(String zonaPartenza, String capolinea, int tempoPrevistoMinuti) {
        this.zonaPartenza = zonaPartenza;
        this.capolinea = capolinea;
        this.tempoPrevistoMinuti = tempoPrevistoMinuti;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getZonaPartenza() { return zonaPartenza; }
    public void setZonaPartenza(String zonaPartenza) { this.zonaPartenza = zonaPartenza; }

    public String getCapolinea() { return capolinea; }
    public void setCapolinea(String capolinea) { this.capolinea = capolinea; }

    public int getTempoPrevistoMinuti() { return tempoPrevistoMinuti; }
    public void setTempoPrevistoMinuti(int tempoPrevistoMinuti) { this.tempoPrevistoMinuti = tempoPrevistoMinuti; }


    public List<PercorrenzaTratta> getPercorrenze() { return percorrenze; }
    public void setPercorrenze(List<PercorrenzaTratta> percorrenze) { this.percorrenze = percorrenze; }

    public void addPercorrenza(PercorrenzaTratta percorrenza) {
        percorrenze.add(percorrenza);
        percorrenza.setTratta(this);
    }

    public void removePercorrenza(PercorrenzaTratta percorrenza) {
        percorrenze.remove(percorrenza);
        percorrenza.setTratta(null);
    }

    @Override
    public String toString() {
        return "Tratta{" +
                "id=" + id +
                ", zonaPartenza='" + zonaPartenza + '\'' +
                ", capolinea='" + capolinea + '\'' +
                ", tempoPrevistoMinuti=" + tempoPrevistoMinuti +
                '}';
    }
}
