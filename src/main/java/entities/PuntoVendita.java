package entities;

import jakarta.persistence.*;

import java.util.List;

@Entity(name = "punti_vendita")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class PuntoVendita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String indirizzo;

    @OneToMany
    private List<TitoloViaggio> titoliViaggio;

    public PuntoVendita() {}

    public PuntoVendita(String nome, String indirizzo) {
        this.nome = nome;
        this.indirizzo = indirizzo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getIndirizzo() { return indirizzo; }
    public void setIndirizzo(String indirizzo) { this.indirizzo = indirizzo; }

    @Override
    public String toString() {
        return "PuntoVendita{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", indirizzo='" + indirizzo + '\'' +
                '}';
    }
}
