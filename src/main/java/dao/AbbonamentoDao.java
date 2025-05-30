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

    public boolean isAbbonamentoValido(String codiceAbbonamento) {
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(a) FROM Abbonamento a " +
                            "WHERE a.codice = :codice AND CURRENT_DATE BETWEEN a.validoDa AND a.validoA",
                    Long.class
            );
            query.setParameter("codice", codiceAbbonamento);
            Long result = query.getSingleResult();
            return result > 0;
        } catch (Exception e) {
            return false;
        }
    }

}

