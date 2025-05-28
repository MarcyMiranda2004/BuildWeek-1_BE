package dao;

import entities.Abbonamento;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.UUID;

public class AbbonamentoDao {
    private EntityManager em;

    public AbbonamentoDao(EntityManager em) {
        this.em = em;
    }

    public void save(Abbonamento abbonamento) {
        em.getTransaction().begin();
        em.persist(abbonamento);
        em.getTransaction().commit();
    }

    public Abbonamento findById(Long id) {
        return em.find(Abbonamento.class, id);
    }

    public boolean isAbbonamentoValido(String numeroTessera) {
        try {
            UUID codiceUUID = UUID.fromString(numeroTessera);
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(a) FROM Abbonamento a JOIN a.tessera t " +
                            "WHERE t.codice = :codice AND CURRENT_DATE BETWEEN a.validoDa AND a.validoA",
                    Long.class
            );
            query.setParameter("codice", codiceUUID);
            Long result = query.getSingleResult();
            return result > 0;
        } catch (IllegalArgumentException e) {
            return false; // Gestione del caso in cui il numeroTessera non sia un UUID valido
        }
    }
}

