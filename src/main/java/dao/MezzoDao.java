package dao;

import entities.Mezzo;
import entities.PeriodoStato;
import entities.Tratta;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
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

    // Periodo di servizio e periodo di manutenzione di ogni mezzo: il metodo mi ritornerà una lista di oggetti periodoStato,
    // passandogli un mezzo specifico e un intervallo di tempo
    public List<PeriodoStato> verificaStatoMezzo(Mezzo mezzo, LocalDateTime inizio, LocalDateTime fine) {
        List<PeriodoStato> statiNelPeriodo = new ArrayList<>(); //Lista vuota che andrà a contenere i vari periodi presenti nell'intervallo indicato

        //Ciclo i periodoStato di quel mezzo specifico
        for (PeriodoStato periodo : mezzo.getStoricoStati()) {
            LocalDateTime start = periodo.getDataInizio();
            LocalDateTime end = periodo.getDataFine() != null ? periodo.getDataFine() : LocalDateTime.now();

            // Verifica se i periodoStato si sovrappongono all'intervallo che l'utente inserirà
            if (!start.isAfter(fine) && !end.isBefore(inizio)) {
                statiNelPeriodo.add(periodo);
            }
        }
        return statiNelPeriodo;


    }

    // Numero di volte che un mezzo percorre una tratta
    public long countPercorrenzeByMezzoAndTratta(Mezzo mezzo, Tratta tratta) {
        return em.createQuery(
                        "SELECT COUNT(p) FROM PercorrenzaTratta p WHERE p.mezzo = :mezzo AND p.tratta = :tratta", Long.class)
                .setParameter("mezzo", mezzo)
                .setParameter("tratta", tratta)
                .getSingleResult();
    }

    public static void contaPercorrenzeTrattaPerMezzo(EntityManager em, Mezzo mezzo, String zonaPartenza, String capolinea) {
        Tratta tratta = em.createQuery(
                        "SELECT t FROM Tratta t WHERE t.zonaPartenza = :zp AND t.capolinea = :cl", Tratta.class)
                .setParameter("zp", zonaPartenza)
                .setParameter("cl", capolinea)
                .getResultStream()
                .findFirst()
                .orElse(null);

        if (tratta == null) {
            System.out.println("Tratta non trovata.");
            return;
        }

        MezzoDao mezzoDao = new MezzoDao(em);
        long count = mezzoDao.countPercorrenzeByMezzoAndTratta(mezzo, tratta);

        System.out.println("Il mezzo con ID " + mezzo.getId() +
                " ha percorso la tratta '" + zonaPartenza + " - " + capolinea + "' per " + count + " volte.");
    }
}

