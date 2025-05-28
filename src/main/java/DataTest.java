import entities.*;
import enums.StatoMezzo;
import enums.TipoMezzo;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;

public class DataTest {
    public static void inserisciDatiDiTest(EntityManager em) {
        em.getTransaction().begin();

        // Mezzi
        Mezzo bus1 = new Mezzo(TipoMezzo.AUTOBUS, 50, StatoMezzo.IN_SERVIZIO);
        Mezzo tram1 = new Mezzo(TipoMezzo.TRAM, 80, StatoMezzo.IN_SERVIZIO);
        em.persist(bus1);
        em.persist(tram1);

        // Tratte
        Tratta trattaA = new Tratta("Napoli", "Roma", 150);
        Tratta trattaB = new Tratta("Milano", "Torino", 150);
        em.persist(trattaA);
        em.persist(trattaB);

        // Percorrenze
        PercorrenzaTratta p1 = new PercorrenzaTratta(bus1, trattaA, LocalDate.now(), 155);
        PercorrenzaTratta p2 = new PercorrenzaTratta(tram1, trattaB, LocalDate.now(), 145);
        bus1.addPercorrenzaTratta(p1);
        trattaA.addPercorrenza(p1);
        tram1.addPercorrenzaTratta(p2);
        trattaB.addPercorrenza(p2);
        em.persist(p1);
        em.persist(p2);

        // Utente
        UtenteNormale utente1 = new UtenteNormale("Mario", "Rossi", "mario", "1234");
        em.persist(utente1);

        // Punti vendita
        RivenditoreAutorizzato rivenditore1 = new RivenditoreAutorizzato("Bar Mario", "Via Roma 10");
        DistributoreAutomatizzato distributore1 = new DistributoreAutomatizzato("Distributore A1", "Stazione Centrale", true);
        em.persist(rivenditore1);
        em.persist(distributore1);

        em.getTransaction().commit();
    }
}
