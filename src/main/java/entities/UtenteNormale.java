// Classe UtenteNormale aggiornata
package entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import java.util.ArrayList;
import java.util.List;

@Entity
public class UtenteNormale extends Utente {

    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Abbonamento> abbonamenti = new ArrayList<>();

    public UtenteNormale() {}

    public UtenteNormale(String nome, String cognome, String username, String password) {
        super(nome, cognome, username, password);
    }

    public List<Abbonamento> getAbbonamenti() { return abbonamenti; }

    public void setAbbonamenti(List<Abbonamento> abbonamenti) { this.abbonamenti = abbonamenti; }

    public void addAbbonamento(Abbonamento abbonamento) {
        abbonamenti.add(abbonamento);
        abbonamento.setUtente(this);
    }

    public void removeAbbonamento(Abbonamento abbonamento) {
        abbonamenti.remove(abbonamento);
        abbonamento.setUtente(null);
    }

    @Override
    public String toString() {
        return "UtenteNormale{" + super.toString() + ", abbonamenti=" + abbonamenti.size() + '}';
    }
}