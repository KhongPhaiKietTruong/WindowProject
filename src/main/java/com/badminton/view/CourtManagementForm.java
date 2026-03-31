package com.badminton.view;

import com.badminton.dao.CourtDAO;
import com.badminton.entity.Court;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CourtManagementForm extends JPanel {

    private CourtDAO courtDAO;
    private JTable table;
    private DefaultTableModel tableModel;

    // Các trường nhập liệu
    private JTextField txtId;
    private JTextField txtName;
    private JComboBox<String> cbCourtType;
    private JTextField txtPrice;

    public CourtManagementForm() {
        this.courtDAO = new CourtDAO();
        initComponents();
        loadData();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tiêu đề
        JLabel lblTitle = new JLabel("QUẢN LÝ SÂN CẦU LÔNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        this.add(lblTitle, BorderLayout.NORTH);

        // Vùng nhập liệu (Form)
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin Sân"));

        formPanel.add(new JLabel("Mã Sân (Tự động):"));
        txtId = new JTextField();
        txtId.setEditable(false);
        formPanel.add(txtId);

        formPanel.add(new JLabel("Tên Sân:"));
        txtName = new JTextField();
        formPanel.add(txtName);

        formPanel.add(new JLabel("Loại Sân:"));
        cbCourtType = new JComboBox<>(new String[]{"Sân Thảm", "Sân Gỗ", "Sân Bê Tông"});
        formPanel.add(cbCourtType);

        formPanel.add(new JLabel("Đơn Giá Thuê/Giờ:"));
        txtPrice = new JTextField();
        formPanel.add(txtPrice);

        // Vùng nút chức năng
        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Thêm/Cập nhật");
        JButton btnClear = new JButton("Làm mới");

        btnAdd.addActionListener(e -> saveCourt());
        btnClear.addActionListener(e -> clearForm());

        btnPanel.add(btnAdd);
        btnPanel.add(btnClear);

        // Gộp Form và Nút
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(btnPanel, BorderLayout.SOUTH);

        // Setup bảng (Table)
        String[] columnNames = {"ID", "Tên Sân", "Loại Sân", "Giá/Giờ"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        
        // Sự kiện click vào dòng
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                txtId.setText(tableModel.getValueAt(row, 0).toString());
                txtName.setText(tableModel.getValueAt(row, 1).toString());
                cbCourtType.setSelectedItem(tableModel.getValueAt(row, 2).toString());
                txtPrice.setText(tableModel.getValueAt(row, 3).toString());
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        
        // Container ở giữa để chứa cả Form và Bảng
        JPanel centerContainer = new JPanel(new BorderLayout(5, 5));
        centerContainer.add(topPanel, BorderLayout.NORTH);
        centerContainer.add(scrollPane, BorderLayout.CENTER);
        
        this.add(centerContainer, BorderLayout.CENTER);
    }

    private void loadData() {
        tableModel.setRowCount(0); // Xóa dữ liệu cũ
        List<Court> courts = courtDAO.findAll();
        for (Court c : courts) {
            Object[] row = {
                c.getId(),
                c.getName(),
                c.getCourtType(),
                c.getPricePerHour()
            };
            tableModel.addRow(row);
        }
    }

    private void saveCourt() {
        try {
            String name = txtName.getText().trim();
            String type = cbCourtType.getSelectedItem().toString();
            Double price = Double.parseDouble(txtPrice.getText().trim());

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tên sân!");
                return;
            }

            Court court = new Court();
            if (!txtId.getText().isEmpty()) {
                court.setId(Integer.parseInt(txtId.getText()));
            }
            court.setName(name);
            court.setCourtType(type);
            court.setPricePerHour(price);

            courtDAO.save(court);
            JOptionPane.showMessageDialog(this, "Lưu thành công!");
            clearForm();
            loadData();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Đơn giá phải là một số hợp lệ!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void clearForm() {
        txtId.setText("");
        txtName.setText("");
        cbCourtType.setSelectedIndex(0);
        txtPrice.setText("");
        table.clearSelection();
    }
}
