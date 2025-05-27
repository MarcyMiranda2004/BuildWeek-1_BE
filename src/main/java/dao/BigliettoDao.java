package dao;

import entities.Biglietto;
import jakarta.persistence.EntityManager;

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

    // Numero di biglietti vidimati: su un particolare mezzo(singolo mezzo), in un determinato periodo di tempo
}


