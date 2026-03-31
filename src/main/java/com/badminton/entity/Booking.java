package com.badminton.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Booking")
public class Booking {

    // Khóa chính
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Khách hàng đặt sân (Khóa ngoại)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // Sân được đặt (Khóa ngoại)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "court_id", nullable = false)
    private Court court;

    // Thời gian bắt đầu
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    // Thời gian kết thúc dự kiến
    @Column(name = "expected_end_time", nullable = false)
    private LocalDateTime expectedEndTime;

    // Trạng thái phiếu đặt (Pending, Playing, Completed, Cancelled)
    @Column(name = "status", length = 50)
    private String status = "Pending";

    // Constructor mặc định
    public Booking() {
    }

    // Constructor đầy đủ
    public Booking(Customer customer, Court court, LocalDateTime startTime, LocalDateTime expectedEndTime, String status) {
        this.customer = customer;
        this.court = court;
        this.startTime = startTime;
        this.expectedEndTime = expectedEndTime;
        this.status = status;
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    public Court getCourt() { return court; }
    public void setCourt(Court court) { this.court = court; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getExpectedEndTime() { return expectedEndTime; }
    public void setExpectedEndTime(LocalDateTime expectedEndTime) { this.expectedEndTime = expectedEndTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

