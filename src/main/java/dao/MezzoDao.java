package dao;

import entities.Mezzo;
import entities.PeriodoStato;
import entities.Tratta;
import entities.PercorrenzaTratta;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MezzoDao {
    private EntityManager em;

    public MezzoDao(EntityManager em) {
        this.em = em;
    }

    public void save(Mezzo mezzo) {
        em.getTransaction().begin();
        em.persist(mezzo);
        em.getTransaction().commit();
    }

    public Mezzo findById(Long id) {
        return em.find(Mezzo.class, id);
    }

    public List<Mezzo> findAll() {return em.createQuery("SELECT m FROM mezzi m", Mezzo.class).getResultList();}

    // Metodo per ottenere i periodi di stato sovrapposti a un intervallo
    public List<PeriodoStato> verificaStatoMezzo(Mezzo mezzo, LocalDateTime inizio, LocalDateTime fine) {
        List<PeriodoStato> statiNelPeriodo = new ArrayList<>();

        for (PeriodoStato periodo : mezzo.getStoricoStati()) {
            LocalDateTime start = periodo.getDataInizio();
            LocalDateTime end = periodo.getDataFine() != null ? periodo.getDataFine() : LocalDateTime.now();

            if (!start.isAfter(fine) && !end.isBefore(inizio)) {
                statiNelPeriodo.add(periodo);
            }
        }
        return statiNelPeriodo;
    }

    // Conta quante volte un mezzo ha percorso una tratta specifica
    public long countPercorrenzeByMezzoAndTratta(Mezzo mezzo, Tratta tratta) {
        return em.createQuery(
                        "SELECT COUNT(p) FROM PercorrenzaTratta p WHERE p.mezzo = :mezzo AND p.tratta = :tratta", Long.class)
                .setParameter("mezzo", mezzo)
                .setParameter("tratta", tratta)
                .getSingleResult();
    }

    // Tempo medio percorrenza tratta
    public Double getTempoMedioPercorrenzaByMezzoAndTratta(Mezzo mezzo, Tratta tratta) {
        String jpql = "SELECT AVG(p.tempoEffettivoMinuti) FROM percorrenza_tratte p WHERE p.mezzo = :mezzo AND p.tratta = :tratta";
        return em.createQuery(jpql, Double.class)
                .setParameter("mezzo", mezzo)
                .setParameter("tratta", tratta)
                .getSingleResult();
    }

}
