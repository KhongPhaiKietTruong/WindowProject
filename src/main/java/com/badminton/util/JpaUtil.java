package com.badminton.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaUtil {
    private static final EntityManagerFactory emf;

    static {
        try {
            emf = Persistence.createEntityManagerFactory("BadmintonCourtPU");
        } catch (Throwable ex) {
            System.err.println("Khởi tạo EntityManagerFactory thất bại: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    // Lấy đối tượng EntityManager để thao tác CSDL
    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}

