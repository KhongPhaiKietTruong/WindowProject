package com.badminton.view;

import javax.swing.*;
import java.awt.*;

public class MainForm extends JFrame {
    private JPanel contentPane;
    public MainForm() {
        setTitle("Hệ thống Quản lý Sân Cầu Lông");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Căn giữa mn hnh
        initComponents();
    }
    private void initComponents() {
        // Tạo MenuBar
        JMenuBar menuBar = new JMenuBar();
        // 1. Menu Danh mục
        JMenu menuDanhMuc = new JMenu("Danh mục");
        JMenuItem itemSan = new JMenuItem("Quản lý Sân");
        JMenuItem itemKhach = new JMenuItem("Quản lý Khách hàng");
        JMenuItem itemNhanVien = new JMenuItem("Quản lý Nhân viên");
        JMenuItem itemSanPham = new JMenuItem("Quản lý Dịch vụ");
        menuDanhMuc.add(itemSan);
        menuDanhMuc.add(itemKhach);
        menuDanhMuc.add(itemNhanVien);
        menuDanhMuc.add(itemSanPham);
        // 2. Menu Nghiệp vụ (Giao dịch)
        JMenu menuNghiepVu = new JMenu("Nghiệp vụ");
        JMenuItem itemDatSan = new JMenuItem("Đặt Sân (Booking)");
        JMenuItem itemThanhToan = new JMenuItem("Thanh Toán & Hóa Đơn");
        menuNghiepVu.add(itemDatSan);
        menuNghiepVu.add(itemThanhToan);
        // 3. Menu Bo co
        JMenu menuBaoCao = new JMenu("Báo cáo");
        JMenuItem itemDoanhThu = new JMenuItem("Thống kê Doanh Thu");
        menuBaoCao.add(itemDoanhThu);
        // 4. Hệ thống
        JMenu menuHeThong = new JMenu("Hệ thống");
        JMenuItem itemDangXuat = new JMenuItem("Đăng xuất");
        JMenuItem itemThoat = new JMenuItem("Thoát");
        menuHeThong.add(itemDangXuat);
        menuHeThong.add(itemThoat);
        menuBar.add(menuHeThong);
        menuBar.add(menuDanhMuc);
        menuBar.add(menuNghiepVu);
        menuBar.add(menuBaoCao);
        setJMenuBar(menuBar);
        // Panel chứa nội dung chnh
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        // Mn hnh Welcome mặc định
        JLabel lblWelcome = new JLabel("CHÀO MỪNG BẠN ĐẾN VỚI HỆ THỐNG QUẢN LÝ SÂN CẦU LÔNG", SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 26));
        contentPane.add(lblWelcome, BorderLayout.CENTER);
        // Gắn vo Frame
        setContentPane(contentPane);
        
        // Bắt sự kiện menu
        itemThoat.addActionListener(e -> System.exit(0));
        
        // Menu Danh mục
        itemSan.addActionListener(e -> switchForm(new CourtManagementForm()));
        itemKhach.addActionListener(e -> switchForm(new CustomerManagementForm()));
        itemSanPham.addActionListener(e -> switchForm(new ProductManagementForm()));
        itemNhanVien.addActionListener(e -> JOptionPane.showMessageDialog(this, "Quản lý Nhân Viên đang được phát triển!"));

        // Menu Nghiệp vụ
        itemDatSan.addActionListener(e -> switchForm(new BookingForm()));
        itemThanhToan.addActionListener(e -> switchForm(new PaymentForm()));
        
        // Menu Báo cáo
        itemDoanhThu.addActionListener(e -> switchForm(new RevenueForm()));
    }
    
    // Hm chuyển đổi cc mn hnh SubForm
    private void switchForm(JPanel newForm) {
        contentPane.removeAll(); // Xa form cũ
        contentPane.add(newForm, BorderLayout.CENTER); // Thm form mới
        contentPane.revalidate(); // Cập nhật lại UI
        contentPane.repaint();    // Vẽ lại UI
    }
}
