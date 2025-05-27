package dao;

import entities.TitoloViaggio;
import jakarta.persistence.EntityManager;

public class TitoloViaggioDao {
    private EntityManager em;

    public TitoloViaggioDao (EntityManager em) {
        this.em = em;
    }

    public void save(TitoloViaggio  titoloViaggio) {
        em.getTransaction().begin();
        em.persist(titoloViaggio);
        em.getTransaction().commit();
    }

    public TitoloViaggio  findById(Long id) {
        return em.find(TitoloViaggio .class, id);
    }

    // Validità abbonamento dato un numero di tessera
    // Numero biglietti e abbonamenti: in un determinato periodo di tempo, in totale, per punto di emissione (se vogliamo possiamo suddividere in bus e tram)
    // Tempo effettivo di percorrenza tratta
    // Tempo medio effettivo di percorrenza di una tratta da parte di un mezzo
}
