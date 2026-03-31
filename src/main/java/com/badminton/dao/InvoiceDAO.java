package com.badminton.dao;

import com.badminton.entity.Invoice;
import com.badminton.util.JpaUtil;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

public class InvoiceDAO {

    // Thêm mới hoặc cập nhật hóa đơn
    public void save(Invoice invoice) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (invoice.getId() == null) {
                em.persist(invoice);
            } else {
                em.merge(invoice);
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

    // Lấy tất cả hóa đơn
    public List<Invoice> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Invoice> query = em.createQuery("SELECT i FROM Invoice i", Invoice.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Thống kê doanh thu trong một khoảng thời gian (từ ngày - đến ngày)
    // Chỉ tính các hóa đơn đã thanh toán (Paid)
    public Double getRevenueBetween(LocalDateTime start, LocalDateTime end) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            String jpql = "SELECT SUM(i.totalAmount) FROM Invoice i " +
                          "WHERE i.status = 'Paid' " +
                          "AND i.createdDate >= :start AND i.createdDate <= :end";
            TypedQuery<Double> query = em.createQuery(jpql, Double.class);
            query.setParameter("start", start);
            query.setParameter("end", end);
            Double result = query.getSingleResult();
            return result != null ? result : 0.0;
        } finally {
            em.close();
        }
    }
}

