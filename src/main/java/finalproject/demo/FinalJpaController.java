/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalproject.demo;

import finalproject.demo.exceptions.NonexistentEntityException;
import finalproject.demo.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author lenovo
 */
public class FinalJpaController implements Serializable {

    private String persistenceUnitName;

    public FinalJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("finalproject_demo_jar_0.0.1-SNAPSHOTPU");
    
    public FinalJpaController(){
        
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Final final1) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(final1);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFinal(final1.getId()) != null) {
                throw new PreexistingEntityException("Final " + final1 + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Final final1) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            final1 = em.merge(final1);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = final1.getId();
                if (findFinal(id) == null) {
                    throw new NonexistentEntityException("The final with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Final final1;
            try {
                final1 = em.getReference(Final.class, id);
                final1.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The final1 with id " + id + " no longer exists.", enfe);
            }
            em.remove(final1);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Final> findFinalEntities() {
        return findFinalEntities(true, -1, -1);
    }

    public List<Final> findFinalEntities(int maxResults, int firstResult) {
        return findFinalEntities(false, maxResults, firstResult);
    }

    private List<Final> findFinalEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Final.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Final findFinal(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Final.class, id);
        } finally {
            em.close();
        }
    }

    public int getFinalCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Final> rt = cq.from(Final.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
