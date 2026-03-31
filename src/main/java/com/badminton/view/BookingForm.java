package com.badminton.view;

import com.badminton.controller.BookingController;
import com.badminton.dao.CourtDAO;
import com.badminton.dao.CustomerDAO;
import com.badminton.entity.Court;
import com.badminton.entity.Customer;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BookingForm extends JPanel {

    private CourtDAO courtDAO;
    private CustomerDAO customerDAO;
    private BookingController bookingController;

    private JComboBox<String> cbCustomer;
    private JComboBox<String> cbCourt;
    private JTextField txtStartTime; // Format: yyyy-MM-dd HH:mm
    private JTextField txtEndTime;   // Format: yyyy-MM-dd HH:mm

    private List<Customer> customers;
    private List<Court> courts;

    public BookingForm() {
        this.courtDAO = new CourtDAO();
        this.customerDAO = new CustomerDAO();
        this.bookingController = new BookingController();
        initComponents();
        loadData();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTitle = new JLabel("ĐẶT SÂN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        this.add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin đặt sân"));

        formPanel.add(new JLabel("Khách hàng:"));
        cbCustomer = new JComboBox<>();
        formPanel.add(cbCustomer);

        formPanel.add(new JLabel("Sân:"));
        cbCourt = new JComboBox<>();
        formPanel.add(cbCourt);

        formPanel.add(new JLabel("Giờ chơi (yyyy-MM-dd HH:mm):"));
        // Mặc định là giờ hiện tại
        txtStartTime = new JTextField(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        formPanel.add(txtStartTime);

        formPanel.add(new JLabel("Giờ kết thúc dự kiến (yyyy-MM-dd HH:mm):"));
        // Mặc định là hiện tại + 1 tiếng
        txtEndTime = new JTextField(LocalDateTime.now().plusHours(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        formPanel.add(txtEndTime);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(formPanel, BorderLayout.NORTH);

        JPanel btnPanel = new JPanel();
        JButton btnBook = new JButton("Xác nhận đặt sân");
        btnBook.setBackground(new Color(50, 150, 250));
        btnBook.setForeground(Color.WHITE);
        btnBook.setFont(new Font("Arial", Font.BOLD, 16));

        btnBook.addActionListener(e -> processBooking());
        btnPanel.add(btnBook);
        
        centerPanel.add(btnPanel, BorderLayout.CENTER);

        this.add(centerPanel, BorderLayout.CENTER);
    }

    private void loadData() {
        customers = customerDAO.findAll();
        cbCustomer.removeAllItems();
        for (Customer c : customers) {
            cbCustomer.addItem(c.getId() + " - " + c.getName());
        }

        courts = courtDAO.findAll();
        cbCourt.removeAllItems();
        for (Court c : courts) {
            cbCourt.addItem(c.getId() + " - " + c.getName() + " (" + c.getCourtType() + ")");
        }
    }

    private void processBooking() {
        try {
            int customerIndex = cbCustomer.getSelectedIndex();
            int courtIndex = cbCourt.getSelectedIndex();
            
            if (customerIndex < 0 || courtIndex < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khách và sân!");
                return;
            }

            Customer selectedCustomer = customers.get(customerIndex);
            Court selectedCourt = courts.get(courtIndex);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime startTime = LocalDateTime.parse(txtStartTime.getText().trim(), formatter);
            LocalDateTime endTime = LocalDateTime.parse(txtEndTime.getText().trim(), formatter);

            if (!endTime.isAfter(startTime)) {
                JOptionPane.showMessageDialog(this, "Giờ kết thúc phải sau giờ bắt đầu!");
                return;
            }

            // Gọi Controller xử lý nghiệp vụ đặt sân
            bookingController.handlBookingAction(this, selectedCustomer, selectedCourt, startTime, endTime);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi định dạng ngày giờ! Hãy nhập theo chuẩn yyyy-MM-dd HH:mm");
        }
    }
}
