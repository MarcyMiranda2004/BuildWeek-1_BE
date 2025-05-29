import dao.*;
import entities.*;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class ArchivioGestionale {

    private final UtenteDao utenteDao;
    private final TesseraDao tesseraDao;
    private final TitoloViaggioDao titoloViaggioDao;
    private final MezzoDao mezzoDao;
    private final PercorrenzaTrattaDao percorrenzaTrattaDao;
    private final BigliettoDao bigliettoDao;

    public ArchivioGestionale(
            EntityManager em,
            UtenteDao utenteDao,
            TesseraDao tesseraDao,
            TitoloViaggioDao titoloViaggioDao,
            MezzoDao mezzoDao,
            PercorrenzaTrattaDao percorrenzaTrattaDao,
            BigliettoDao bigliettoDao
    ) {
        this.utenteDao = utenteDao;
        this.tesseraDao = tesseraDao;
        this.titoloViaggioDao = titoloViaggioDao;
        this.mezzoDao = mezzoDao;
        this.percorrenzaTrattaDao = percorrenzaTrattaDao;
        this.bigliettoDao = bigliettoDao;
    }

    // 1. Visualizza tutti gli utenti
    public void visualizzaUtenti() {
        List<Utente> utenti = utenteDao.findAll();
        utenti.forEach(System.out::println);
    }

    // 2. Visualizza tutte le tessere
    public void visualizzaTessere() {
        List<Tessera> tessere = tesseraDao.findAll();
        tessere.forEach(System.out::println);
    }

    // 3. Visualizza tutti i mezzi e i loro stati
    public void visualizzaMezziStati() {
        List<Mezzo> mezzi = mezzoDao.findAll();
        mezzi.forEach(System.out::println);
    }

    // 4. Visualizza tutte le percorrenze tratte
    public void visualizzaTratteMezzi() {
        List<PercorrenzaTratta> percorrenze = percorrenzaTrattaDao.findAll();
        percorrenze.forEach(System.out::println);
    }

    // 5. Visualizza biglietti e abbonamenti validati per singolo mezzo
    public void visualizzaBigliettiValidatiPerMezzo(Scanner scanner) {
        List<Mezzo> tuttiMezzi = mezzoDao.findAll();
        if (tuttiMezzi.isEmpty()) {
            System.out.println("Nessun mezzo presente.");
            return;
        }

        System.out.println("Seleziona un mezzo:");
        for (int i = 0; i < tuttiMezzi.size(); i++) {
            System.out.printf("%d) %s\n", i + 1, tuttiMezzi.get(i));
        }

        int scelta = -1;
        try {
            scelta = scanner.nextInt() - 1;
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Input non valido.");
            scanner.nextLine();
            return;
        }

        if (scelta < 0 || scelta >= tuttiMezzi.size()) {
            System.out.println("Scelta non valida.");
            return;
        }

        Mezzo mezzoSelezionato = tuttiMezzi.get(scelta);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime unMeseFa = now.minusMonths(1);

        long numeroBiglietti = bigliettoDao.countValidatiByMezzoAndPeriodo(mezzoSelezionato, unMeseFa, now);

        System.out.println("Biglietti validati su " + mezzoSelezionato + " nell'ultimo mese: " + numeroBiglietti);
    }

    // 6. Visualizza il tempo di percorrenza medio per tratta
    public void visualizzaTempoMedioPercorrenzaMedioTratta(Scanner scanner) {
        // Carico mezzi e tratte da DB
        List<Mezzo> mezzi = mezzoDao.findAll();
        if (mezzi.isEmpty()) {
            System.out.println("Nessun mezzo presente.");
            return;
        }
        List<Tratta> tratte = percorrenzaTrattaDao.findAllTratteDistinct();

        if (tratte.isEmpty()) {
            System.out.println("Nessuna tratta presente.");
            return;
        }

        // Scelta mezzo
        System.out.println("Seleziona un mezzo:");
        for (int i = 0; i < mezzi.size(); i++) {
            System.out.printf("%d) %s%n", i + 1, mezzi.get(i));
        }
        int sceltaMezzo = -1;
        try {
            sceltaMezzo = scanner.nextInt() - 1;
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Input non valido.");
            scanner.nextLine();
            return;
        }
        if (sceltaMezzo < 0 || sceltaMezzo >= mezzi.size()) {
            System.out.println("Scelta mezzo non valida.");
            return;
        }
        Mezzo mezzoSelezionato = mezzi.get(sceltaMezzo);

        // Scelta tratta
        System.out.println("Seleziona una tratta:");
        for (int i = 0; i < tratte.size(); i++) {
            System.out.printf("%d) %s%n", i + 1, tratte.get(i));
        }
        int sceltaTratta = -1;
        try {
            sceltaTratta = scanner.nextInt() - 1;
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Input non valido.");
            scanner.nextLine();
            return;
        }
        if (sceltaTratta < 0 || sceltaTratta >= tratte.size()) {
            System.out.println("Scelta tratta non valida.");
            return;
        }
        Tratta trattaSelezionata = tratte.get(sceltaTratta);

        // Calcolo tempo medio con il metodo che ti ho fatto prima
        Double tempoMedio = mezzoDao.getTempoMedioPercorrenzaByMezzoAndTratta(mezzoSelezionato, trattaSelezionata);
        if (tempoMedio == null) {
            System.out.println("Nessuna percorrenza trovata per il mezzo e la tratta specificati.");
        } else {
            System.out.printf("Tempo medio effettivo di percorrenza: %.2f minuti%n", tempoMedio);
        }
    }

    // 7. Visualizza statistiche vendita biglietti e abbonamenti in intervallo di tempo
    public void stampaStatistichePeriodo(TitoloViaggioDao titoloViaggioDao, Scanner scanner) {
        try {
            System.out.print("Inserisci data inizio (yyyy-MM-dd): ");
            LocalDate inizio = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE);
            System.out.print("Inserisci data fine (yyyy-MM-dd): ");
            LocalDate fine = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE);

            if (fine.isBefore(inizio)) {
                System.out.println("La data di fine deve essere successiva a quella di inizio.");
                return;
            }

            List<TitoloViaggio> titoliVenduti = titoloViaggioDao.findByPeriodo(inizio.atStartOfDay(), fine.atTime(23, 59, 59));

            if (titoliVenduti.isEmpty()) {
                System.out.println("Nessuna vendita in questo intervallo di tempo.");
                return;
            }

            long bigliettiCount = titoliVenduti.stream()
                    .filter(t -> t instanceof Biglietto)
                    .count();

            long abbonamentiCount = titoliVenduti.stream()
                    .filter(t -> t instanceof Abbonamento)
                    .count();

            System.out.printf("Vendite dal %s al %s:%n", inizio, fine);
            System.out.printf("Biglietti venduti: %d%n", bigliettiCount);
            System.out.printf("Abbonamenti venduti: %d%n", abbonamentiCount);

        } catch (Exception e) {
            System.out.println("data non presente nel database o nessun titolo corrispondente al periodo .");
        }
    }

    // 8. Visualizza quante volte un mezzo ha percorso una tratta specifica
    public void visualizzaNumeroPercorrenzeTrattaMezzo(Scanner scanner) {
        try {
            System.out.print("Inserisci l'ID del mezzo: ");
            Long mezzoId = Long.parseLong(scanner.nextLine());
            Mezzo mezzo = mezzoDao.findById(mezzoId);
            if (mezzo == null) {
                System.out.println("Mezzo non trovato.");
                return;
            }

            System.out.print("Inserisci zona di partenza della tratta: ");
            String zonaPartenza = scanner.nextLine();
            System.out.print("Inserisci capolinea della tratta: ");
            String capolinea = scanner.nextLine();

            Tratta tratta = percorrenzaTrattaDao.findTrattaByPartenzaECapolinea(zonaPartenza, capolinea);
            if (tratta == null) {
                System.out.println("Tratta non trovata.");
                return;
            }

            long count = mezzoDao.countPercorrenzeByMezzoAndTratta(mezzo, tratta);
            System.out.println("Il mezzo ha percorso la tratta " + count + " volte.");

        } catch (Exception e) {
            System.out.println("Errore: input non valido.");
        }
    }

}
