package dao;

import entities.PeriodoStato;
import jakarta.persistence.EntityManager;

public class PeriodoStatoDao {
    private EntityManager em;

    public PeriodoStatoDao(EntityManager em) {
        this.em = em;
    }

    public void save(PeriodoStato stato) {
        em.getTransaction().begin();
        em.persist(stato);
        em.getTransaction().commit();
    }
}

