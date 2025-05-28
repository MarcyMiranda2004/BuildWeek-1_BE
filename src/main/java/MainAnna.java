import dao.MezzoDao;
import dao.TrattaDao;
import entities.Amministratore;
import entities.Mezzo;
import entities.PercorrenzaTratta;
import entities.Tratta;
import enums.StatoMezzo;
import enums.TipoMezzo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Scanner;

public class MainAnna {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Name_BuildWeek_1");
        EntityManager em = emf.createEntityManager();
        Scanner scanner = new Scanner(System.in);

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
            mezzo.setStato(StatoMezzo.IN_SERVIZIO);
            em.persist(mezzo);

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

            System.out.println("Scegli un'operazione:");
            System.out.println("1 - Calcola tempi medi per tratta (richiede autenticazione)");
            System.out.println("2 - Calcola numero percorrenze di un mezzo su una tratta");

            System.out.print("Inserisci scelta (1 o 2): ");
            int scelta = Integer.parseInt(scanner.nextLine());

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

                    MezzoDao mezzoDao = new MezzoDao(em);
                    long count = mezzoDao.countPercorrenzeByMezzoAndTratta(mezzo, tratta);
                    System.out.println("Il mezzo ha percorso la tratta " + count + " volte.");
                }
                default -> System.out.println("Scelta non valida.");
            }

        } catch (Exception e) {
            System.out.println("Errore: " + e.getMessage());
            e.printStackTrace();
        }
    }}




