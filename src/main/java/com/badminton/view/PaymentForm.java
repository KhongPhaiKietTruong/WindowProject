package com.badminton.view;

import javax.swing.*;
import java.awt.*;

public class PaymentForm extends JPanel {

    public PaymentForm() {
        initComponents();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tiêu đề
        JLabel lblTitle = new JLabel("THANH TOÁN & XUẤT HÓA ĐƠN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        this.add(lblTitle, BorderLayout.NORTH);

        // Vùng chọn Booking đợi thanh toán
        JPanel pnlBookingSelection = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlBookingSelection.add(new JLabel("Chọn Booking:"));
        JComboBox<String> cbBookings = new JComboBox<>(new String[]{"Booking #1 - Nguyễn Văn A - Sân 1", "Booking #2 - Trần Thị B - Sân 2"});
        pnlBookingSelection.add(cbBookings);
        JButton btnLoad = new JButton("Tải thông tin");
        pnlBookingSelection.add(btnLoad);

        // Vùng chi tiết hóa đơn
        JPanel pnlDetails = new JPanel(new GridLayout(6, 2, 5, 5));
        pnlDetails.setBorder(BorderFactory.createTitledBorder("Chi tiết Hóa đơn"));
        
        pnlDetails.add(new JLabel("Tiền Thuê Sân:"));
        JTextField txtCourtFee = new JTextField("0 đ");
        txtCourtFee.setEditable(false);
        pnlDetails.add(txtCourtFee);
        
        pnlDetails.add(new JLabel("Tiền Dịch vụ:"));
        JTextField txtServiceFee = new JTextField("0 đ");
        txtServiceFee.setEditable(false);
        pnlDetails.add(txtServiceFee);
        
        pnlDetails.add(new JLabel("Chiết Khấu (%):"));
        JTextField txtDiscount = new JTextField("0 đ");
        txtDiscount.setEditable(false);
        pnlDetails.add(txtDiscount);
        
        pnlDetails.add(new JLabel("TỔNG TIỀN THANH TOÁN:"));
        JTextField txtTotal = new JTextField("0 đ");
        txtTotal.setEditable(false);
        txtTotal.setFont(new Font("Arial", Font.BOLD, 16));
        txtTotal.setForeground(Color.RED);
        pnlDetails.add(txtTotal);

        // Nút Thanh Toán
        JPanel pnlButtons = new JPanel();
        JButton btnThanhToan = new JButton("Thanh toán & Xuất hóa đơn");
        btnThanhToan.setFont(new Font("Arial", Font.BOLD, 14));
        btnThanhToan.setBackground(new Color(50, 150, 250));
        btnThanhToan.setForeground(Color.WHITE);
        
        btnThanhToan.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Đã thanh toán và xuất hóa đơn thành công! (Chức năng gọi InvoiceService)");
        });
        
        pnlButtons.add(btnThanhToan);

        // Container giữa
        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.add(pnlBookingSelection, BorderLayout.NORTH);
        pnlCenter.add(pnlDetails, BorderLayout.CENTER);
        pnlCenter.add(pnlButtons, BorderLayout.SOUTH);

        this.add(pnlCenter, BorderLayout.CENTER);
    }
}
