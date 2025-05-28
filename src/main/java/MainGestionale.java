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
                            if (u instanceof UtenteNormale un &&
                                    un.getUsername().equals(usernameLogin) &&
                                    un.getPassword().equals(passwordLogin)) {
                                accessoConsentito = true;
                                utenteLoggato = un;
                                break;
                            }
                        }

                        if (accessoConsentito) {
                            System.out.println("Login effettuato con successo. Benvenuto " + utenteLoggato.getNome() + "!");
                            // eventuale sotto-menu utente loggato
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
