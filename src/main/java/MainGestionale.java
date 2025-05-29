import dao.*;
import entities.*;
import enums.*;

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

        DataTest.inserisciDatiDiTest(em);

        boolean running = true;

        try {
            while (running) {
                System.out.println("Benvenuto! Seleziona un'opzione:");
                System.out.println("\n1. Accedi\n2. Registrati\n0. Esci");
                System.out.println("Scegli un Opzione: ");
                int scelta = scanner.nextInt();
                scanner.nextLine();

                switch (scelta) {
                    case 1: {
                        System.out.println("Inserisci username:");
                        String usernameLogin = scanner.nextLine();
                        System.out.println("Inserisci password:");
                        String passwordLogin = scanner.nextLine();

                        List<Utente> utenti = utenteDao.findAll();
                        boolean accessoConsentito = false;
                        Utente utenteLoggato = null;

                        for (Utente u : utenti) {
                            if (u.getUsername().equals(usernameLogin) && u.getPassword().equals(passwordLogin)) {
                                accessoConsentito = true;
                                utenteLoggato = u;
                                break;
                            }
                        }

                        if (accessoConsentito) {
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
                                    System.out.println("6. Visualizza il tempo di percorrenza medio per tratta");
                                    System.out.println("7. Visualizza gli stati dei mezzi");
                                    System.out.println("8. Visualizza biglietti e abbonamenti venduti in un intervallo di tempo presso");
                                    System.out.println("9. Visualizza quanti biglietti e abbonamenti sono stai emessi in totale");
                                    System.out.println("10. Ottieni un biglietti o un abbonamenti");
                                    System.out.println("11. Valida un biglietti o un abbonamenti");
                                    System.out.println("12. Rinnova una tessera o un abbonamenti scaduto o in scadenza");
                                    System.out.println("0. Logout");

                                    int sceltaAdmin = scanner.nextInt();
                                    scanner.nextLine();

                                    switch (sceltaAdmin) {
                                        case 1 -> {
                                            utenti = utenteDao.findAll();
                                            utenti.forEach(System.out::println);
                                        }

                                        case 2 -> {
                                            List<Tessera> tessere = tesseraDao.findAll();
                                            tessere.forEach(System.out::println);
                                        }

                                        case 3 -> {
                                            List<Mezzo> mezzi = em.createQuery("SELECT m FROM mezzi m", Mezzo.class).getResultList();
                                            mezzi.forEach(System.out::println);
                                        }

                                        case 4 -> {
                                            List<PercorrenzaTratta> tratte = em.createQuery("SELECT p FROM percorrenza_tratte p", PercorrenzaTratta.class).getResultList();
                                            tratte.forEach(System.out::println);
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
                                    System.out.println("2. Visualizza la tua Tesserta");
                                    System.out.println("4. Visualizza le tratte con i mezzi affidati");
                                    System.out.println("5. Ottieni un Biglietto o un Abbonamento");
                                    System.out.println("6. Valida un Biglietto o un Abbonamento");
                                    System.out.println("7. Rinnova una Tessera o un Abbonamento scaduto o in Scadenza");
                                    System.out.println("0. Logout");

                                    int sceltaUtente = scanner.nextInt();
                                    scanner.nextLine();

                                    switch (sceltaUtente) {
                                        case 1 -> {
                                            try {
                                                Tessera tessera = tesseraDao.findByUtenteId(utenteLoggato.getId());
                                                System.out.println(tessera);
                                            } catch (Exception e) {
                                                System.out.println("Tessera non trovata.");
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


                        break;
                    }

                    case 2: {
                        System.out.println("Nome:");
                        String nome = scanner.nextLine();
                        System.out.println("Cognome:");
                        String cognome = scanner.nextLine();
                        System.out.println("Username:");
                        String user = scanner.nextLine();
                        System.out.println("Password:");
                        String password = scanner.nextLine();

                        UtenteNormale nuovoUtente = new UtenteNormale(nome, cognome, user, password);
                        utenteDao.save(nuovoUtente);

                        System.out.println("Utente registrato con successo.");
                        break;
                    }

                    case 0:
                        running = false;
                        System.out.println("Arrivederci!");
                        break;

                    default:
                        System.out.println("Scelta non valida. Riprova.");
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
            scanner.close();
        }
    }
}
