package com.badminton.entity;

import javax.persistence.*;

@Entity
@Table(name = "Employee")
public class Employee {

    // Khóa chính
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Tên nhân viên
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    // Số điện thoại
    @Column(name = "phone", length = 20)
    private String phone;

    // Tên đăng nhập
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    // Mật khẩu
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    // Ca làm việc (Khóa ngoại)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_id")
    private Shift shift;

    // Constructor mặc định
    public Employee() {
    }

    // Constructor đầy đủ
    public Employee(String name, String phone, String username, String password, Shift shift) {
        this.name = name;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.shift = shift;
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Shift getShift() { return shift; }
    public void setShift(Shift shift) { this.shift = shift; }
}

