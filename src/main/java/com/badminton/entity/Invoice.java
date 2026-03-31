package com.badminton.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Invoice")
public class Invoice {

    // Khóa chính
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Phiếu đặt sân tương ứng (1-1)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false, unique = true)
    private Booking booking;

    // Nhân viên tạo hóa đơn
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    // Ngày tạo hóa đơn
    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    // Tiền thuê sân
    @Column(name = "court_fee", precision = 10, scale = 2)
    private Double courtFee = 0.0;

    // Tiền các dịch vụ kèm theo
    @Column(name = "service_fee", precision = 10, scale = 2)
    private Double serviceFee = 0.0;

    // Tiền được giảm giá
    @Column(name = "discount_amount", precision = 10, scale = 2)
    private Double discountAmount = 0.0;

    // Tổng tiền thanh toán
    @Column(name = "total_amount", precision = 10, scale = 2)
    private Double totalAmount = 0.0;

    // Trạng thái hóa đơn (Unpaid, Paid)
    @Column(name = "status", length = 50)
    private String status = "Unpaid";

    // Chi tiết các dịch vụ được sử dụng trong hóa đơn này
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceDetail> serviceDetails;

    // Constructor mặc định
    public Invoice() {
    }

    // Constructor đầy đủ
    public Invoice(Booking booking, Employee employee, Double courtFee, Double serviceFee, Double discountAmount, Double totalAmount, String status) {
        this.booking = booking;
        this.employee = employee;
        this.courtFee = courtFee;
        this.serviceFee = serviceFee;
        this.discountAmount = discountAmount;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }
    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
    public Double getCourtFee() { return courtFee; }
    public void setCourtFee(Double courtFee) { this.courtFee = courtFee; }
    public Double getServiceFee() { return serviceFee; }
    public void setServiceFee(Double serviceFee) { this.serviceFee = serviceFee; }
    public Double getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(Double discountAmount) { this.discountAmount = discountAmount; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public List<ServiceDetail> getServiceDetails() { return serviceDetails; }
    public void setServiceDetails(List<ServiceDetail> serviceDetails) { this.serviceDetails = serviceDetails; }
}

