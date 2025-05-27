package entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Utente")
@Inheritance(strategy = InheritanceType.JOINED)
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String cognome;

    @Column(nullable = false, length = 20)
    private String username;

    @Column(nullable = false, length = 20)
    private String password;

    public Utente() {
    }

    public Utente(String nome, String cognome, String username, String password) {
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.password = password;
    }

    public Long getId() {return id;}
    public String getNome() {return nome;}

    public void setNome(String nome) {this.nome = nome;}
    public String getCognome() {return cognome;}

    public void setCognome(String cognome) {this.cognome = cognome;}
    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}
    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    @Override
    public String toString() {
        return "Utente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
