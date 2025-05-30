import dao.*;
import entities.*;
import enums.Periodicita;
import enums.TipoMezzo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

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

            List<Tratta> tratte = percorrenzaTrattaDao.findAllTratte();
            if (tratte.isEmpty()) {
                System.out.println("Nessuna tratta trovata nel sistema.");
                return;
            }

            System.out.println("Seleziona una tratta tra le seguenti:");
            for (int i = 0; i < tratte.size(); i++) {
                Tratta t = tratte.get(i);
                System.out.printf("%d) %s -> %s%n", i + 1, t.getZonaPartenza(), t.getCapolinea());
            }

            System.out.print("Inserisci il numero della tratta: ");
            int scelta = Integer.parseInt(scanner.nextLine());

            if (scelta < 1 || scelta > tratte.size()) {
                System.out.println("Scelta non valida.");
                return;
            }

            Tratta trattaSelezionata = tratte.get(scelta - 1);

            long count = mezzoDao.countPercorrenzeByMezzoAndTratta(mezzo, trattaSelezionata);
            System.out.println("Il mezzo ha percorso la tratta " + count + " volte.");

        } catch (NumberFormatException e) {
            System.out.println("Errore: inserisci un numero valido.");
        } catch (Exception e) {
            System.out.println("Errore inaspettato: " + e.getMessage());
        }
    }

    // 9(a). Crea nuovo Biglietto
    public void ottieniBiglietto(Scanner scanner, EntityManager em, Utente utenteLoggato) {
        TrattaDao trattaDao = new TrattaDao(em);
        PercorrenzaTrattaDao percorrenzaTrattaDao1 = new PercorrenzaTrattaDao(em);
        PuntoVenditaDao puntoVenditaDao = new PuntoVenditaDao(em);
        BigliettoDao bigliettoDao = new BigliettoDao(em);

        List<Tratta> tratte = trattaDao.findAll(em);
        if (tratte.isEmpty()) {
            System.out.println("Nessuna tratta disponibile.");
            return;
        }

        System.out.println("Seleziona una tratta:");
        for (int i = 0; i < tratte.size(); i++) {
            System.out.printf("%d) %s\n", i + 1, tratte.get(i));
        }
        int idxTratta = scanner.nextInt() - 1;
        scanner.nextLine();
        if (idxTratta < 0 || idxTratta >= tratte.size()) {
            System.out.println("Tratta non valida.");
            return;
        }
        Tratta trattaSelezionata = tratte.get(idxTratta);

        List<Mezzo> mezzi = percorrenzaTrattaDao1.findMezziByTratta(em, trattaSelezionata);
        if (mezzi.isEmpty()) {
            System.out.println("Nessun mezzo per questa tratta.");
            return;
        }

        System.out.println("Seleziona un mezzo:");
        for (int i = 0; i < mezzi.size(); i++) {
            System.out.printf("%d) %s\n", i + 1, mezzi.get(i));
        }
        int idxMezzo = scanner.nextInt() - 1;
        scanner.nextLine();
        if (idxMezzo < 0 || idxMezzo >= mezzi.size()) {
            System.out.println("Mezzo non valido.");
            return;
        }
        Mezzo mezzoSelezionato = mezzi.get(idxMezzo);

        List<PuntoVendita> puntiVendita = puntoVenditaDao.findAll(em);
        if (puntiVendita.isEmpty()) {
            System.out.println("Nessun punto vendita disponibile.");
            return;
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
            return;
        }
        PuntoVendita puntoVenditaSelezionato = puntiVendita.get(idxPv);

        Biglietto biglietto = Biglietto.creaBiglietto(mezzoSelezionato, utenteLoggato, puntoVenditaSelezionato);
        em.getTransaction().begin();
        bigliettoDao.save(em, biglietto);
        em.getTransaction().commit();

        System.out.println("Biglietto creato con codice: " + biglietto.getCodice());
    }

    // 9(b). Crea nuovo Abbonamento
    public void ottieniAbbonamento(Scanner scanner, EntityManager em, Utente utenteLoggato) {
        PuntoVenditaDao puntoVenditaDao = new PuntoVenditaDao(em);

        try {
            System.out.println("Creazione Abbonamento");

            Tessera tessera = utenteLoggato.getTessera();
            if (tessera == null) {
                System.out.println("Errore: L'utente non ha una tessera associata.");
                return;
            }

            System.out.println("Seleziona il tipo di punto vendita:");
            System.out.println("1. Rivenditore Autorizzato");
            System.out.println("2. Distributore Automatizzato");
            System.out.print("Scelta: ");
            int sceltaPV = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Nome punto vendita: ");
            String nomePV = scanner.nextLine();
            System.out.print("Indirizzo punto vendita: ");
            String indirizzoPV = scanner.nextLine();

            PuntoVendita puntoVendita;

            switch (sceltaPV) {
                case 1 -> puntoVendita = new RivenditoreAutorizzato(nomePV, indirizzoPV);
                case 2 -> {
                    System.out.print("Il distributore è attivo? (true/false): ");
                    boolean attivo = scanner.nextBoolean();
                    scanner.nextLine();
                    puntoVendita = new DistributoreAutomatizzato(nomePV, indirizzoPV, attivo);
                }
                default -> {
                    System.out.println("Scelta punto vendita non valida.");
                    return;
                }
            }

            LocalDate dataEmissione = LocalDate.now();
            LocalDate validoDa = LocalDate.now();

            System.out.println("Periodicità disponibili:");
            System.out.println("1. SETTIMANALE");
            System.out.println("2. MENSILE");
            System.out.print("Scelta: ");

            int sceltaPeriodicita = scanner.nextInt();
            scanner.nextLine();

            Periodicita periodicita;
            LocalDate validoA;

            switch (sceltaPeriodicita) {
                case 1 -> {
                    periodicita = Periodicita.SETTIMANALE;
                    validoA = validoDa.plusWeeks(1);
                }
                case 2 -> {
                    periodicita = Periodicita.MENSILE;
                    validoA = validoDa.plusMonths(1);
                }
                default -> {
                    System.out.println("Periodicità non valida.");
                    return;
                }
            }

            System.out.println("Seleziona il tipo mezzo:");
            System.out.println("1. BUS");
            System.out.println("2. TRAM");
            System.out.print("Scelta: ");
            int sceltaMezzo = scanner.nextInt();
            scanner.nextLine();

            TipoMezzo tipoMezzo;

            switch (sceltaMezzo) {
                case 1 -> tipoMezzo = TipoMezzo.AUTOBUS;
                case 2 -> tipoMezzo = TipoMezzo.TRAM;
                default -> {
                    System.out.println("Tipo mezzo non valido.");
                    return;
                }
            }

            String codice = UUID.randomUUID().toString();

            Abbonamento abbonamento = new Abbonamento(
                    codice, dataEmissione, puntoVendita, utenteLoggato,
                    periodicita, tipoMezzo, validoDa, validoA, tessera
            );

            tessera.addAbbonamento(abbonamento);

            em.getTransaction().begin();
            em.persist(puntoVendita);
            em.persist(abbonamento);
            em.getTransaction().commit();

            System.out.println("Abbonamento creato con successo:");
            System.out.println(abbonamento);

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Errore durante la creazione dell'abbonamento: " + e.getMessage());
        }
    }

    // 10(a). valida Biglietto
    public void validaBiglietto(Scanner scanner, EntityManager em, Utente utenteLoggato) {
        try {
            List<Biglietto> bigliettiNonValidati = em.createQuery(
                            "SELECT b FROM Biglietto b WHERE b.utente = :utente AND b.validato = false", Biglietto.class)
                    .setParameter("utente", utenteLoggato)
                    .getResultList();

            if (bigliettiNonValidati.isEmpty()) {
                System.out.println("Nessun biglietto da validare.");
                return;
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
                return;
            }

            Biglietto daValidare = bigliettiNonValidati.get(idxBiglietto);

            em.getTransaction().begin();
            Biglietto.validaBiglietto(daValidare);
            em.merge(daValidare);
            em.getTransaction().commit();

            System.out.println("Biglietto validato in data: " + daValidare.getDataValidazione());

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Errore durante la validazione del biglietto: " + e.getMessage());
        }
    }

    //10(b). validità Abbonamento
    public void validitàAbbonamento(Scanner scanner, EntityManager em, String codiceTessera) {
        try {
            Tessera tessera = em.createQuery("SELECT t FROM tessere t WHERE t.codice = :codice", Tessera.class)
                    .setParameter("codice", UUID.fromString(codiceTessera))
                    .getSingleResult();

            List<Abbonamento> abbonamenti = em.createQuery(
                            "SELECT a FROM abbonamenti a WHERE a.tessera = :tessera", Abbonamento.class)
                    .setParameter("tessera", tessera)
                    .getResultList();

            if (abbonamenti.isEmpty()) {
                System.out.println("Nessun abbonamento associato alla tessera " + codiceTessera);
                return;
            }

            System.out.println("Abbonamenti associati alla tessera " + codiceTessera + ":");
            for (int i = 0; i < abbonamenti.size(); i++) {
                Abbonamento a = abbonamenti.get(i);
                boolean valido = !a.getValidoA().isBefore(LocalDate.now()) && !a.getValidoDa().isAfter(LocalDate.now());
                System.out.printf("%d) Codice: %s, Periodicità: %s, Tipo Mezzo: %s, Valido da: %s, Valido a: %s - %s\n",
                        i + 1,
                        a.getCodice(),
                        a.getPeriodicita(),
                        a.getTipo(),
                        a.getValidoDa(),
                        a.getValidoA(),
                        valido ? "VALIDO" : "SCADUTO");
            }

            System.out.print("Seleziona l'abbonamento da controllare: ");
            int scelta = scanner.nextInt() - 1;
            scanner.nextLine();

            if (scelta < 0 || scelta >= abbonamenti.size()) {
                System.out.println("Scelta non valida.");
                return;
            }

            Abbonamento abbonamentoScelto = abbonamenti.get(scelta);
            boolean isValido = !abbonamentoScelto.getValidoA().isBefore(LocalDate.now()) && !abbonamentoScelto.getValidoDa().isAfter(LocalDate.now());

            System.out.println("L'abbonamento con codice " + abbonamentoScelto.getCodice() + " è " + (isValido ? "ancora valido." : "scaduto."));

        } catch (NoResultException e) {
            System.out.println("Tessera non trovata con codice: " + codiceTessera);
        } catch (Exception e) {
            System.out.println("Errore durante il controllo della validità: " + e.getMessage());
        }
    }

}