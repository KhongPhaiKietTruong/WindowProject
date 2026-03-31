package com.badminton.service;

import com.badminton.dao.CourtDAO;
import com.badminton.dao.BookingDAO;
import com.badminton.entity.Booking;
import com.badminton.entity.Court;

import java.time.LocalDateTime;
import java.util.List;

public class BookingService {
    
    private CourtDAO courtDAO;
    private BookingDAO bookingDAO;

    public BookingService() {
        this.courtDAO = new CourtDAO();
        this.bookingDAO = new BookingDAO();
    }

    // Kiểm tra xem sân có bị trùng lịch trong khoảng thời gian muốn đặt hay không
    public boolean isCourtAvailable(Integer courtId, LocalDateTime startTime, LocalDateTime endTime) {
        // Lấy danh sách các sân trống trong khoảng thời gian này
        List<Court> availableCourts = courtDAO.findAvailableCourts(startTime, endTime);
        
        // Kiểm tra xem id sân cần đặt có nằm trong những sân trống này không
        for (Court court : availableCourts) {
            if (court.getId().equals(courtId)) {
                return true; // Sân trống rỗng, có thể đặt
            }
        }
        return false; // Đã có người đặt (trùng khớp)
    }

    // Đặt sân với sự kiểm tra trùng lịch
    public void bookCourt(Booking booking) throws Exception {
        if (!isCourtAvailable(booking.getCourt().getId(), booking.getStartTime(), booking.getExpectedEndTime())) {
            throw new Exception("Sân này đã bị đặt trong khoảng thời gian bạn chọn!");
        }
        bookingDAO.save(booking);
        System.out.println("Đặt sân thành công cho sân: " + booking.getCourt().getName());
    }
}
