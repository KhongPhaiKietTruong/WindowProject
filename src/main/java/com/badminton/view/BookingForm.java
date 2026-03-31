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

    private JTextField txtCustomerName;
    private JTextField txtCustomerPhone;
    private JComboBox<String> cbCourt;
    private JTextField txtStartTime; // Format: yyyy-MM-dd HH:mm
    private JTextField txtEndTime;   // Format: yyyy-MM-dd HH:mm

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

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin đặt sân"));

        formPanel.add(new JLabel("Tên khách hàng:"));
        txtCustomerName = new JTextField();
        formPanel.add(txtCustomerName);

        formPanel.add(new JLabel("SĐT (Dùng để tìm KH cũ):"));
        txtCustomerPhone = new JTextField();
        formPanel.add(txtCustomerPhone);

        formPanel.add(new JLabel("Sân:"));
        cbCourt = new JComboBox<>();
        formPanel.add(cbCourt);

        formPanel.add(new JLabel("Giờ chơi (yyyy-MM-dd HH:mm):"));
        txtStartTime = new JTextField(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        formPanel.add(txtStartTime);

        formPanel.add(new JLabel("Giờ kết thúc dự kiến (yyyy-MM-dd HH:mm):"));
        txtEndTime = new JTextField(LocalDateTime.now().plusHours(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        formPanel.add(txtEndTime);

        // Auto load customer info when phone loses focus
        txtCustomerPhone.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                String phone = txtCustomerPhone.getText().trim();
                if (!phone.isEmpty()) {
                    Customer c = customerDAO.findByPhone(phone);
                    if (c != null) {
                        txtCustomerName.setText(c.getName());
                    }
                }
            }
        });

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
        courts = courtDAO.findAll();
        cbCourt.removeAllItems();
        for (Court c : courts) {
            cbCourt.addItem(c.getId() + " - " + c.getName() + " (" + c.getCourtType() + ")");
        }
    }

    private void processBooking() {
        try {
            String customerName = txtCustomerName.getText().trim();
            String customerPhone = txtCustomerPhone.getText().trim();

            if (customerName.isEmpty() || customerPhone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập Tên và SĐT khách hàng!");
                return;
            }

            int courtIndex = cbCourt.getSelectedIndex();
            if (courtIndex < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn sân!");
                return;
            }

            Customer customer = customerDAO.findByPhone(customerPhone);
            if (customer == null) {
                customer = new Customer();
                customer.setName(customerName);
                customer.setPhone(customerPhone);
                customer.setMemberType("Normal");
                customer.setDiscountPercent(0.0);
                customerDAO.save(customer);
                
                // Get again to acquire ID
                customer = customerDAO.findByPhone(customerPhone);
            }

            Court selectedCourt = courts.get(courtIndex);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime startTime = LocalDateTime.parse(txtStartTime.getText().trim(), formatter);
            LocalDateTime endTime = LocalDateTime.parse(txtEndTime.getText().trim(), formatter);

            if (!endTime.isAfter(startTime)) {
                JOptionPane.showMessageDialog(this, "Giờ kết thúc phải sau giờ bắt đầu!");
                return;
            }

            bookingController.handleBookingAction(this, customer, selectedCourt, startTime, endTime);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi định dạng ngày giờ! Hãy nhập theo chuẩn yyyy-MM-dd HH:mm");
        }
    }
}
