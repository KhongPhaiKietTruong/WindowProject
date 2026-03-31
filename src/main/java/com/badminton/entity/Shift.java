package com.badminton.entity;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "Shift")
public class Shift {
    
    // Khóa chính
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Tên ca làm việc
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    // Giờ bắt đầu
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    // Giờ kết thúc
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    // Hệ số giá sân theo ca
    @Column(name = "price_multiplier", precision = 5, scale = 2)
    private Double priceMultiplier = 1.0;

    // Danh sách nhân viên làm ca này
    @OneToMany(mappedBy = "shift", fetch = FetchType.LAZY)
    private List<Employee> employees;

    // Constructor mặc định
    public Shift() {
    }

    // Constructor đầy đủ
    public Shift(String name, LocalTime startTime, LocalTime endTime, Double priceMultiplier) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.priceMultiplier = priceMultiplier;
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    public Double getPriceMultiplier() { return priceMultiplier; }
    public void setPriceMultiplier(Double priceMultiplier) { this.priceMultiplier = priceMultiplier; }
    public List<Employee> getEmployees() { return employees; }
    public void setEmployees(List<Employee> employees) { this.employees = employees; }
}

