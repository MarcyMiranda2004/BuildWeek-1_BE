package dao;

import entities.Abbonamento;
import jakarta.persistence.EntityManager;

public class AbbonamentoDao {
    private EntityManager em;

    public AbbonamentoDao(EntityManager em) {
        this.em = em;
    }

    public void save(Abbonamento abbonamento) {
        em.getTransaction().begin();
        em.persist(abbonamento);
        em.getTransaction().commit();
    }

    public Abbonamento findById(Long id) {
        return em.find(Abbonamento.class, id);
    }


}

