import dao.TitoloViaggioDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class MainGabriele {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Name_BuildWeek_1");
        EntityManager em = emf.createEntityManager();
        TitoloViaggioDao dao = new TitoloViaggioDao(em);
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        boolean running = true;

        while (running) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Stampa totale titoli di viaggio");
            System.out.println("2. Stampa titoli emessi da distributori automatici");
            System.out.println("3. Stampa titoli emessi da rivenditori autorizzati");
            System.out.println("4. Stampa titoli emessi in un intervallo di tempo");
            System.out.println("0. Esci");
            System.out.print("Seleziona un'opzione: ");

            int scelta = scanner.nextInt();
            scanner.nextLine(); // pulisce il buffer dopo nextInt()

            switch (scelta) {
                case 1:
                    long totale = dao.countTotaleTitoli();
                    System.out.println("Totale titoli emessi: " + totale);
                    break;
                case 2:
                    long daDistributori = dao.countTitoliDaDistributori();
                    System.out.println("Titoli emessi da distributori automatici: " + daDistributori);
                    break;
                case 3:
                    long daRivenditori = dao.countTitoliDaRivenditori();
                    System.out.println("Titoli emessi da rivenditori autorizzati: " + daRivenditori);
                    break;
                case 4:
                    System.out.print("Inserisci data di inizio (yyyy-MM-dd): ");
                    String dataInizioStr = scanner.nextLine();
                    System.out.print("Inserisci data di fine (yyyy-MM-dd): ");
                    String dataFineStr = scanner.nextLine();

                    try {
                        LocalDate dataInizio = LocalDate.parse(dataInizioStr, formatter);
                        LocalDate dataFine = LocalDate.parse(dataFineStr, formatter);

                        long inPeriodo = TitoloViaggioDao.countTitoliInPeriodo(dataInizio, dataFine);
                        System.out.println("Titoli emessi tra " + dataInizio + " e " + dataFine + ": " + inPeriodo);
                    } catch (Exception e) {
                        System.out.println("Formato data non valido. Usa yyyy-MM-dd.");
                    }
                    break;
                case 0:
                    running = false;
                    System.out.println("Chiusura del programma...");
                    break;
                default:
                    System.out.println("Scelta non valida. Riprova.");
            }
        }

        em.close();
        emf.close();
        scanner.close();
    }
}
