package dao;

import entities.Tessera;
import jakarta.persistence.EntityManager;

public class TesseraDao {
    private EntityManager em;

    public TesseraDao(EntityManager em) {
        this.em = em;
    }

    public void save(Tessera tessera) {
        em.getTransaction().begin();
        em.persist(tessera);
        em.getTransaction().commit();
    }

    public Tessera findById(Long id) {
        return em.find(Tessera.class, id);
    }

    // Metodo per il rinnovo della tessera dopo un anno
}

