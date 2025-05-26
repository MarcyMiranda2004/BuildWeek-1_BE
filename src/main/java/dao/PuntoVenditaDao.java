package dao;

import entities.PuntoVendita;
import jakarta.persistence.EntityManager;

public class PuntoVenditaDao {
    private EntityManager em;

    public PuntoVenditaDao(EntityManager em) {
        this.em = em;
    }

    public void save(PuntoVendita puntoVendita) {
        em.getTransaction().begin();
        em.persist(puntoVendita);
        em.getTransaction().commit();
    }

    public PuntoVendita findById(Long id) {
        return em.find(PuntoVendita.class, id);
    }
}

