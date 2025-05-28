import dao.TesseraDao;
import dao.TitoloViaggioDao;
import dao.UtenteDao;
import entities.*;
import enums.Periodicita;
import enums.TipoMezzo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.UUID;

public class MainJoseline {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Name_BuildWeek_1");
        EntityManager em = emf.createEntityManager();

        TesseraDao tesseraDao = new TesseraDao(em);
        TitoloViaggioDao titoloViaggioDao = new TitoloViaggioDao(em);
        UtenteDao utenteDao = new UtenteDao(em);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\nScegli un'opzione:");
            System.out.println("1 - Crea Utente Normale e Tessera (automatica)");
            System.out.println("2 - Crea Amministratore");
            System.out.println("3 - Rinnova Tessera");
            System.out.println("4 - Esci");
            System.out.println("5 - Crea Abbonamento di prova");
            System.out.println("6 - tessere di test (una scaduta, una valida)");
            System.out.print("Scelta: ");

            int scelta = scanner.nextInt();
            scanner.nextLine();

            switch (scelta) {
                case 1 -> {
                    System.out.println("Nome Utente:");
                    String nome = scanner.nextLine();
                    System.out.println("Cognome Utente:");
                    String cognome = scanner.nextLine();

                    UtenteNormale utenteNormale = new UtenteNormale(nome, cognome);
                    utenteDao.save(utenteNormale);  // Salva anche la tessera automaticamente

                    System.out.println("Utente e Tessera creati con successo.");
                }

                case 2 -> {
                    System.out.print("Nome amministratore: ");
                    String nome = scanner.nextLine();
                    System.out.print("Cognome amministratore: ");
                    String cognome = scanner.nextLine();
                    System.out.print("Username amministratore: ");
                    String username = scanner.nextLine();
                    System.out.print("Password amministratore: ");
                    String password = scanner.nextLine();

                    Amministratore amministratore = new Amministratore(nome, cognome, username, password);

                    em.getTransaction().begin();
                    em.persist(amministratore);
                    em.getTransaction().commit();

                    System.out.println("Amministratore creato con successo: " + amministratore);
                }

                case 3 -> {
                    System.out.print("Inserisci ID tessera da rinnovare: ");
                    Long idTessera = scanner.nextLong();
                    scanner.nextLine();

                    tesseraDao.rinnovaTessera(idTessera);
                    System.out.println("Tessera con ID " + idTessera + " rinnovata (se esiste).");
                }

                case 5 -> {
                    System.out.println("Creazione Abbonamento");

                    System.out.print("ID Tessera esistente: ");
                    Long idTessera = scanner.nextLong();
                    scanner.nextLine();

                    Tessera tessera = tesseraDao.findById(idTessera);
                    if (tessera == null) {
                        System.out.println("Tessera non trovata.");
                        break;
                    }

                    System.out.print("Nome punto vendita: ");
                    String nomePV = scanner.nextLine();
                    System.out.print("Indirizzo punto vendita: ");
                    String indirizzoPV = scanner.nextLine();
                    PuntoVendita puntoVendita = new PuntoVendita(nomePV, indirizzoPV) {};

                    System.out.print("Data emissione (YYYY-MM-DD): ");
                    LocalDate dataEmissione = LocalDate.parse(scanner.nextLine());
                    System.out.print("Data inizio validità (YYYY-MM-DD): ");
                    LocalDate validoDa = LocalDate.parse(scanner.nextLine());
                    System.out.print("Data fine validità (YYYY-MM-DD): ");
                    LocalDate validoA = LocalDate.parse(scanner.nextLine());

                    System.out.print("Periodicità (SETTIMANALE, MENSILE): ");
                    Periodicita periodicita = Periodicita.valueOf(scanner.nextLine().toUpperCase());
                    System.out.print("Tipo mezzo (BUS, TRAM): ");
                    TipoMezzo tipoMezzo = TipoMezzo.valueOf(scanner.nextLine().toUpperCase());

                    String codice = UUID.randomUUID().toString();

                    Abbonamento abbonamento = new Abbonamento(
                            codice, dataEmissione, puntoVendita, tessera.getUtente(),
                            periodicita, tipoMezzo, validoDa, validoA
                    );
                    tessera.addAbbonamento(abbonamento);

                    em.getTransaction().begin();
                    em.persist(puntoVendita);
                    em.persist(abbonamento);
                    em.getTransaction().commit();

                    System.out.println("Abbonamento creato con successo: " + abbonamento);
                }

                case 6 -> {
                    UtenteNormale u1 = new UtenteNormale("Topo", "Gigio");
                    Tessera t1 = new Tessera(LocalDate.now(), LocalDate.now().plusYears(1), u1);
                    u1.setTessera(t1);

                    UtenteNormale u2 = new UtenteNormale("Fabio", "Verdi");
                    Tessera t2 = new Tessera(LocalDate.now().minusYears(2), LocalDate.now().minusDays(30), u2);
                    u2.setTessera(t2);

                    em.getTransaction().begin();
                    em.persist(u1);
                    em.persist(u2);
                    em.getTransaction().commit();

                    System.out.println("Tessera valida creata con ID: " + t1.getId());
                    System.out.println("Tessera scaduta creata con ID: " + t2.getId());
                }

                case 4 -> {
                    running = false;
                    System.out.println("Uscita dal programma.");
                }

                default -> System.out.println("Scelta non valida. Riprova.");
            }
        }

        em.close();
        emf.close();
        scanner.close();
    }
}
