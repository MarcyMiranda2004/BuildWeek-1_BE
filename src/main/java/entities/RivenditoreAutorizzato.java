package entities;

import jakarta.persistence.Entity;

@Entity(name = "rivenditori_autorizzati")
public class RivenditoreAutorizzato extends PuntoVendita {

    public RivenditoreAutorizzato() {}

    public RivenditoreAutorizzato(String nome, String indirizzo) {
        super(nome, indirizzo);
    }

    @Override
    public String toString() {
        return "RivenditoreAutorizzato{" + super.toString() + "}";
    }
}
