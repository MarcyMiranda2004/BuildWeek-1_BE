package entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Amministratore extends Utente {

    @Column(length = 20, nullable = false)
    private String username;

    @Column(length = 20, nullable = false)
    private String password;

    public Amministratore() {}

    public Amministratore(String nome, String cognome, String username, String password) {
        super(nome, cognome);
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return "Amministratore{" +
                "nome='" + getNome() + '\'' +
                ", cognome='" + getCognome() + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
