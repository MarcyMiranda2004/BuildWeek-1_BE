package dao;

import entities.Utente;
import jakarta.persistence.EntityManager;

import java.util.List;

public class UtenteDao {
    private EntityManager em;

    public UtenteDao(EntityManager em) {
        this.em = em;
    }

    public void save(Utente utente) {
        em.getTransaction().begin();
        em.persist(utente);
        em.getTransaction().commit();
    }

    public Utente findById(Long id) {
        return em.find(Utente.class, id);
    }

    public List<Utente> findAll() {
        return em.createQuery("FROM Utente", Utente.class).getResultList();
    }

    public void delete(Utente utente) {
        em.getTransaction().begin();
        em.remove(em.contains(utente) ? utente : em.merge(utente));
        em.getTransaction().commit();
    }
}

