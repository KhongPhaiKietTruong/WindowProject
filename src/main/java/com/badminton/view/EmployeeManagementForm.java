package com.badminton.view;

import com.badminton.dao.EmployeeDAO;
import com.badminton.entity.Employee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EmployeeManagementForm extends JPanel {

    private EmployeeDAO employeeDAO;
    private JTable table;
    private DefaultTableModel tableModel;

    private Integer currentEmployeeId = null;

    private JTextField txtName;
    private JTextField txtPhone;
    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public EmployeeManagementForm() {
        this.employeeDAO = new EmployeeDAO();
        initComponents();
        loadData();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTitle = new JLabel("QUẢN LÝ NHÂN VIÊN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        this.add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin Nhân viên"));

        formPanel.add(new JLabel("Tên Nhân viên:"));
        txtName = new JTextField();
        formPanel.add(txtName);

        formPanel.add(new JLabel("Số điện thoại:"));
        txtPhone = new JTextField();
        formPanel.add(txtPhone);

        formPanel.add(new JLabel("Tài khoản:"));
        txtUsername = new JTextField();
        formPanel.add(txtUsername);

        formPanel.add(new JLabel("Mật khẩu:"));
        txtPassword = new JPasswordField();
        formPanel.add(txtPassword);

        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Thêm/Cập nhật");
        JButton btnClear = new JButton("Làm mới");
        JButton btnDelete = new JButton("Xóa");
        btnDelete.setBackground(new Color(255, 100, 100));
        btnDelete.setForeground(Color.WHITE);

        btnAdd.addActionListener(e -> saveEmployee());
        btnClear.addActionListener(e -> clearForm());
        btnDelete.addActionListener(e -> deleteEmployee());

        btnPanel.add(btnAdd);
        btnPanel.add(btnClear);
        btnPanel.add(btnDelete);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(btnPanel, BorderLayout.SOUTH);

        String[] columnNames = {"ID", "Tên NV", "SĐT", "Tài khoản"};
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
                currentEmployeeId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                txtName.setText(tableModel.getValueAt(row, 1).toString());
                txtPhone.setText(tableModel.getValueAt(row, 2).toString());
                txtUsername.setText(tableModel.getValueAt(row, 3).toString());
                txtPassword.setText(""); // Don't show password
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
        List<Employee> employees = employeeDAO.findAll();
        for (Employee emp : employees) {
            Object[] row = {
                emp.getId(),
                emp.getName(),
                emp.getPhone(),
                emp.getUsername()
            };
            tableModel.addRow(row);
        }
    }

    private void saveEmployee() {
        try {
            String name = txtName.getText().trim();
            String phone = txtPhone.getText().trim();
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();

            if (name.isEmpty() || username.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập Tên và Tài khoản!");
                return;
            }

            if (currentEmployeeId == null && password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập Mật khẩu cho nhân viên mới!");
                return;
            }

            Employee employee = new Employee();
            if (currentEmployeeId != null) {
                employee.setId(currentEmployeeId);
            }
            employee.setName(name);
            employee.setPhone(phone);
            employee.setUsername(username);
            // If updating and password left empty, ideally keep old password. 
            // In a real app we'd fetch the old entity first. For simplicity we just require it or overwrite.
            if (!password.isEmpty()) {
                employee.setPassword(password);
            } else {
                // Fetch existing to get pass (simple hack since we don't want to show it)
                for(Employee e : employeeDAO.findAll()) {
                    if(e.getId().equals(currentEmployeeId)) {
                        employee.setPassword(e.getPassword());
                        break;
                    }
                }
            }

            employeeDAO.save(employee);
            JOptionPane.showMessageDialog(this, "Lưu thành công!");
            clearForm();
            loadData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void deleteEmployee() {
        if (currentEmployeeId == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên để xóa!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắn xóa nhân viên này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                employeeDAO.delete(currentEmployeeId);
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                clearForm();
                loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        }
    }

    private void clearForm() {
        currentEmployeeId = null;
        txtName.setText("");
        txtPhone.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
        table.clearSelection();
    }
}

