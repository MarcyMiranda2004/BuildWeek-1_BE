package entities;

import jakarta.persistence.*;

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
