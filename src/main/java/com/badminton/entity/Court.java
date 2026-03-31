package com.badminton.entity;

import javax.persistence.*;

@Entity
@Table(name = "Court")
public class Court {

    // Khóa chính
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Tên sân
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    // Loại sân (Thảm, Gỗ, v.v.)
    @Column(name = "court_type", nullable = false, length = 50)
    private String courtType;

    // Đơn giá thuê trên 1 giờ
    @Column(name = "price_per_hour", nullable = false, precision = 10, scale = 2)
    private Double pricePerHour;

    // Constructor mặc định
    public Court() {
    }

    // Constructor đầy đủ
    public Court(String name, String courtType, Double pricePerHour) {
        this.name = name;
        this.courtType = courtType;
        this.pricePerHour = pricePerHour;
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCourtType() { return courtType; }
    public void setCourtType(String courtType) { this.courtType = courtType; }
    public Double getPricePerHour() { return pricePerHour; }
    public void setPricePerHour(Double pricePerHour) { this.pricePerHour = pricePerHour; }
}

