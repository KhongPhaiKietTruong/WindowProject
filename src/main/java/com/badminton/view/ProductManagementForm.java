package com.badminton.view;

import com.badminton.dao.ProductDAO;
import com.badminton.entity.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProductManagementForm extends JPanel {

    private ProductDAO productDAO;
    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField txtId;
    private JTextField txtName;
    private JComboBox<String> cbCategory;
    private JTextField txtPrice;
    private JTextField txtStock;

    public ProductManagementForm() {
        this.productDAO = new ProductDAO();
        initComponents();
        loadData();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTitle = new JLabel("QUẢN LÝ DỊCH VỤ / SẢN PHẨM", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        this.add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin Sản phẩm"));

        formPanel.add(new JLabel("Mã (Tự động):"));
        txtId = new JTextField();
        txtId.setEditable(false);
        formPanel.add(txtId);

        formPanel.add(new JLabel("Tên Sản phẩm:"));
        txtName = new JTextField();
        formPanel.add(txtName);

        formPanel.add(new JLabel("Danh mục:"));
        cbCategory = new JComboBox<>(new String[]{"Nước uống", "Thuê vợt & cầu", "Khác"});
        formPanel.add(cbCategory);

        formPanel.add(new JLabel("Giá bán:"));
        txtPrice = new JTextField("0");
        formPanel.add(txtPrice);

        formPanel.add(new JLabel("Tồn kho:"));
        txtStock = new JTextField("0");
        formPanel.add(txtStock);

        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Thêm/Cập nhật");
        JButton btnClear = new JButton("Làm mới");

        btnAdd.addActionListener(e -> saveProduct());
        btnClear.addActionListener(e -> clearForm());

        btnPanel.add(btnAdd);
        btnPanel.add(btnClear);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(btnPanel, BorderLayout.SOUTH);

        String[] columnNames = {"ID", "Tên", "Danh mục", "Đơn giá", "Tồn kho"};
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
                cbCategory.setSelectedItem(tableModel.getValueAt(row, 2).toString());
                txtPrice.setText(tableModel.getValueAt(row, 3).toString());
                txtStock.setText(tableModel.getValueAt(row, 4).toString());
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
        List<Product> products = productDAO.findAll();
        for (Product p : products) {
            Object[] row = {
                p.getId(),
                p.getName(),
                p.getCategory(),
                p.getPrice(),
                p.getStock()
            };
            tableModel.addRow(row);
        }
    }

    private void saveProduct() {
        try {
            String name = txtName.getText().trim();
            String category = cbCategory.getSelectedItem() != null ? cbCategory.getSelectedItem().toString() : "";
            Double price = Double.parseDouble(txtPrice.getText().trim());
            Integer stock = Integer.parseInt(txtStock.getText().trim());

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tên sản phẩm!");
                return;
            }

            Product product = new Product();
            if (!txtId.getText().isEmpty()) {
                product.setId(Integer.parseInt(txtId.getText()));
            }
            product.setName(name);
            product.setCategory(category);
            product.setPrice(price);
            product.setStock(stock);

            productDAO.save(product);
            JOptionPane.showMessageDialog(this, "Lưu thành công!");
            clearForm();
            loadData();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Giá và tồn kho phải là số hợp lệ!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void clearForm() {
        txtId.setText("");
        txtName.setText("");
        cbCategory.setSelectedIndex(0);
        txtPrice.setText("0");
        txtStock.setText("0");
        table.clearSelection();
    }
}
