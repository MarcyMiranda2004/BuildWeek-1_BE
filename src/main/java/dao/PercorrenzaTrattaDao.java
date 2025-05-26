package dao;

import entities.PercorrenzaTratta;
import jakarta.persistence.EntityManager;

public class PercorrenzaTrattaDao {
    private EntityManager em;

    public PercorrenzaTrattaDao(EntityManager em) {
        this.em = em;
    }

    public void save(PercorrenzaTratta percorrenza) {
        em.getTransaction().begin();
        em.persist(percorrenza);
        em.getTransaction().commit();
    }

    public PercorrenzaTratta findById(Long id) {
        return em.find(PercorrenzaTratta.class, id);
    }
}

