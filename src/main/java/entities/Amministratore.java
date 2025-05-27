// Classe Amministratore aggiornata
package entities;

import jakarta.persistence.Entity;

@Entity
public class Amministratore extends Utente {

    public Amministratore() {}

    public Amministratore(String nome, String cognome, String username, String password) {
        super(nome, cognome, username, password);
    }

    @Override
    public String toString() {
        return "Amministratore{" +
                "nome='" + getNome() + '\'' +
                ", cognome='" + getCognome() + '\'' +
                ", username='" + getUsername() + '\'' +
                '}';
    }
}
