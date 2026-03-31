package com.badminton.entity;

import javax.persistence.*;

@Entity
@Table(name = "ServiceDetail")
public class ServiceDetail {

    // Khóa chính
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Hóa đơn (Khóa ngoại)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    // Sản phẩm/dịch vụ (Khóa ngoại)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Số lượng
    @Column(name = "quantity", nullable = false)
    private Integer quantity = 1;

    // Đơn giá tại thời điểm mua
    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private Double unitPrice;

    // Tổng tiền (số lượng * đơn giá)
    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private Double totalPrice;

    // Constructor mặc định
    public ServiceDetail() {
    }

    // Constructor đầy đủ
    public ServiceDetail(Invoice invoice, Product product, Integer quantity, Double unitPrice, Double totalPrice) {
        this.invoice = invoice;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Invoice getInvoice() { return invoice; }
    public void setInvoice(Invoice invoice) { this.invoice = invoice; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(Double unitPrice) { this.unitPrice = unitPrice; }
    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }
}

