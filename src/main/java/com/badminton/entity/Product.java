package com.badminton.entity;

import javax.persistence.*;

@Entity
@Table(name = "Product")
public class Product {

    // Khóa chính
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Tên sản phẩm/dịch vụ
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    // Danh mục (Nước uống, Thuê đồ, v.v.)
    @Column(name = "category", length = 50)
    private String category;

    // Giá sản phẩm
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private Double price;

    // Số lượng tồn kho
    @Column(name = "stock")
    private Integer stock = 0;

    // Constructor mặc định
    public Product() {
    }

    // Constructor đầy đủ
    public Product(String name, String category, Double price, Integer stock) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
}

