import dao.*;
import entities.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.Scanner;

public class MainGestionale {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Name_BuildWeek_1");
        EntityManager em = emf.createEntityManager();
        Scanner scanner = new Scanner(System.in);

        TesseraDao tesseraDao = new TesseraDao(em);
        TitoloViaggioDao titoloViaggioDao = new TitoloViaggioDao(em);
        UtenteDao utenteDao = new UtenteDao(em);
        MezzoDao mezzoDao = new MezzoDao(em);
        PercorrenzaTrattaDao percorrenzaTrattaDao = new PercorrenzaTrattaDao(em);
        BigliettoDao bigliettoDao = new BigliettoDao(em);

        ArchivioGestionale archivioGestionale = new ArchivioGestionale(em, utenteDao, tesseraDao, titoloViaggioDao, mezzoDao, percorrenzaTrattaDao, bigliettoDao);

        DataTest.inserisciDatiDiTest(em);

        boolean running = true;

        try {
            while (running) {
                System.out.println("\nBenvenuto! Seleziona un'opzione:");
                System.out.println("1. Accedi");
                System.out.println("2. Registrati");
                System.out.println("0. Esci");
                System.out.print("Scegli un'opzione: ");

                int scelta;
                try {
                    scelta = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Input non valido, inserisci un numero.");
                    continue;
                }

                switch (scelta) {
                    case 1 -> {
                        System.out.print("Inserisci username: ");
                        String usernameLogin = scanner.nextLine();
                        System.out.print("Inserisci password: ");
                        String passwordLogin = scanner.nextLine();

                        List<Utente> utenti = utenteDao.findAll();
                        Utente utenteLoggato = utenti.stream()
                                .filter(u -> u.getUsername().equals(usernameLogin) && u.getPassword().equals(passwordLogin))
                                .findFirst()
                                .orElse(null);

                        if (utenteLoggato != null) {
                            System.out.println("Login effettuato con successo. Benvenuto " + utenteLoggato.getNome() + "!");

                            if (utenteLoggato instanceof Amministratore) {
                                boolean adminMenu = true;
                                while (adminMenu) {
                                    System.out.println("\n--- Menu Amministratore ---");
                                    System.out.println("1. Visualizza tutti gli utenti");
                                    System.out.println("2. Visualizza tutte le tessere");
                                    System.out.println("3. Visualizza tutti i mezzi e i loro stati");
                                    System.out.println("4. Visualizza le tratte con i mezzi affidati");
                                    System.out.println("5. Visualizza biglietti e abbonamenti validati per singolo mezzo");
                                    System.out.println("6. Visualizza il tempo medio di percorrenza medio per tratta");
                                    System.out.println("7. Visualizza statistiche vendita biglietti e abbonamenti in un intervallo di tempo");
                                    System.out.println("8. Visualizza quante volte un mezzo ha percorso una tratta specifica");
                                    System.out.println("9. Ottieni un biglietto o un abbonamento");
                                    System.out.println("10. Valida un biglietto o un abbonamento");
                                    System.out.println("11. Rinnova una tessera o un abbonamento scaduto o in scadenza");
                                    System.out.println("0. Logout");
                                    System.out.print("Scegli un'opzione: ");

                                    int sceltaAdmin;
                                    try {
                                        sceltaAdmin = Integer.parseInt(scanner.nextLine());
                                    } catch (NumberFormatException e) {
                                        System.out.println("Input non valido.");
                                        continue;
                                    }

                                    switch (sceltaAdmin) {
                                        case 1 -> archivioGestionale.visualizzaUtenti();
                                        case 2 -> archivioGestionale.visualizzaTessere();
                                        case 3 -> archivioGestionale.visualizzaMezziStati();
                                        case 4 -> archivioGestionale.visualizzaTratteMezzi();
                                        case 5 -> archivioGestionale.visualizzaBigliettiValidatiPerMezzo(scanner);
                                        case 6 -> archivioGestionale.visualizzaTempoMedioPercorrenzaMedioTratta(scanner);
                                        case 7 -> archivioGestionale.stampaStatistichePeriodo(titoloViaggioDao, scanner);
                                        case 8 -> archivioGestionale.visualizzaNumeroPercorrenzeTrattaMezzo(scanner);
                                        case 9 -> {
                                            System.out.println("vuoi un abbonamento o un biglietto ?");
                                            System.out.println("\n1. Biglietto\n2. Abbonamento");

                                            int sceltaTitoloViaggio = scanner.nextInt();
                                            scanner.nextLine();

                                            System.out.println("Scelta inserita: " + sceltaTitoloViaggio);

                                            switch (sceltaTitoloViaggio) {
                                                case 1 -> archivioGestionale.ottieniBiglietto(scanner, em, utenteLoggato);
                                                case 2 -> archivioGestionale.ottieniAbbonamento(scanner, em, utenteLoggato);
                                                default -> System.out.println("Scelta titolo di viaggio non valida.");
                                            }
                                        }
                                        case 10 -> {
                                            System.out.println("vuoi Validare un abbonamento o un biglietto ?");
                                            System.out.println("\n1. Biglietto\n2. Abbonamento");

                                            int sceltaTitoloViaggio = scanner.nextInt();
                                            scanner.nextLine();

                                            System.out.println("Scelta inserita: " + sceltaTitoloViaggio);

                                            switch (sceltaTitoloViaggio) {
                                                case 1 -> archivioGestionale.validaBiglietto(scanner, em, utenteLoggato);
                                                case 2 -> {
                                                    System.out.print("Inserisci il codice tessera: ");
                                                    String codiceTessera = scanner.nextLine();
                                                    archivioGestionale.validitàAbbonamento(scanner, em, codiceTessera);
                                                }

                                                default -> System.out.println("Scelta titolo di viaggio non valida.");
                                            }
                                        }
                                        case 0 -> {
                                            adminMenu = false;
                                            System.out.println("Logout effettuato.");
                                        }
                                        default -> System.out.println("Scelta non valida.");
                                    }
                                }

                            } else if (utenteLoggato instanceof UtenteNormale) {
                                boolean userMenu = true;
                                while (userMenu) {
                                    System.out.println("\n--- Menu Utente ---");
                                    System.out.println("1. Visualizza il tuo Profilo");
                                    System.out.println("2. Visualizza la tua Tessera");
                                    System.out.println("3. Visualizza le tratte con i mezzi affidati");
                                    System.out.println("4. Ottieni un Biglietto o un Abbonamento");
                                    System.out.println("5. Valida un Biglietto o un Abbonamento");
                                    System.out.println("0. Logout");
                                    System.out.print("Scegli un'opzione: ");

                                    int sceltaUtente;
                                    try {
                                        sceltaUtente = Integer.parseInt(scanner.nextLine());
                                    } catch (NumberFormatException e) {
                                        System.out.println("Input non valido.");
                                        continue;
                                    }

                                    switch (sceltaUtente) {
                                        case 1 -> System.out.println(utenteLoggato);
                                        case 2 -> {
                                            Tessera tessera = tesseraDao.findByUtenteId(utenteLoggato.getId());
                                            if (tessera != null) System.out.println(tessera);
                                            else System.out.println("Tessera non trovata.");
                                        }
                                        case 3 -> archivioGestionale.visualizzaTratteMezzi();
                                        case 4 -> {
                                            System.out.println("vuoi un abbonamento o un biglietto ?");
                                            System.out.println("\n1. Biglietto\n2. Abbonamento");

                                            int sceltaTitoloViaggio = scanner.nextInt();
                                            scanner.nextLine();

                                            System.out.println("Scelta inserita: " + sceltaTitoloViaggio);

                                            switch (sceltaTitoloViaggio) {
                                                case 1 -> archivioGestionale.ottieniBiglietto(scanner, em, utenteLoggato);
                                                case 2 -> archivioGestionale.ottieniAbbonamento(scanner, em, utenteLoggato);
                                                default -> System.out.println("Scelta titolo di viaggio non valida.");
                                            }
                                        }
                                        case 5 -> {
                                            System.out.println("vuoi Validare un abbonamento o un biglietto ?");
                                            System.out.println("\n1. Biglietto\n2. Abbonamento");

                                            int sceltaTitoloViaggio = scanner.nextInt();
                                            scanner.nextLine();

                                            System.out.println("Scelta inserita: " + sceltaTitoloViaggio);

                                            switch (sceltaTitoloViaggio) {
                                                case 1 -> archivioGestionale.validaBiglietto(scanner, em, utenteLoggato);
                                                case 2 -> {
                                                    System.out.print("Inserisci il codice tessera: ");
                                                    String codiceTessera = scanner.nextLine();
                                                    archivioGestionale.validitàAbbonamento(scanner, em, codiceTessera);
                                                }

                                                default -> System.out.println("Scelta titolo di viaggio non valida.");
                                            }
                                        }
                                        case 0 -> {
                                            userMenu = false;
                                            System.out.println("Logout effettuato.");
                                        }
                                        default -> System.out.println("Scelta non valida.");
                                    }
                                }
                            }

                        } else {
                            System.out.println("Username o password errati.");
                        }
                    }
                    case 2 -> {
                        System.out.print("Nome: ");
                        String nome = scanner.nextLine();
                        System.out.print("Cognome: ");
                        String cognome = scanner.nextLine();
                        System.out.print("Username: ");
                        String user = scanner.nextLine();
                        System.out.print("Password: ");
                        String password = scanner.nextLine();

                        UtenteNormale nuovoUtente = new UtenteNormale(nome, cognome, user, password);
                        utenteDao.save(nuovoUtente);

                        System.out.println("Utente registrato con successo.");
                    }
                    case 0 -> {
                        running = false;
                        System.out.println("Arrivederci!");
                    }
                    default -> System.out.println("Scelta non valida. Riprova.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanner.close();
            em.close();
            emf.close();
        }
    }
}
