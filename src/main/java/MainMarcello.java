import entities.*;
import enums.StatoMezzo;
import enums.TipoMezzo;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Scanner;

public class MainMarcello {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Name_BuildWeek_1");
        EntityManager em = emf.createEntityManager();
        Scanner scanner = new Scanner(System.in);

        try {
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

            boolean running = true;

            while (running) {
                System.out.println("\n--- MENU ---");
                System.out.println("1. Crea biglietto");
                System.out.println("2. Valida biglietto");
                System.out.println("3. Statistiche biglietti validati su mezzo (ultimo mese)");
                System.out.println("0. Esci");
                System.out.print("Scegli un'opzione: ");
                int scelta = scanner.nextInt();
                scanner.nextLine();

                switch (scelta) {
                    case 1: {
                        List<Tratta> tratte = em.createQuery("SELECT t FROM Tratta t", Tratta.class).getResultList();
                        if (tratte.isEmpty()) {
                            System.out.println("Nessuna tratta disponibile.");
                            break;
                        }
                        System.out.println("Seleziona una tratta:");
                        for (int i = 0; i < tratte.size(); i++) {
                            System.out.printf("%d) %s\n", i + 1, tratte.get(i));
                        }
                        int idxTratta = scanner.nextInt() - 1;
                        scanner.nextLine();
                        if (idxTratta < 0 || idxTratta >= tratte.size()) {
                            System.out.println("Tratta non valida.");
                            break;
                        }
                        Tratta trattaSelezionata = tratte.get(idxTratta);

                        List<Mezzo> mezzi = em.createQuery(
                                        "SELECT DISTINCT p.mezzo FROM PercorrenzaTratta p WHERE p.tratta = :tratta", Mezzo.class)
                                .setParameter("tratta", trattaSelezionata)
                                .getResultList();
                        if (mezzi.isEmpty()) {
                            System.out.println("Nessun mezzo per questa tratta.");
                            break;
                        }
                        System.out.println("Seleziona un mezzo:");
                        for (int i = 0; i < mezzi.size(); i++) {
                            System.out.printf("%d) %s\n", i + 1, mezzi.get(i));
                        }
                        int idxMezzo = scanner.nextInt() - 1;
                        scanner.nextLine();
                        if (idxMezzo < 0 || idxMezzo >= mezzi.size()) {
                            System.out.println("Mezzo non valido.");
                            break;
                        }
                        Mezzo mezzoSelezionato = mezzi.get(idxMezzo);

                        List<PuntoVendita> puntiVendita = em.createQuery("SELECT p FROM PuntoVendita p", PuntoVendita.class).getResultList();
                        if (puntiVendita.isEmpty()) {
                            System.out.println("Nessun punto vendita disponibile.");
                            break;
                        }
                        System.out.println("Dove vuoi comprare il biglietto?");
                        for (int i = 0; i < puntiVendita.size(); i++) {
                            PuntoVendita pv = puntiVendita.get(i);
                            System.out.printf("%d) %s - Indirizzo: %s\n", i + 1, pv.getNome(), pv.getIndirizzo());
                        }
                        int idxPv = scanner.nextInt() - 1;
                        scanner.nextLine();
                        if (idxPv < 0 || idxPv >= puntiVendita.size()) {
                            System.out.println("Punto vendita non valido.");
                            break;
                        }
                        PuntoVendita puntoVenditaSelezionato = puntiVendita.get(idxPv);

                        Utente utente = em.createQuery("SELECT u FROM Utente u WHERE u.username = :username", Utente.class)
                                .setParameter("username", "mario")
                                .getSingleResult();

                        em.getTransaction().begin();
                        Biglietto biglietto = Biglietto.creaBiglietto(mezzoSelezionato, utente, puntoVenditaSelezionato);
                        em.persist(biglietto);
                        em.getTransaction().commit();

                        System.out.println("Biglietto creato con codice: " + biglietto.getCodice());
                        break;
                    }

                    case 2: {
                        Utente utente = em.createQuery("SELECT u FROM Utente u WHERE u.username = :username", Utente.class)
                                .setParameter("username", "mario")
                                .getSingleResult();

                        List<Biglietto> bigliettiNonValidati = em.createQuery(
                                        "SELECT b FROM Biglietto b WHERE b.utente = :utente AND b.validato = false", Biglietto.class)
                                .setParameter("utente", utente)
                                .getResultList();

                        if (bigliettiNonValidati.isEmpty()) {
                            System.out.println("Nessun biglietto da validare.");
                            break;
                        }

                        System.out.println("Seleziona il biglietto da validare:");
                        for (int i = 0; i < bigliettiNonValidati.size(); i++) {
                            Biglietto b = bigliettiNonValidati.get(i);
                            System.out.printf("%d) Codice: %s, Mezzo: %s, Data: %s\n",
                                    i + 1, b.getCodice(), b.getMezzoValidato(), b.getDataEmissione());
                        }
                        int idxBiglietto = scanner.nextInt() - 1;
                        scanner.nextLine();
                        if (idxBiglietto < 0 || idxBiglietto >= bigliettiNonValidati.size()) {
                            System.out.println("Biglietto non valido.");
                            break;
                        }
                        Biglietto daValidare = bigliettiNonValidati.get(idxBiglietto);

                        em.getTransaction().begin();
                        Biglietto.validaBiglietto(daValidare);
                        em.merge(daValidare);
                        em.getTransaction().commit();

                        System.out.println("Biglietto validato in data: " + daValidare.getDataValidazione());
                        break;
                    }

                    case 3: {
                        List<Mezzo> tuttiMezzi = em.createQuery("SELECT m FROM Mezzo m", Mezzo.class).getResultList();
                        if (tuttiMezzi.isEmpty()) {
                            System.out.println("Nessun mezzo presente.");
                            break;
                        }
                        System.out.println("Seleziona un mezzo:");
                        for (int i = 0; i < tuttiMezzi.size(); i++) {
                            System.out.printf("%d) %s\n", i + 1, tuttiMezzi.get(i));
                        }
                        int idxM = scanner.nextInt() - 1;
                        scanner.nextLine();
                        if (idxM < 0 || idxM >= tuttiMezzi.size()) {
                            System.out.println("Mezzo non valido.");
                            break;
                        }
                        Mezzo mezzoStat = tuttiMezzi.get(idxM);

                        LocalDateTime now = LocalDateTime.now();
                        LocalDateTime unMeseFa = now.minus(1, ChronoUnit.MONTHS);

                        Long numero = em.createQuery(
                                        "SELECT COUNT(b) FROM Biglietto b WHERE b.mezzoValidato = :mezzo AND b.validato = true AND b.dataValidazione BETWEEN :inizio AND :fine",
                                        Long.class)
                                .setParameter("mezzo", mezzoStat)
                                .setParameter("inizio", unMeseFa)
                                .setParameter("fine", now)
                                .getSingleResult();

                        System.out.println("Biglietti validati su " + mezzoStat + " nell'ultimo mese: " + numero);
                        break;
                    }

                    case 0:
                        running = false;
                        System.out.println("Arrivederci!");
                        break;

                    default:
                        System.out.println("Opzione non valida.");
                }
            }

        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
            scanner.close();
        }
    }
}
