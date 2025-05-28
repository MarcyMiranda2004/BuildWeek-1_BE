package dao;

import entities.TitoloViaggio;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;

public class TitoloViaggioDao {
    private static EntityManager em;

    public TitoloViaggioDao (EntityManager em) {
        this.em = em;
    }

    public void save(TitoloViaggio  titoloViaggio) {
        em.getTransaction().begin();
        em.persist(titoloViaggio);
        em.getTransaction().commit();
    }

    public TitoloViaggio  findById(Long id) {
        return em.find(TitoloViaggio .class, id);
    }

    // 1. Biglietti e abbonamenti rilasciati da DistributoriAutomatizzati
    public long countTitoliDaDistributori() {
        return em.createQuery(
                "SELECT COUNT(t) FROM TitoloViaggio t WHERE TYPE(t.puntoVendita) = DistributoreAutomatizzato",
                Long.class
        ).getSingleResult();
    }

    // 2. Biglietti e abbonamenti rilasciati da RivenditoriAutorizzati
    public long countTitoliDaRivenditori() {
        return em.createQuery(
                "SELECT COUNT(t) FROM TitoloViaggio t WHERE TYPE(t.puntoVendita) = RivenditoreAutorizzato",
                Long.class
        ).getSingleResult();
    }

    // 3. Biglietti e abbonamenti rilasciati in un intervallo di tempo
    public static long countTitoliInPeriodo(LocalDate start, LocalDate end) {
        return em.createQuery(
                        "SELECT COUNT(t) FROM TitoloViaggio t WHERE t.dataEmissione BETWEEN :start AND :end",
                        Long.class
                ).setParameter("start", start)
                .setParameter("end", end)
                .getSingleResult();
    }

    // 4. Totale di biglietti e abbonamenti rilasciati
    public long countTotaleTitoli() {
        return em.createQuery("SELECT COUNT(t) FROM TitoloViaggio t", Long.class)
                .getSingleResult();
    }
}
