package dao;

import entities.Tessera;
import entities.Utente;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;

public class UtenteDao {
    private EntityManager em;

    public UtenteDao(EntityManager em) {
        this.em = em;
    }

    public void save(Utente utente) {
        em.getTransaction().begin();

        em.persist(utente);

        // Creazione automatica della tessera associata
        Tessera tessera = new Tessera(
                LocalDate.now(),
                LocalDate.now().plusYears(1),
                utente
        );
        em.persist(tessera);

        em.getTransaction().commit();

        System.out.println("Utente e tessera creati. Tessera ID: " + tessera.getId());
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
