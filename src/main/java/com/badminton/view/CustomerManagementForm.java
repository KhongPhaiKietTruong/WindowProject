package com.badminton.view;
import com.badminton.dao.CustomerDAO;
import com.badminton.entity.Customer;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
public class CustomerManagementForm extends JPanel {
    private CustomerDAO customerDAO;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtId;
    private JTextField txtName;
    private JTextField txtPhone;
    private JComboBox<String> cbMemberType;
    private JTextField txtDiscount;
    public CustomerManagementForm() {
        this.customerDAO = new CustomerDAO();
        initComponents();
        loadData();
    }
    private void initComponents() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel lblTitle = new JLabel("QUẢN LÝ KHÁCH HÀNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        this.add(lblTitle, BorderLayout.NORTH);
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin Khách hàng"));
        formPanel.add(new JLabel("Mã KH (Tự động):"));
        txtId = new JTextField();
        txtId.setEditable(false);
        formPanel.add(txtId);
        formPanel.add(new JLabel("Tên Khách hàng:"));
        txtName = new JTextField();
        formPanel.add(txtName);
        formPanel.add(new JLabel("Số điện thoại:"));
        txtPhone = new JTextField();
        formPanel.add(txtPhone);
        formPanel.add(new JLabel("Loại Khách:"));
        cbMemberType = new JComboBox<>(new String[]{"Normal", "VIP"});
        formPanel.add(cbMemberType);
        formPanel.add(new JLabel("Chiết khấu (%):"));
        txtDiscount = new JTextField("0");
        formPanel.add(txtDiscount);
        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Thêm/Cập nhật");
        JButton btnClear = new JButton("Làm mới");
        btnAdd.addActionListener(e -> saveCustomer());
        btnClear.addActionListener(e -> clearForm());
        btnPanel.add(btnAdd);
        btnPanel.add(btnClear);
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(btnPanel, BorderLayout.SOUTH);
        String[] columnNames = {"ID", "Tên KH", "SĐT", "Loại Khách", "Chiết Khấu (%)"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                txtId.setText(tableModel.getValueAt(row, 0).toString());
                txtName.setText(tableModel.getValueAt(row, 1).toString());
                txtPhone.setText(tableModel.getValueAt(row, 2).toString());
                cbMemberType.setSelectedItem(tableModel.getValueAt(row, 3).toString());
                txtDiscount.setText(tableModel.getValueAt(row, 4).toString());
            }
        });
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel centerContainer = new JPanel(new BorderLayout(5, 5));
        centerContainer.add(topPanel, BorderLayout.NORTH);
        centerContainer.add(scrollPane, BorderLayout.CENTER);
        this.add(centerContainer, BorderLayout.CENTER);
    }
    private void loadData() {
        tableModel.setRowCount(0);
        List<Customer> customers = customerDAO.findAll();
        for (Customer c : customers) {
            Object[] row = {
                c.getId(),
                c.getName(),
                c.getPhone(),
                c.getMemberType(),
                c.getDiscountPercent()
            };
            tableModel.addRow(row);
        }
    }
    private void saveCustomer() {
        try {
            String name = txtName.getText().trim();
            String phone = txtPhone.getText().trim();
            String type = cbMemberType.getSelectedItem().toString();
            Double discount = Double.parseDouble(txtDiscount.getText().trim());
            if (name.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tên và số điện thoại!");
                return;
            }
            Customer customer = new Customer();
            if (!txtId.getText().isEmpty()) {
                customer.setId(Integer.parseInt(txtId.getText()));
            }
            customer.setName(name);
            customer.setPhone(phone);
            customer.setMemberType(type);
            customer.setDiscountPercent(discount);
            customerDAO.save(customer);
            JOptionPane.showMessageDialog(this, "Lưu thành công!");
            clearForm();
            loadData();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Chiết khấu phải là số!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }
    private void clearForm() {
        txtId.setText("");
        txtName.setText("");
        txtPhone.setText("");
        cbMemberType.setSelectedIndex(0);
        txtDiscount.setText("0");
        table.clearSelection();
    }
}
