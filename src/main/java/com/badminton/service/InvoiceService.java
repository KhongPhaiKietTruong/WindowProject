package com.badminton.service;

import com.badminton.dao.InvoiceDAO;
import com.badminton.entity.Invoice;
import com.badminton.entity.ServiceDetail;
import com.badminton.entity.Customer;
import com.badminton.entity.Booking;

import java.time.Duration;

public class InvoiceService {

    private InvoiceDAO invoiceDAO;

    public InvoiceService() {
        this.invoiceDAO = new InvoiceDAO();
    }

    // Tính toán lại tổng tiền của hóa đơn
    // Yêu cầu: Tiền sân = (Số giờ + phút lẻ quy ra giờ) * giá sân
    // Tổng dịch vụ: Tổng các detail (số lượng * đơn giá)
    // Tính chiết khấu = %giảm (từ Customer) * (Tiền sân + Tổng dịch vụ)
    // Tổng cần trả = Tiền sân + Dịch vụ - Giá trị chiết khấu
    public void calculateInvoiceTurnover(Invoice invoice) {
        Booking booking = invoice.getBooking();
        Customer customer = booking.getCustomer();

        // 1. Tính toán tiền thuê sân dựa trên thời lượng chơi (hẹn trước hoặc thực tế)
        Duration timePlayed = Duration.between(booking.getStartTime(), booking.getExpectedEndTime());
        // Chuyển đổi thành số giờ (VD: 90 phút = 1.5 giờ)
        double hoursPlayed = Math.max(0.5, timePlayed.toMinutes() / 60.0); // Tối thiểu 30 phút
        
        // Giá sân
        double courtPricePerHour = booking.getCourt().getPricePerHour();
        
        // Áp dụng hệ số ca nếu nhân viên có ca làm việc
        double multiplier = 1.0;
        if (invoice.getEmployee() != null && invoice.getEmployee().getShift() != null) {
            multiplier = invoice.getEmployee().getShift().getPriceMultiplier();
        }
        
        double courtFee = hoursPlayed * courtPricePerHour * multiplier;
        invoice.setCourtFee(courtFee);

        // 2. Tính toán tiền các dịch vụ đi kèm
        double serviceTotal = 0.0;
        if (invoice.getServiceDetails() != null) {
            for (ServiceDetail detail : invoice.getServiceDetails()) {
                // Đảm bảo update detail total price = số lượng * giá đơn
                detail.setTotalPrice(detail.getQuantity() * detail.getUnitPrice());
                serviceTotal += detail.getTotalPrice();
            }
        }
        invoice.setServiceFee(serviceTotal);

        // 3. Tính tiền khách hạng ưu đãi (Discount)
        double baseTotal = courtFee + serviceTotal;
        double discountPercent = (customer != null && customer.getDiscountPercent() != null) 
                                ? customer.getDiscountPercent() : 0.0;
        double discountValue = (baseTotal * discountPercent) / 100.0;
        invoice.setDiscountAmount(discountValue);

        // 4. Tổng thanh toán cuối cùng
        double finalAmount = baseTotal - discountValue;
        invoice.setTotalAmount(finalAmount);
        
        System.out.println("Hóa đơn tóm tắt: Tiền Sân=" + courtFee + " (x" + multiplier + "), Tiền Dịch Vụ=" + serviceTotal + ", Chiết Khấu=" + discountValue + " -> TỔNG TRẢ=" + finalAmount);
    }
    
    // Lưu hóa đơn sau khi đã tính toán
    public void createAndSaveInvoice(Invoice invoice) {
        calculateInvoiceTurnover(invoice);
        invoiceDAO.save(invoice);
        System.out.println("Lưu hóa đơn thành công!");
    }
}

