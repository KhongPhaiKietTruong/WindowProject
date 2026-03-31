package com.badminton.view;

import com.badminton.dao.InvoiceDAO;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RevenueForm extends JPanel {

    private InvoiceDAO invoiceDAO;
    
    private JTextField txtStartDate;
    private JTextField txtEndDate;
    private JLabel lblTotalRevenue;

    public RevenueForm() {
        this.invoiceDAO = new InvoiceDAO();
        initComponents();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTitle = new JLabel("THỐNG KÊ DOANH THU", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        this.add(lblTitle, BorderLayout.NORTH);

        JPanel pnlFilter = new JPanel(new FlowLayout());
        
        pnlFilter.add(new JLabel("Từ ngày (yyyy-MM-dd HH:mm):"));
        txtStartDate = new JTextField(LocalDateTime.now().minusDays(30).format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00")), 15);
        pnlFilter.add(txtStartDate);

        pnlFilter.add(new JLabel("Đến ngày:"));
        txtEndDate = new JTextField(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59")), 15);
        pnlFilter.add(txtEndDate);

        JButton btnFilter = new JButton("Thống kê");
        btnFilter.addActionListener(e -> calculateRevenue());
        pnlFilter.add(btnFilter);

        JPanel pnlResult = new JPanel();
        lblTotalRevenue = new JLabel("TỔNG DOANH THU: 0 đ");
        lblTotalRevenue.setFont(new Font("Arial", Font.BOLD, 20));
        lblTotalRevenue.setForeground(Color.RED);
        pnlResult.add(lblTotalRevenue);

        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.add(pnlFilter, BorderLayout.NORTH);
        pnlCenter.add(pnlResult, BorderLayout.CENTER);

        this.add(pnlCenter, BorderLayout.CENTER);
    }

    private void calculateRevenue() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime start = LocalDateTime.parse(txtStartDate.getText().trim(), formatter);
            LocalDateTime end = LocalDateTime.parse(txtEndDate.getText().trim(), formatter);

            Double revenue = invoiceDAO.getRevenueBetween(start, end);
            lblTotalRevenue.setText("TỔNG DOANH THU: " + String.format("%.0f", revenue) + " VNĐ");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi định dạng ngày giờ! Chuẩn: yyyy-MM-dd HH:mm");
        }
    }
}
