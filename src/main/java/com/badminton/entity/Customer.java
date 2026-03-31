package com.badminton.entity;

import javax.persistence.*;

@Entity
@Table(name = "Customer")
public class Customer {

    // Khóa chính
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Tên khách hàng
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    // Số điện thoại (Duy nhất)
    @Column(name = "phone", nullable = false, unique = true, length = 20)
    private String phone;

    // Loại thành viên (ví dụ: Normal, VIP)
    @Column(name = "member_type", length = 50)
    private String memberType = "Normal";

    // Phần trăm giảm giá
    @Column(name = "discount_percent", precision = 5, scale = 2)
    private Double discountPercent = 0.0;

    // Constructor mặc định
    public Customer() {
    }

    // Constructor đầy đủ
    public Customer(String name, String phone, String memberType, Double discountPercent) {
        this.name = name;
        this.phone = phone;
        this.memberType = memberType;
        this.discountPercent = discountPercent;
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getMemberType() { return memberType; }
    public void setMemberType(String memberType) { this.memberType = memberType; }
    public Double getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(Double discountPercent) { this.discountPercent = discountPercent; }
}

