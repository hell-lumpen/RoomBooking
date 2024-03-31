package org.mai.roombooking.repositories;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class RuleExceptionRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insertWithQuery(Long ruleId, Long exceptionId) {
        entityManager.createNativeQuery("INSERT INTO rule_exception (rule_id, exception_id) VALUES (?,?)")
                .setParameter(1, ruleId)
                .setParameter(2, exceptionId)
                .executeUpdate();
    }

}
