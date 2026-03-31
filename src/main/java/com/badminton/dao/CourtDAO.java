package com.badminton.dao;

import com.badminton.entity.Court;
import com.badminton.util.JpaUtil;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

public class CourtDAO {

    // Thêm mới hoặc cập nhật sân
    public void save(Court court) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (court.getId() == null) {
                em.persist(court);
            } else {
                em.merge(court);
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

    // Lấy danh sách tất cả các sân
    public List<Court> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Court> query = em.createQuery("SELECT c FROM Court c", Court.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Tìm kiếm sân trống trong một khoảng thời gian (start -> end)
    // Sân trống nếu không có Booking nào thỏa mãn: 
    //   (Booking có trạng thái Playing, Pending) VÀ
    //   (booking.startTime < end AND booking.expectedEndTime > start)
    public List<Court> findAvailableCourts(LocalDateTime start, LocalDateTime end) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            String jpql = "SELECT c FROM Court c WHERE c.id NOT IN (" +
                          "  SELECT b.court.id FROM Booking b " +
                          "  WHERE b.status IN ('Pending', 'Playing') " +
                          "  AND b.startTime < :endTime AND b.expectedEndTime > :startTime" +
                          ")";
            TypedQuery<Court> query = em.createQuery(jpql, Court.class);
            query.setParameter("startTime", start);
            query.setParameter("endTime", end);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}

