-- Tạo database
CREATE DATABASE IF NOT EXISTS badminton_court_management;

USE badminton_court_management;

-- 1. Bảng Ca làm việc (Shift)
CREATE TABLE IF NOT EXISTS Shift (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL COMMENT 'Tên ca (Sáng, Chiều, Tối)',
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    price_multiplier DECIMAL(5,2) DEFAULT 1.0 COMMENT 'Hệ số giá sân theo ca'
);

-- 2. Bảng Nhân viên (Employee)
CREATE TABLE IF NOT EXISTS Employee (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    shift_id INT,
    FOREIGN KEY (shift_id) REFERENCES Shift(id) ON DELETE SET NULL
);

-- 3. Bảng Khách hàng (Customer)
CREATE TABLE IF NOT EXISTS Customer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) UNIQUE NOT NULL,
    member_type VARCHAR(50) DEFAULT 'Normal' COMMENT 'Loại thành viên (Normal, VIP)',
    discount_percent DECIMAL(5,2) DEFAULT 0.0 COMMENT 'Phần trăm giảm giá'
);

-- 4. Bảng Sân (Court)
CREATE TABLE IF NOT EXISTS Court (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    court_type VARCHAR(50) NOT NULL COMMENT 'Loại sân (Thảm, Gỗ...)',
    price_per_hour DECIMAL(10,2) NOT NULL COMMENT 'Đơn giá thuê/giờ'
);

-- 5. Bảng Sản phẩm/Dịch vụ (Product)
CREATE TABLE IF NOT EXISTS Product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50) COMMENT 'Loại (Nước uống, Thuê đồ...)',
    price DECIMAL(10,2) NOT NULL,
    stock INT DEFAULT 0 COMMENT 'Số lượng tồn kho'
);

-- 6. Bảng Phiếu đặt sân (Booking)
CREATE TABLE IF NOT EXISTS Booking (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    court_id INT NOT NULL,
    start_time DATETIME NOT NULL,
    expected_end_time DATETIME NOT NULL,
    status VARCHAR(50) DEFAULT 'Pending' COMMENT 'Trạng thái (Pending, Playing, Completed, Cancelled)',
    FOREIGN KEY (customer_id) REFERENCES Customer(id),
    FOREIGN KEY (court_id) REFERENCES Court(id)
);

-- 7. Bảng Hóa đơn (Invoice)
CREATE TABLE IF NOT EXISTS Invoice (
    id INT AUTO_INCREMENT PRIMARY KEY,
    booking_id INT UNIQUE NOT NULL,
    employee_id INT NOT NULL,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    court_fee DECIMAL(10,2) DEFAULT 0 COMMENT 'Tiền sân',
    service_fee DECIMAL(10,2) DEFAULT 0 COMMENT 'Tiền dịch vụ',
    discount_amount DECIMAL(10,2) DEFAULT 0 COMMENT 'Tiền giảm giá',
    total_amount DECIMAL(10,2) DEFAULT 0 COMMENT 'Tổng tiền thanh toán',
    status VARCHAR(50) DEFAULT 'Unpaid' COMMENT 'Trạng thái (Unpaid, Paid)',
    FOREIGN KEY (booking_id) REFERENCES Booking(id),
    FOREIGN KEY (employee_id) REFERENCES Employee(id)
);

