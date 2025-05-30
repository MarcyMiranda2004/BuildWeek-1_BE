package dao;

import entities.PuntoVendita;
import jakarta.persistence.EntityManager;

import java.util.List;

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

    public List<PuntoVendita> findAll(EntityManager em) {
        return em.createQuery("SELECT p FROM punti_vendita p", PuntoVendita.class).getResultList();
    }
}

