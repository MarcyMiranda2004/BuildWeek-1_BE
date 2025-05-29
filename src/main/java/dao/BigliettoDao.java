package dao;

import entities.Biglietto;
import entities.Mezzo;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;

public class BigliettoDao {
    private EntityManager em;

    public BigliettoDao(EntityManager em) {
        this.em = em;
    }

    public void save(Biglietto biglietto) {
        em.getTransaction().begin();
        em.persist(biglietto);
        em.getTransaction().commit();
    }

    public Biglietto findById(Long id) {
        return em.find(Biglietto.class, id);
    }

    // Metodo per contare i biglietti validati su un mezzo in un intervallo di tempo
    public long countValidatiByMezzoAndPeriodo(Mezzo mezzo, LocalDateTime inizio, LocalDateTime fine) {
        return em.createQuery(
                        "SELECT COUNT(b) FROM Biglietto b WHERE b.mezzoValidato = :mezzo AND b.validato = true AND b.dataValidazione BETWEEN :inizio AND :fine",
                        Long.class)
                .setParameter("mezzo", mezzo)
                .setParameter("inizio", inizio)
                .setParameter("fine", fine)
                .getSingleResult();
    }

}
