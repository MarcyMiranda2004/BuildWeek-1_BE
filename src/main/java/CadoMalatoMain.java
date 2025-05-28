import dao.AbbonamentoDao;
import dao.PuntoVenditaDao;
import dao.TesseraDao;
import dao.UtenteDao;
import entities.*;
import enums.Periodicita;
import enums.TipoMezzo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class CadoMalatoMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Name_BuildWeek_1");
        EntityManager em = emf.createEntityManager();

        TesseraDao tesseraDao = new TesseraDao(em);
        UtenteDao utenteDao = new UtenteDao(em);
        AbbonamentoDao abbonamentoDao = new AbbonamentoDao(em);
        PuntoVenditaDao puntoVenditaDao= new PuntoVenditaDao(em);
        Scanner scanner= new Scanner(System.in);

        try {
            em.getTransaction().begin();

            // Creazione di un utente
            UtenteNormale utente = new UtenteNormale("Mario", "Rossi", "mario", "password");

            em.persist(utente);

            // Creazione di una tessera
            Tessera tesseraCreata = new Tessera(LocalDate.now(), LocalDate.now().plusYears(1), utente); // Rinomina la variabile per chiarezza
            em.persist(tesseraCreata);
            em.persist(tesseraCreata);

            // creazione di un punto vendita
            RivenditoreAutorizzato rivenditore1 = new RivenditoreAutorizzato("Bar Mario", "Via Roma 10");
            em.persist(rivenditore1);


            // creazione di un abbonamento
            Abbonamento abbonamento1= new Abbonamento(UUID.randomUUID().toString(),LocalDate.now(),rivenditore1,utente, Periodicita.MENSILE, TipoMezzo.TRAM,LocalDate.now(),
                    LocalDate.now().plusYears(1),tesseraCreata);
            // Aggiorna la lista di abbonamenti della tessera
            tesseraCreata.getListaAbbonamenti().add(abbonamento1);
            em.persist(abbonamento1);



            em.getTransaction().commit();

            System.out.println(tesseraCreata.getListaAbbonamenti());
            boolean running = true;

            while (running){
                System.out.println("----MENU-----");
                System.out.println("1. Cerca la validità di un abbonamento");
                System.out.println("2. Crea un nuovo abbonamento");
                System.out.println("0. Esci");
                System.out.print("Scegli un'opzione: ");
                int scelta = scanner.nextInt();
                scanner.nextLine();

                switch (scelta){
//                    case 1:
//                        System.out.print("Inserisci l'ID della tessera (esistente): ");
//                        Long tesseraId = scanner.nextLong();
//                        scanner.nextLine();
//
//                        Tessera tesseraRicercata = tesseraDao.findById(tesseraId);
//                        System.out.println(tesseraRicercata);
//                        System.out.println(tesseraCreata);
//                        System.out.println(abbonamento1);
//                        if (tesseraRicercata != null) {
//                            String codiceTessera = tesseraRicercata.getCodice().toString(); // Converte l'UUID in stringa
//                            boolean isValido = abbonamentoDao.isAbbonamentoValido(codiceTessera);
//
//                            if (isValido) {
//                                System.out.println("L'abbonamento associato alla tessera con ID "+tesseraId+" è valido.");
//                                System.out.println("Dettagli dell'abbonamento:");
//                                for (Abbonamento abbonamento : tesseraRicercata.getListaAbbonamenti()) {
//                                    System.out.println("ID abbonamento: " + abbonamento.getId() +" Della tessera con ID:"+ tesseraId);
//                                    System.out.println("Tipo: " + abbonamento.getTipo());
//                                    System.out.println("Data inizio: " + abbonamento.getValidoDa());
//                                    System.out.println("Data fine: " + abbonamento.getValidoA());
//                                    System.out.println("Punto di vendita: " + abbonamento.getPuntoVendita().getNome());
//                                    System.out.println("Utente: " + abbonamento.getUtente().getNome() + " " + abbonamento.getUtente().getCognome());
//                                }
//                            } else {
//                                System.out.println("L'abbonamento associato alla tessera con ID"+tesseraId+"  è scaduto.");
//                            }
//                        } else {
//                            System.out.println("Tessera non trovata.");
//                        }
//                        break;
                    case 1:
                        System.out.print("Inserisci il codice dell'abbonamento: ");
                        String codiceAbbonamento = scanner.nextLine();

                        boolean isValido = abbonamentoDao.isAbbonamentoValido(codiceAbbonamento);

                        if (isValido) {
                            Abbonamento abbonamento = em.createQuery(
                                            "SELECT a FROM Abbonamento a WHERE a.codice = :codice", Abbonamento.class)
                                    .setParameter("codice", codiceAbbonamento)
                                    .getSingleResult();

                            System.out.println("L'abbonamento con codice " + codiceAbbonamento + " è valido.");
                            System.out.println("Dettagli dell'abbonamento:");
                            System.out.println("Tipo: " + abbonamento.getTipo());
                            System.out.println("Periodicità: " + abbonamento.getPeriodicita());
                            System.out.println("Data inizio: " + abbonamento.getValidoDa());
                            System.out.println("Data fine: " + abbonamento.getValidoA());
                            System.out.println("Punto di vendita: " + abbonamento.getPuntoVendita().getNome());
                            System.out.println("Utente: " + abbonamento.getUtente().getNome() + " " + abbonamento.getUtente().getCognome());
                        } else {
                            System.out.println("L'abbonamento con codice " + codiceAbbonamento + " è scaduto o non esiste.");
                        }
                        break;
                    case 2:
                        System.out.print("Inserisci l'ID della tessera (esistente): ");
                        Long tesseraIdCreazione = scanner.nextLong();
                        scanner.nextLine();

                        Tessera tesseraPerAbbonamento = tesseraDao.findById(tesseraIdCreazione);
                        if (tesseraPerAbbonamento == null) {
                            System.out.println("Tessera non trovata.");
                            break;
                        }

                        System.out.print("Inserisci la periodicità dell'abbonamento (MENSILE/SETTIMANALE): ");
                        String periodicitaInput = scanner.nextLine().trim().toUpperCase();
                        Periodicita periodicita;
                        try {
                            periodicita = Periodicita.valueOf(periodicitaInput);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Periodicità non valida.");
                            break;
                        }

                        System.out.print("Inserisci il tipo di mezzo (AUTOBUS/TRAM): ");
                        String tipoMezzoInput = scanner.nextLine().toUpperCase();
                        TipoMezzo tipoMezzo;
                        try {
                            tipoMezzo = TipoMezzo.valueOf(tipoMezzoInput);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Tipo di mezzo non valido.");
                            break;
                        }

                        List<PuntoVendita> puntiVenditaDisponibili = em.createQuery("SELECT p FROM PuntoVendita p", PuntoVendita.class).getResultList();
                        if (puntiVenditaDisponibili.isEmpty()) {
                            System.out.println("Nessun punto vendita disponibile.");
                            break;
                        }

                        System.out.println("Seleziona un punto vendita:");
                        for (int i = 0; i < puntiVenditaDisponibili.size(); i++) {
                            System.out.printf("%d) %s - Indirizzo: %s\n", i + 1, puntiVenditaDisponibili.get(i).getNome(), puntiVenditaDisponibili.get(i).getIndirizzo());
                        }
                        int idxPuntoVendita = scanner.nextInt() - 1;
                        scanner.nextLine();
                        if (idxPuntoVendita < 0 || idxPuntoVendita >= puntiVenditaDisponibili.size()) {
                            System.out.println("Punto vendita non valido.");
                            break;
                        }
                        PuntoVendita puntoVenditaSelezionato = puntiVenditaDisponibili.get(idxPuntoVendita);

                        em.getTransaction().begin();
                        Abbonamento nuovoAbbonamento = Abbonamento.creaAbbonamento(tesseraPerAbbonamento.getUtente(), periodicita, tipoMezzo, puntoVenditaSelezionato, tesseraPerAbbonamento);
                        tesseraPerAbbonamento.getListaAbbonamenti().add(nuovoAbbonamento);
                        em.persist(nuovoAbbonamento);
                        em.merge(tesseraPerAbbonamento);
                        em.getTransaction().commit();

                        System.out.println("Abbonamento creato con codice: " + nuovoAbbonamento.getCodice());
                        break;
                    case 0:
                        running = false;
                        System.out.println("Arrivederci!");
                        break;

                }

            }

        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }
}
