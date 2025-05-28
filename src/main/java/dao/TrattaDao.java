package dao;

import entities.Tratta;
import jakarta.persistence.EntityManager;

import jakarta.persistence.TypedQuery;


import java.util.List;

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


    public List<Integer> getTempiEffettiviByTratta(String zonaPartenza, String capolinea) {
        String jpql = "SELECT p.tempoEffettivoMinuti FROM PercorrenzaTratta p " +
                "WHERE p.tratta.zonaPartenza = :zonaPartenza AND p.tratta.capolinea = :capolinea";

        TypedQuery<Integer> query = em.createQuery(jpql, Integer.class);
        query.setParameter("zonaPartenza", zonaPartenza);
        query.setParameter("capolinea", capolinea);

        return query.getResultList();
    }
}
