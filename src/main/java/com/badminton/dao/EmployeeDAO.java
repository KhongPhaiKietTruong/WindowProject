package com.badminton.dao;
import com.badminton.entity.Employee;
import com.badminton.util.JpaUtil;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
public class EmployeeDAO {
    public void save(Employee employee) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (employee.getId() == null) {
                em.persist(employee);
            } else {
                em.merge(employee);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Lỗi lưu nhân viên (có thể trùng username?)");
        } finally {
            em.close();
        }
    }
    public void delete(Integer id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Employee employee = em.find(Employee.class, id);
            if (employee != null) {
                em.remove(employee);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Không thể xóa nhân viên này!");
        } finally {
            em.close();
        }
    }
    public List<Employee> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Employee> query = em.createQuery("SELECT e FROM Employee e", Employee.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
