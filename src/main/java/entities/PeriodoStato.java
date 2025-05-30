package entities;

import enums.StatoMezzo;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity(name = "periodo_stati")
public class PeriodoStato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "mezzo_id", nullable = false)
    private Mezzo mezzo;

    @Column(nullable = false)
    private LocalDateTime dataInizio;

    private LocalDateTime dataFine;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoMezzo stato;

    public PeriodoStato() {}

    public PeriodoStato(Long id, Mezzo mezzo, LocalDateTime dataInizio, LocalDateTime dataFine, StatoMezzo stato) {
        this.id = id;
        this.mezzo = mezzo;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.stato = stato;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Mezzo getMezzo() { return mezzo; }
    public void setMezzo(Mezzo mezzo) { this.mezzo = mezzo; }

    public LocalDateTime getDataInizio() { return dataInizio; }
    public void setDataInizio(LocalDateTime dataInizio) { this.dataInizio = dataInizio; }

    public LocalDateTime getDataFine() { return dataFine; }
    public void setDataFine(LocalDateTime dataFine) { this.dataFine = dataFine; }

    public StatoMezzo getStato() { return stato; }
    public void setStato(StatoMezzo stato) { this.stato = stato; }

    /*@Override
    public String toString() {
        return "PeriodoStato{" +
                "id=" + id +
                ", mezzoId=" + (mezzo != null ? mezzo.getId() : null) +
                ", dataInizio=" + dataInizio +
                ", dataFine=" + dataFine +
                ", stato=" + stato +
                '}';
    }*/
    @Override
    public String toString() {
        return "PeriodoStato{" +
                "Il mezzo con id " + id +

                " dal " + dataInizio +
                " al " + dataFine +
                " era in stato di: " + stato +
                '}';
    }
}
