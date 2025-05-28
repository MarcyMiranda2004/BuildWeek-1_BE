package dao;

import entities.Mezzo;
import jakarta.persistence.EntityManager;

public class MezzoDao {
    private EntityManager em;

    public MezzoDao(EntityManager em) {
        this.em = em;
    }

    public void save(Mezzo mezzo) {
        em.getTransaction().begin();
        em.persist(mezzo);
        em.getTransaction().commit();
    }

    public Mezzo findById(Long id) {
        return em.find(Mezzo.class, id);
    }


}

// Periodo di servizio e periodo di manutenzione di ogni mezzo
// Numero di volte che un mezzo percorre una tratta