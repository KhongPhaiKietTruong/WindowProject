package com.badminton.dao;

import com.badminton.entity.Booking;
import com.badminton.util.JpaUtil;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class BookingDAO {
    public void save(Booking booking) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (booking.getId() == null) {
                em.persist(booking);
            } else {
                em.merge(booking);
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

    public List<Booking> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Booking> query = em.createQuery("SELECT b FROM Booking b", Booking.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}