-- 8. Bảng Chi tiết dịch vụ (ServiceDetail)
CREATE TABLE IF NOT EXISTS ServiceDetail (
    id INT AUTO_INCREMENT PRIMARY KEY,
    invoice_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    unit_price DECIMAL(10,2) NOT NULL COMMENT 'Giá tại thời điểm mua',
    total_price DECIMAL(10,2) NOT NULL COMMENT 'Tổng tiền = quantity * unit_price',
    FOREIGN KEY (invoice_id) REFERENCES Invoice(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES Product(id)
);

-- ==========================================
-- SCRIPT CHÈN DỮ LIỆU MẪU (SEED DATA)
-- ==========================================

-- Chèn dữ liệu bảng Shift
INSERT INTO Shift (name, start_time, end_time, price_multiplier) VALUES
('Ca Sáng', '06:00:00', '12:00:00', 1.0),
('Ca Chiều', '12:00:00', '18:00:00', 1.2),
('Ca Tối', '18:00:00', '22:00:00', 1.5),
('Ca Đêm', '22:00:00', '06:00:00', 0.8),
('Ca Cuối Tuần', '06:00:00', '22:00:00', 1.5);

-- Chèn dữ liệu bảng Employee
INSERT INTO Employee (name, phone, username, password, shift_id) VALUES
('Nguyễn Văn A', '0901234567', 'nva', '123456', 1),
('Trần Thị B', '0912345678', 'ttb', '123456', 2),
('Lê Văn C', '0923456789', 'lvc', '123456', 3),
('Phạm Thị D', '0934567890', 'ptd', '123456', 1),
('Hoàng Văn E', '0945678901', 'hve', '123456', 2);

-- Chèn dữ liệu bảng Customer
INSERT INTO Customer (name, phone, member_type, discount_percent) VALUES
('Khách Hàng 1', '0981111111', 'Normal', 0),
('Khách Hàng 2', '0982222222', 'Silver', 5.0),
('Khách Hàng 3', '0983333333', 'Gold', 10.0),
('Khách Hàng 4', '0984444444', 'VIP', 15.0),
('Khách Hàng 5', '0985555555', 'Normal', 0);

-- Chèn dữ liệu bảng Court
INSERT INTO Court (name, court_type, price_per_hour) VALUES
('Sân 1', 'Thảm', 80000),
('Sân 2', 'Thảm', 80000),
('Sân 3', 'Gỗ', 100000),
('Sân 4', 'Gỗ VIP', 120000),
('Sân 5', 'Thảm', 80000);

-- Chèn dữ liệu bảng Product
INSERT INTO Product (name, category, price, stock) VALUES
('Nước khoáng Lavie', 'Nước uống', 10000, 100),
('Nước tăng lực Redbull', 'Nước uống', 15000, 50),
('Thuê Vợt Yonex', 'Thuê đồ', 30000, 20),
('Ống cầu lông Vina', 'Bán lẻ', 180000, 30),
('Thuê Giày Cầu Lông', 'Thuê đồ', 25000, 15);

-- Chèn dữ liệu bảng Booking
INSERT INTO Booking (customer_id, court_id, start_time, expected_end_time, status) VALUES
(1, 1, '2023-10-01 08:00:00', '2023-10-01 10:00:00', 'Completed'),
(2, 3, '2023-10-01 14:00:00', '2023-10-01 16:00:00', 'Completed'),
(3, 4, '2023-10-01 18:00:00', '2023-10-01 20:00:00', 'Playing'),
(4, 2, '2023-10-02 07:00:00', '2023-10-02 09:00:00', 'Pending'),
(5, 5, '2023-10-02 19:00:00', '2023-10-02 21:00:00', 'Pending');

-- Chèn dữ liệu bảng Invoice
-- Giả sử Invoice 1 (Booking 1) và Invoice 2 (Booking 2) đã được thanh toán
INSERT INTO Invoice (booking_id, employee_id, created_date, court_fee, service_fee, discount_amount, total_amount, status) VALUES
(1, 1, '2023-10-01 10:05:00', 160000, 20000, 0, 180000, 'Paid'),
(2, 2, '2023-10-01 16:10:00', 200000, 45000, 10000, 235000, 'Paid'),
(3, 3, '2023-10-01 20:00:00', 240000, 0, 24000, 216000, 'Unpaid'),
(4, 1, '2023-10-02 09:00:00', 160000, 0, 24000, 136000, 'Unpaid'),
(5, 2, '2023-10-02 21:00:00', 160000, 0, 0, 160000, 'Unpaid');

-- Chèn dữ liệu bảng ServiceDetail
INSERT INTO ServiceDetail (invoice_id, product_id, quantity, unit_price, total_price) VALUES
(1, 1, 2, 10000, 20000),         -- Hóa đơn 1 mua 2 chai Lavie
(2, 2, 1, 15000, 15000),         -- Hóa đơn 2 mua 1 Redbull
(2, 3, 1, 30000, 30000),         -- Hóa đơn 2 thuê 1 vợt
(3, 1, 4, 10000, 40000),         -- Hóa đơn 3 mua 4 chai Lavie
(4, 4, 1, 180000, 180000);       -- Hóa đơn 4 mua 1 ống cầu

