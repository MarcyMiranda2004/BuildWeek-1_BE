package dao;

import entities.TitoloViaggio;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.time.LocalDate;

public class TitoloViaggioDao {
    private EntityManager em;

    public TitoloViaggioDao(EntityManager em) {
        this.em = em;
    }

    public void save(TitoloViaggio titoloViaggio) {
        em.getTransaction().begin();
        em.persist(titoloViaggio);
        em.getTransaction().commit();
    }

    public TitoloViaggio findById(Long id) {
        return em.find(TitoloViaggio.class, id);
    }

    public long countTitoliDaDistributori() {
        return em.createQuery(
                "SELECT COUNT(t) FROM TitoloViaggio t WHERE TYPE(t.puntoVendita) = DistributoreAutomatizzato",
                Long.class
        ).getSingleResult();
    }

    public long countTitoliDaRivenditori() {
        return em.createQuery(
                "SELECT COUNT(t) FROM TitoloViaggio t WHERE TYPE(t.puntoVendita) = RivenditoreAutorizzato",
                Long.class
        ).getSingleResult();
    }

    public long countTitoliInPeriodo(LocalDate start, LocalDate end) {
        return em.createQuery(
                        "SELECT COUNT(t) FROM TitoloViaggio t WHERE t.dataEmissione BETWEEN :start AND :end",
                        Long.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .getSingleResult();
    }

    public long countTotaleTitoli() {
        return em.createQuery("SELECT COUNT(t) FROM TitoloViaggio t", Long.class)
                .getSingleResult();
    }

    public long countTitoliDaDistributoriInPeriodo(LocalDate inizio, LocalDate fine) {
        return em.createQuery(
                        "SELECT COUNT(t) FROM TitoloViaggio t WHERE t.emettitore.tipo = 'DISTRIBUTORE_AUTOMATICO' AND t.dataEmissione BETWEEN :inizio AND :fine",
                        Long.class)
                .setParameter("inizio", inizio)
                .setParameter("fine", fine)
                .getSingleResult();
    }

    public long countTitoliDaRivenditoriInPeriodo(LocalDate inizio, LocalDate fine) {
        return em.createQuery(
                        "SELECT COUNT(t) FROM TitoloViaggio t WHERE t.emettitore.tipo = 'RIVENDITORE_AUTORIZZATO' AND t.dataEmissione BETWEEN :inizio AND :fine",
                        Long.class)
                .setParameter("inizio", inizio)
                .setParameter("fine", fine)
                .getSingleResult();
    }

    public long countBigliettiVendutiInPeriodo(LocalDate from, LocalDate to) {
        return em.createQuery("SELECT COUNT(b) FROM Biglietto b WHERE b.dataEmissione BETWEEN :start AND :end", Long.class)
                .setParameter("start", from)
                .setParameter("end", to)
                .getSingleResult();
    }

    public long countAbbonamentiVendutiInPeriodo(LocalDate from, LocalDate to) {
        return em.createQuery("SELECT COUNT(a) FROM Abbonamento a WHERE a.dataInizio BETWEEN :start AND :end", Long.class)
                .setParameter("start", from)
                .setParameter("end", to)
                .getSingleResult();
    }

    public List<TitoloViaggio> findByPeriodo(LocalDateTime start, LocalDateTime end) {
        return em.createQuery(
                        "SELECT t FROM TitoloViaggio t WHERE t.dataEmissione BETWEEN :start AND :end", TitoloViaggio.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }
}
