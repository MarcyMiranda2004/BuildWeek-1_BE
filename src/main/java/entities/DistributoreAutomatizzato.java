package entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity(name = "distributori_autorizzati")
public class DistributoreAutomatizzato extends PuntoVendita {

    @Column(name = "attivo", nullable = false)
    private boolean attivo;

    public DistributoreAutomatizzato() {}

    public DistributoreAutomatizzato(String nome, String indirizzo, boolean attivo) {
        super(nome, indirizzo);
        this.attivo = attivo;
    }

    public boolean isAttivo() {
        return attivo;
    }

    public void setAttivo(boolean attivo) {
        this.attivo = attivo;
    }

    @Override
    public String toString() {
        return "DistributoreAutomatizzato{" +
                "attivo=" + attivo +
                "} " + super.toString();
    }
}
