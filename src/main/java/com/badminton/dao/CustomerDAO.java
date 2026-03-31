package com.badminton.dao;
import com.badminton.entity.Customer;
import com.badminton.util.JpaUtil;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;
public class CustomerDAO {
    public void save(Customer customer) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (customer.getId() == null) {
                em.persist(customer);
            } else {
                em.merge(customer);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
    public void delete(Integer id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Customer customer = em.find(Customer.class, id);
            if (customer != null) {
                em.remove(customer);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Không thể xóa khách hàng vì có dữ liệu liên quan!");
        } finally {
            em.close();
        }
    }
    public List<Customer> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Customer> query = em.createQuery("SELECT c FROM Customer c", Customer.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    public Customer findByPhone(String phone) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Customer> query = em.createQuery("SELECT c FROM Customer c WHERE c.phone = :phone", Customer.class);
            query.setParameter("phone", phone);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}
