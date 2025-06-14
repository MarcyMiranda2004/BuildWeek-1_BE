package dao;

import entities.Mezzo;
import entities.PercorrenzaTratta;
import entities.Tratta;
import jakarta.persistence.EntityManager;

import java.util.List;

public class PercorrenzaTrattaDao {
    private EntityManager em;

    public PercorrenzaTrattaDao(EntityManager em) {
        this.em = em;
    }

    public void save(PercorrenzaTratta percorrenza) {
        em.getTransaction().begin();
        em.persist(percorrenza);
        em.getTransaction().commit();
    }

    public PercorrenzaTratta findById(Long id) {
        return em.find(PercorrenzaTratta.class, id);
    }

    public List<PercorrenzaTratta> findAll() {
        return em.createQuery("SELECT p FROM percorrenza_tratte p", PercorrenzaTratta.class)
                .getResultList();
    }

    public List<Tratta> findAllTratteDistinct() {
        return em.createQuery("SELECT DISTINCT p.tratta FROM percorrenza_tratte p", Tratta.class).getResultList();
    }

    public Tratta findTrattaByPartenzaECapolinea(String zonaPartenza, String capolinea) {
        return em.createQuery("""
            SELECT t FROM Tratta t
            WHERE t.zonaPartenza = :zona AND t.capolinea = :cap
        """, Tratta.class)
                .setParameter("zona", zonaPartenza)
                .setParameter("cap", capolinea)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public List<Tratta> findAllTratte() {
        return em.createQuery("SELECT DISTINCT p.tratta FROM percorrenza_tratte p", Tratta.class)
                .getResultList();
    }

    public class PercorrenzaTrattaDAO {
        public List<Mezzo> findMezziByTratta(EntityManager em, Tratta tratta) {
            return em.createQuery("SELECT DISTINCT p.mezzo FROM PercorrenzaTratta p WHERE p.tratta = :tratta", Mezzo.class)
                    .setParameter("tratta", tratta)
                    .getResultList();
        }
    }

    public List<Mezzo> findMezziByTratta(EntityManager em, Tratta tratta) {
        return em.createQuery("SELECT DISTINCT p.mezzo FROM percorrenza_tratte p WHERE p.tratta = :tratta", Mezzo.class)
                .setParameter("tratta", tratta)
                .getResultList();
    }


}
