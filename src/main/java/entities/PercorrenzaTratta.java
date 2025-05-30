package entities;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity(name = "percorrenza_tratte")
@Table(name = "percorrenza_tratte")
public class PercorrenzaTratta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "mezzo_id", nullable = false)
    private Mezzo mezzo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tratta_id", nullable = false)
    private Tratta tratta;

    @Column(nullable = false)
    private LocalDate data;

    @Column(name = "tempo_effettivo_minuti", nullable = false)
    private int tempoEffettivoMinuti;

    public PercorrenzaTratta() {}

    public PercorrenzaTratta(Mezzo mezzo, Tratta tratta, LocalDate data, int tempoEffettivoMinuti) {
        this.mezzo = mezzo;
        this.tratta = tratta;
        this.data = data;
        this.tempoEffettivoMinuti = tempoEffettivoMinuti;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Mezzo getMezzo() { return mezzo; }
    public void setMezzo(Mezzo mezzo) { this.mezzo = mezzo; }

    public Tratta getTratta() { return tratta; }
    public void setTratta(Tratta tratta) { this.tratta = tratta; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public int getTempoEffettivoMinuti() { return tempoEffettivoMinuti; }
    public void setTempoEffettivoMinuti(int tempoEffettivoMinuti) { this.tempoEffettivoMinuti = tempoEffettivoMinuti; }

    @Override
    public String toString() {
        String tipoMezzo = (mezzo != null && mezzo.getTipo() != null) ? mezzo.getTipo().name() : "N/A";
        String trattaStr = (tratta != null) ? tratta.getZonaPartenza() + " - " + tratta.getCapolinea() : "N/A";
        return "PercorrenzaTratta{" +
                "id=" + id +
                ", tipoMezzo=" + tipoMezzo +
                ", tratta=" + trattaStr +
                ", data=" + data +
                ", tempoEffettivoMinuti=" + tempoEffettivoMinuti +
                '}';
    }

}
