
import dao.MezzoDao;
import dao.TrattaDao;
import entities.*;
import enums.StatoMezzo;
import enums.TipoMezzo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Scanner;

public class MainAnna {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Name_BuildWeek_1");
        EntityManager em = emf.createEntityManager();
        Scanner scanner = new Scanner(System.in);
        MezzoDao mezzoDao = new MezzoDao(em);

        try {
            // CREO I DATI INIZIALI
            em.getTransaction().begin();

            Amministratore admin1 = new Amministratore("Mario", "Rossi", "admin1", "p1");
            Amministratore admin2 = new Amministratore("Carlo", "Bianchi", "admin2", "p2");
            Amministratore admin3 = new Amministratore("Dario", "Verdi", "admin3", "p3");
            Amministratore admin4 = new Amministratore("Topo", "Gigio", "admin4", "p4");
            em.persist(admin1);
            em.persist(admin2);
            em.persist(admin3);
            em.persist(admin4);

            Tratta tratta1 = new Tratta("Centro", "Stazione", 25);
            Tratta tratta2 = new Tratta("Milano", "Roma", 190);
            Tratta tratta3 = new Tratta("Roma", "Napoli", 55);
            Tratta tratta4 = new Tratta("Cagliari", "Sassari", 120);
            em.persist(tratta1);
            em.persist(tratta2);
            em.persist(tratta3);
            em.persist(tratta4);

            Mezzo mezzo = new Mezzo();
            mezzo.setTipo(TipoMezzo.AUTOBUS);
            mezzo.setCapienza(50);
            mezzo.setStato(StatoMezzo.IN_SERVIZIO);//Stato attuale
            em.persist(mezzo);

            Mezzo mezzo2 = new Mezzo();
            mezzo2.setTipo(TipoMezzo.AUTOBUS);
            mezzo2.setCapienza(50);
            mezzo2.setStato(StatoMezzo.IN_MANUTENZIONE);//Stato attuale
            em.persist(mezzo2);

            //Creo dei periodi per lo Stato che poi andrò ad assocciare al mezzo
            PeriodoStato p1 = new PeriodoStato(null, mezzo,
                    LocalDateTime.of(2025, 5, 1, 0, 0),
                    LocalDateTime.of(2025, 5, 5, 23, 59),
                    StatoMezzo.IN_SERVIZIO);

            PeriodoStato p2 = new PeriodoStato(null, mezzo,
                    LocalDateTime.of(2025, 5, 6, 0, 0),
                    LocalDateTime.of(2025, 5, 10, 23, 59),
                    StatoMezzo.IN_MANUTENZIONE);

            PeriodoStato p3 = new PeriodoStato(null, mezzo,
                    LocalDateTime.of(2025, 5, 11, 0, 0),
                    null, // ancora in corso
                    StatoMezzo.IN_SERVIZIO);

            // Associo i periodi al mezzo
            mezzo.addPeriodoStato(p1);
            mezzo.addPeriodoStato(p2);
            mezzo.addPeriodoStato(p3);

            //Creo dei periodi per lo Stato che poi andrò ad assocciare al mezzo2
            PeriodoStato p4 = new PeriodoStato(null, mezzo2,
                    LocalDateTime.of(2025, 5, 1, 0, 0),
                    LocalDateTime.of(2025, 5, 5, 23, 59),
                    StatoMezzo.IN_MANUTENZIONE);

            PeriodoStato p5 = new PeriodoStato(null, mezzo,
                    LocalDateTime.of(2025, 5, 6, 0, 0),
                    LocalDateTime.of(2025, 5, 10, 23, 59),
                    StatoMezzo.IN_SERVIZIO);

            PeriodoStato p6 = new PeriodoStato(null, mezzo,
                    LocalDateTime.of(2025, 5, 11, 0, 0),
                    null, // ancora in corso
                    StatoMezzo.IN_MANUTENZIONE);

            // Associo i periodi al mezzo
            mezzo2.addPeriodoStato(p4);
            mezzo2.addPeriodoStato(p5);
            mezzo2.addPeriodoStato(p6);

            // Percorrenze per tratta
            em.persist(new PercorrenzaTratta( mezzo, tratta1, LocalDate.now().minusDays(2), 27));
            em.persist(new PercorrenzaTratta( mezzo, tratta1, LocalDate.now().minusDays(1), 24));
            em.persist(new PercorrenzaTratta( mezzo, tratta1, LocalDate.now(), 26));

            em.persist(new PercorrenzaTratta( mezzo, tratta2, LocalDate.now().minusDays(2), 200));
            em.persist(new PercorrenzaTratta( mezzo, tratta2, LocalDate.now().minusDays(1), 210));
            em.persist(new PercorrenzaTratta( mezzo, tratta2, LocalDate.now(), 190));

            em.persist(new PercorrenzaTratta( mezzo, tratta3, LocalDate.now().minusDays(2), 65));
            em.persist(new PercorrenzaTratta( mezzo, tratta3, LocalDate.now().minusDays(1), 50));
            em.persist(new PercorrenzaTratta( mezzo, tratta3, LocalDate.now(), 60));

            em.persist(new PercorrenzaTratta( mezzo, tratta4, LocalDate.now().minusDays(2), 150));
            em.persist(new PercorrenzaTratta( mezzo, tratta4, LocalDate.now().minusDays(1), 130));
            em.persist(new PercorrenzaTratta( mezzo, tratta4, LocalDate.now(), 110));

            em.getTransaction().commit();
            System.out.println("Dati iniziali creati con successo.");
/*
            System.out.println("Scegli un'operazione:");
            System.out.println("1 - Calcola tempi medi per tratta (richiede autenticazione)");
            System.out.println("2 - Calcola numero percorrenze di un mezzo su una tratta");

            System.out.print("Inserisci scelta (1 o 2): ");
            int scelta = Integer.parseInt(scanner.nextLine());
*/
            boolean running = true;

            while (running) {
                System.out.println("\n Scegli cosa vuoi fare:");
                System.out.println("1. Tempi effettivi di percorrenza di una tratta e media dei tempi (richiede autenticazione)");
                System.out.println("2. Scopri quante volte un mezzo ha percorso una determinata tratta");
                System.out.println("3. Verifica stato di un mezzo in un intervallo di tempo");
                System.out.println("0. Esci");
                System.out.print("Scelta: ");
                int scelta = scanner.nextInt();
                scanner.nextLine();

                switch (scelta) {
                    case 1 -> {
                        // Autenticazione
                        System.out.print("Inserisci username amministratore: ");
                        String username = scanner.nextLine();

                        System.out.print("Inserisci password: ");
                        String password = scanner.nextLine();

                        Amministratore admin = em.createQuery(
                                        "SELECT a FROM Amministratore a WHERE a.username = :user AND a.password = :pass", Amministratore.class)
                                .setParameter("user", username)
                                .setParameter("pass", password)
                                .getResultStream()
                                .findFirst()
                                .orElse(null);

                        if (admin == null) {
                            System.out.println("Accesso negato.");
                            return;
                        }
                        System.out.println("Accesso amministratore riuscito.");

                        // Dati tratta
                        System.out.print("Inserisci zona di partenza della tratta: ");
                        String zonaPartenza = scanner.nextLine();
                        System.out.print("Inserisci capolinea della tratta: ");
                        String capolinea = scanner.nextLine();

                        TrattaDao trattaDAO = new TrattaDao(em);
                        List<Integer> tempi = trattaDAO.getTempiEffettiviByTratta(zonaPartenza, capolinea);

                        if (tempi.isEmpty()) {
                            System.out.println("Nessuna percorrenza trovata per la tratta specificata.");
                        } else {
                            System.out.println("Tempi effettivi di percorrenza trovati:");
                            tempi.forEach(t -> System.out.println("- " + t + " minuti"));

                            OptionalDouble media = tempi.stream().mapToInt(Integer::intValue).average();
                            media.ifPresent(m -> System.out.printf("Tempo medio: %.2f minuti%n", m));
                        }
                    }
                    case 2 -> {
                        // Inserisci ID mezzo
                        System.out.print("Inserisci l'ID del mezzo: ");
                        Long mezzoId = Long.parseLong(scanner.nextLine());

                        mezzo = em.find(Mezzo.class, mezzoId);
                        if (mezzo == null) {
                            System.out.println("Mezzo non trovato.");
                            return;
                        }

                        // Dati tratta
                        System.out.print("Inserisci zona di partenza della tratta: ");
                        String zonaPartenza = scanner.nextLine();
                        System.out.print("Inserisci capolinea della tratta: ");
                        String capolinea = scanner.nextLine();

                        Tratta tratta = em.createQuery("""
                                        SELECT t FROM Tratta t
                                        WHERE t.zonaPartenza = :zona AND t.capolinea = :cap
                                        """, Tratta.class)
                                .setParameter("zona", zonaPartenza)
                                .setParameter("cap", capolinea)
                                .getResultStream()
                                .findFirst()
                                .orElse(null);

                        if (tratta == null) {
                            System.out.println("Tratta non trovata.");
                            return;
                        }

                        mezzoDao = new MezzoDao(em);
                        long count = mezzoDao.countPercorrenzeByMezzoAndTratta(mezzo, tratta);
                        System.out.println("Il mezzo ha percorso la tratta " + count + " volte.");
                    }

                    case 3 ->{
                        System.out.print("Inserisci ID del mezzo: ");
                        Long idMezzo = scanner.nextLong();
                        scanner.nextLine();

                        mezzo = mezzoDao.findById(idMezzo);
                        if (mezzo == null) {
                            System.out.println("Mezzo non trovato.");
                            break;
                        }

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                        System.out.print("Inserisci data e ora di inizio (formato: yyyy-MM-dd HH:mm): ");
                        String inizioStr = scanner.nextLine();
                        LocalDateTime inizio = LocalDateTime.parse(inizioStr, formatter);

                        System.out.print("Inserisci data e ora di fine (formato: yyyy-MM-dd HH:mm): ");
                        String fineStr = scanner.nextLine();
                        LocalDateTime fine = LocalDateTime.parse(fineStr, formatter);

                        List<PeriodoStato> stati = mezzoDao.verificaStatoMezzo(mezzo, inizio, fine);

                        if (stati.isEmpty()) {
                            System.out.println("Nessuno stato registrato per questo mezzo nell'intervallo specificato.");
                        } else {
                            System.out.println("Stati nell'intervallo di tempo:");
                            for (PeriodoStato stato : stati) {
                                System.out.println(stato);
                            }
                        }

                    }
                    default -> System.out.println("Scelta non valida.");
                }
            }
        } catch (Exception e) {
            System.out.println("Errore: " + e.getMessage());
            e.printStackTrace();
        }
    }}




