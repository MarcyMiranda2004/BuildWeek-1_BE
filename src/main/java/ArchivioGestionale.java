import dao.*;
import entities.*;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Scanner;

public class ArchivioGestionale {

    private final UtenteDao utenteDao;
    private final TesseraDao tesseraDao;
    private final TitoloViaggioDao titoloViaggioDao;
    private final EntityManager em;

    public ArchivioGestionale(EntityManager em, UtenteDao utenteDao, TesseraDao tesseraDao, TitoloViaggioDao titoloViaggioDao) {
        this.em = em;
        this.utenteDao = utenteDao;
        this.tesseraDao = tesseraDao;
        this.titoloViaggioDao = titoloViaggioDao;
    }

    // Metodo per il Login
    public Utente login(Scanner scanner) {
        System.out.println("Inserisci username:");
        String usernameLogin = scanner.nextLine();
        System.out.println("Inserisci password:");
        String passwordLogin = scanner.nextLine();

        List<Utente> utenti = utenteDao.findAll();
        for (Utente u : utenti) {
            if (u.getUsername().equals(usernameLogin) && u.getPassword().equals(passwordLogin)) {
                return u;
            }
        }
        return null;
    }

}
