package dao;

import entities.Abbonamento;
import entities.Tessera;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.util.List;

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

    public Tessera findByUtenteId(Long utenteId) {
        return em.createQuery("""
                SELECT t FROM tessere t WHERE t.utente.id = :uid
            """, Tessera.class)
                .setParameter("uid", utenteId)
                .getSingleResult();
    }

    public List<Tessera> findAll() {
        return em.createQuery("SELECT t FROM tessere t", Tessera.class).getResultList();
    }

    public Tessera findByCodice(String codice) {
        try {
            TypedQuery<Tessera> query = em.createQuery(
                    "SELECT t FROM tessere t WHERE t.codice = :codice", Tessera.class);
            query.setParameter("codice", codice);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


    public void rinnovaTessera(Long id) {
        em.getTransaction().begin();
        Tessera tessera = em.find(Tessera.class, id);

        if (tessera != null) {
            if (tessera.getDataScadenza().isBefore(LocalDate.now())) {
                LocalDate nuovaScadenza = LocalDate.now().plusYears(1);
                tessera.setDataScadenza(nuovaScadenza);
                System.out.println("Tessera rinnovata fino al: " + nuovaScadenza);
            } else {
                System.out.println("Tessera non scaduta. Scadenza attuale: " + tessera.getDataScadenza());
            }
        } else {
            System.out.println("Tessera non trovata con ID: " + id);
        }

        em.getTransaction().commit();
    }

    public void aggiungiAbbonamento(Long tesseraId, Abbonamento abbonamento) {
        em.getTransaction().begin();
        Tessera tessera = em.find(Tessera.class, tesseraId);
        if (tessera != null) {
            tessera.addAbbonamento(abbonamento);
            em.persist(abbonamento);
        } else {
            System.out.println("Tessera non trovata per ID: " + tesseraId);
        }
        em.getTransaction().commit();
    }


}
