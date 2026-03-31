package com.badminton.view;

import com.badminton.dao.BookingDAO;
import com.badminton.dao.EmployeeDAO;
import com.badminton.entity.Booking;
import com.badminton.entity.Employee;
import com.badminton.entity.Invoice;
import com.badminton.service.InvoiceService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PaymentForm extends JPanel {

    private BookingDAO bookingDAO;
    private EmployeeDAO employeeDAO;
    private InvoiceService invoiceService;

    private JComboBox<BookingWrapper> cbBookings;
    private JComboBox<EmployeeWrapper> cbEmployees;
    private JTextField txtCourtFee;
    private JTextField txtServiceFee;
    private JTextField txtDiscount;
    private JTextField txtTotal;
    
    private List<Booking> unpaidBookings;
    private List<Employee> employees;

    public PaymentForm() {
        this.bookingDAO = new BookingDAO();
        this.employeeDAO = new EmployeeDAO();
        this.invoiceService = new InvoiceService();
        initComponents();
        loadData();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tiêu đề
        JLabel lblTitle = new JLabel("THANH TOÁN & XUẤT HÓA ĐƠN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        this.add(lblTitle, BorderLayout.NORTH);

        // Vùng chọn Booking và Nhân viên
        JPanel pnlSelection = new JPanel(new GridLayout(2, 2, 10, 10));
        pnlSelection.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        pnlSelection.add(new JLabel("Chọn Booking (Chưa thanh toán):"));
        cbBookings = new JComboBox<>();
        pnlSelection.add(cbBookings);
        
        pnlSelection.add(new JLabel("Nhân viên thu ngân:"));
        cbEmployees = new JComboBox<>();
        pnlSelection.add(cbEmployees);

        JButton btnLoad = new JButton("Tải & Tính toán thông tin");
        btnLoad.addActionListener(e -> updateInvoiceDetails());

        // Vùng chi tiết hóa đơn
        JPanel pnlDetails = new JPanel(new GridLayout(4, 2, 5, 5));
        pnlDetails.setBorder(BorderFactory.createTitledBorder("Chi tiết Hóa đơn dự kiến"));
        
        pnlDetails.add(new JLabel("Tiền Thuê Sân:"));
        txtCourtFee = new JTextField("0 VNĐ");
        txtCourtFee.setEditable(false);
        pnlDetails.add(txtCourtFee);
        
        pnlDetails.add(new JLabel("Tiền Dịch vụ:"));
        txtServiceFee = new JTextField("0 VNĐ");
        txtServiceFee.setEditable(false);
        pnlDetails.add(txtServiceFee);
        
        pnlDetails.add(new JLabel("Chiết Khấu:"));
        txtDiscount = new JTextField("0 VNĐ");
        txtDiscount.setEditable(false);
        pnlDetails.add(txtDiscount);
        
        pnlDetails.add(new JLabel("TỔNG TIỀN THANH TOÁN:"));
        txtTotal = new JTextField("0 VNĐ");
        txtTotal.setEditable(false);
        txtTotal.setFont(new Font("Arial", Font.BOLD, 16));
        txtTotal.setForeground(Color.RED);
        pnlDetails.add(txtTotal);

        // Nút Thanh Toán
        JPanel pnlButtons = new JPanel();
        JButton btnThanhToan = new JButton("Xác nhận Thanh toán & Xuất hóa đơn");
        btnThanhToan.setFont(new Font("Arial", Font.BOLD, 14));
        btnThanhToan.setBackground(new Color(50, 150, 250));
        btnThanhToan.setForeground(Color.WHITE);
        
        btnThanhToan.addActionListener(e -> processPayment());
        
        pnlButtons.add(btnLoad);
        pnlButtons.add(btnThanhToan);

        // Container giữa
        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.add(pnlSelection, BorderLayout.NORTH);
        pnlCenter.add(pnlDetails, BorderLayout.CENTER);
        pnlCenter.add(pnlButtons, BorderLayout.SOUTH);

        this.add(pnlCenter, BorderLayout.CENTER);
    }

    private void loadData() {
        unpaidBookings = bookingDAO.findUnpaidBookings();
        cbBookings.removeAllItems();
        for (Booking b : unpaidBookings) {
            cbBookings.addItem(new BookingWrapper(b));
        }

        employees = employeeDAO.findAll();
        cbEmployees.removeAllItems();
        for (Employee e : employees) {
            cbEmployees.addItem(new EmployeeWrapper(e));
        }
    }

    private void updateInvoiceDetails() {
        BookingWrapper selectedBookingWrapper = (BookingWrapper) cbBookings.getSelectedItem();
        EmployeeWrapper selectedEmployeeWrapper = (EmployeeWrapper) cbEmployees.getSelectedItem();
        
        if (selectedBookingWrapper == null) return;

        Booking booking = selectedBookingWrapper.booking;
        Employee employee = selectedEmployeeWrapper != null ? selectedEmployeeWrapper.employee : null;

        Invoice tempInvoice = new Invoice();
        tempInvoice.setBooking(booking);
        tempInvoice.setEmployee(employee);
        
        invoiceService.calculateInvoiceTurnover(tempInvoice);

        txtCourtFee.setText(String.format("%.0f VNĐ", tempInvoice.getCourtFee()));
        txtServiceFee.setText(String.format("%.0f VNĐ", tempInvoice.getServiceFee()));
        txtDiscount.setText(String.format("%.0f VNĐ", tempInvoice.getDiscountAmount()));
        txtTotal.setText(String.format("%.0f VNĐ", tempInvoice.getTotalAmount()));
    }

    private void processPayment() {
        BookingWrapper selectedBooking = (BookingWrapper) cbBookings.getSelectedItem();
        EmployeeWrapper selectedEmployee = (EmployeeWrapper) cbEmployees.getSelectedItem();

        if (selectedBooking == null || selectedEmployee == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đầy đủ Booking và Nhân viên!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận thanh toán cho " + selectedBooking.booking.getCustomer().getName() + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            Invoice invoice = new Invoice();
            invoice.setBooking(selectedBooking.booking);
            invoice.setEmployee(selectedEmployee.employee);
            invoice.setStatus("Paid");

            invoiceService.createAndSaveInvoice(invoice);
            
            // Cập nhật trạng thái booking thành Completed nếu chưa
            Booking b = selectedBooking.booking;
            b.setStatus("Completed");
            bookingDAO.save(b);

            JOptionPane.showMessageDialog(this, "Thanh toán thành công!");
            loadData(); // Refresh list
            clearFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xử lý thanh toán: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void clearFields() {
        txtCourtFee.setText("0 VNĐ");
        txtServiceFee.setText("0 VNĐ");
        txtDiscount.setText("0 VNĐ");
        txtTotal.setText("0 VNĐ");
    }

    // Wrapper classes for JComboBox
    private class BookingWrapper {
        Booking booking;
        BookingWrapper(Booking b) { this.booking = b; }
        @Override
        public String toString() {
            return "ID: " + booking.getId() + " - " + booking.getCustomer().getName() + " - " + booking.getCourt().getName();
        }
    }

    private class EmployeeWrapper {
        Employee employee;
        EmployeeWrapper(Employee e) { this.employee = e; }
        @Override
        public String toString() {
            return employee.getName() + " (" + employee.getUsername() + ")";
        }
    }
}
