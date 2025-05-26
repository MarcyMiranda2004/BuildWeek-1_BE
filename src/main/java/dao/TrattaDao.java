package dao;

import entities.Tratta;
import jakarta.persistence.EntityManager;

public class TrattaDao {
    private EntityManager em;

    public TrattaDao(EntityManager em) {
        this.em = em;
    }

    public void save(Tratta tratta) {
        em.getTransaction().begin();
        em.persist(tratta);
        em.getTransaction().commit();
    }

    public Tratta findById(Long id) {
        return em.find(Tratta.class, id);
    }
}

