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

    private Integer currentCourtId = null;

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

        JLabel lblTitle = new JLabel("QUẢN LÝ SÂN CẦU LÔNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        this.add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin Sân"));

        formPanel.add(new JLabel("Tên Sân:"));
        txtName = new JTextField();
        formPanel.add(txtName);

        formPanel.add(new JLabel("Loại Sân:"));
        cbCourtType = new JComboBox<>(new String[]{"Sân Thảm", "Sân Gỗ", "Sân Bê Tông"});
        formPanel.add(cbCourtType);

        formPanel.add(new JLabel("Đơn Giá Thuê/Giờ:"));
        txtPrice = new JTextField("100000"); // default for Thảm
        txtPrice.setEditable(false);
        formPanel.add(txtPrice);

        // Auto update price based on type
        cbCourtType.addActionListener(e -> {
            String type = cbCourtType.getSelectedItem() != null ? cbCourtType.getSelectedItem().toString() : "";
            if ("Sân Thảm".equals(type)) {
                txtPrice.setText("100000");
            } else if ("Sân Gỗ".equals(type)) {
                txtPrice.setText("120000");
            } else {
                txtPrice.setText("80000");
            }
        });

        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Thêm/Cập nhật");
        JButton btnClear = new JButton("Làm mới");
        JButton btnDelete = new JButton("Xóa");
        btnDelete.setBackground(new Color(255, 100, 100));
        btnDelete.setForeground(Color.WHITE);

        btnAdd.addActionListener(e -> saveCourt());
        btnClear.addActionListener(e -> clearForm());
        btnDelete.addActionListener(e -> deleteCourt());

        btnPanel.add(btnAdd);
        btnPanel.add(btnClear);
        btnPanel.add(btnDelete);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(btnPanel, BorderLayout.SOUTH);

        String[] columnNames = {"ID", "Tên Sân", "Loại Sân", "Giá/Giờ"};
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
                currentCourtId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                txtName.setText(tableModel.getValueAt(row, 1).toString());
                cbCourtType.setSelectedItem(tableModel.getValueAt(row, 2).toString());
                txtPrice.setText(tableModel.getValueAt(row, 3).toString());
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
            if (currentCourtId != null) {
                court.setId(currentCourtId);
            }
            court.setName(name);
            court.setCourtType(type);
            court.setPricePerHour(price);

            courtDAO.save(court);
            JOptionPane.showMessageDialog(this, "Lưu thành công!");
            clearForm();
            loadData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void deleteCourt() {
        if (currentCourtId == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sân để xóa!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa sân này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                courtDAO.delete(currentCourtId);
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                clearForm();
                loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        }
    }

    private void clearForm() {
        currentCourtId = null;
        txtName.setText("");
        cbCourtType.setSelectedIndex(0);
        txtPrice.setText("100000");
        table.clearSelection();
    }
}
