package com.badminton.controller;

import com.badminton.entity.Booking;
import com.badminton.entity.Court;
import com.badminton.entity.Customer;
import com.badminton.service.BookingService;

import javax.swing.*;
import java.time.LocalDateTime;

public class BookingController {
    
    private BookingService bookingService;
    
    public BookingController() {
        this.bookingService = new BookingService();
    }
    
    /**
     * Xử lý sự kiện khi người dùng nhấn nút "Đặt sân"
     * @param parentComponent Component dùng để hiển thị hộp thoại thông báo
     * @param customer Khách hàng đặt sân
     * @param court Sân được chọn
     * @param startTime Thời gian bắt đầu
     * @param expectedEndTime Thời gian kết thúc dự kiến
     */
    public void handleBookingAction(java.awt.Component parentComponent, Customer customer, Court court,
                                    LocalDateTime startTime, LocalDateTime expectedEndTime) {
        
        // Tạo đối tượng Booking
        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setCourt(court);
        booking.setStartTime(startTime);
        booking.setExpectedEndTime(expectedEndTime);
        booking.setStatus("Pending");
        
        try {
            // Gọi Service để kiểm tra và đặt sân
            bookingService.bookCourt(booking);
            
            // Nếu không có exception, hiển thị thông báo thành công
            JOptionPane.showMessageDialog(parentComponent, 
                "Giữ chỗ thành công cho sân: " + court.getName() + " lúc " + startTime.toString(),
                "Thành công", 
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception ex) {
            // Bắt lỗi trùng sân hoặc lỗi hệ thống từ Service và báo lên UI
            JOptionPane.showMessageDialog(parentComponent, 
                ex.getMessage(), 
                "Lỗi đặt sân", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}

