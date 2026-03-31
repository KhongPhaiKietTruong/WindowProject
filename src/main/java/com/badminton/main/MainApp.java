package com.badminton.main;

import com.badminton.view.MainForm;
import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class MainApp {
    public static void main(String[] args) {
        // Thiết lập Look and Feel
        try {
            // Sử dụng Nimbus Look and Feel cho giao diện đẹp và hiện đại hơn
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Chạy chương trình chính
        SwingUtilities.invokeLater(() -> {
            MainForm mainForm = new MainForm();
            mainForm.setVisible(true);
        });
    }
}
