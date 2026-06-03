package com.rikkei.bai1.service;

import com.rikkei.bai1.entity.Coupon;
import com.rikkei.bai1.exception.BusinessException;
import com.rikkei.bai1.repository.CouponRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;

public class FlashSaleService {

    private static final Logger logger = LoggerFactory.getLogger(FlashSaleService.class);

    // Giả định đây là các lớp đảm nhận việc gọi Database độc lập
    private final CouponRepository couponRepository = new CouponRepository();
    private final UsageHistoryRepository usageRepository = new UsageHistoryRepository();

    public void applyDiscount(String userId, String code) {
        logger.info("[FlashSale] Bắt đầu xử lý mã: {} cho user: {}", code, userId);

        try {
            // 1. Kiểm tra xem mã giảm giá có tồn tại trong hệ thống không
            Coupon coupon = couponRepository.findByCode(code);
            if (coupon == null) {
                throw new BusinessException("Mã giảm giá không tồn tại hoặc đã bị xóa!");
            }

            // 2. Kiểm tra thời gian hiệu lực của Flash Sale
            LocalDateTime giờHiệnTại = LocalDateTime.now();
            if (giờHiệnTại.isBefore(coupon.getStartTime()) || giờHiệnTại.isAfter(coupon.getEndTime())) {
                throw new BusinessException("Mã giảm giá chưa đến giờ áp dụng hoặc đã hết hạn!");
            }

            // 3. Kiểm tra số lượng mã tồn kho (Số lượng giới hạn của Flash Sale)
            if (coupon.getAvailableQuantity() <= 0) {
                throw new BusinessException("Mã giảm giá đã hết lượt sử dụng (Sold out)!");
            }

            boolean đãDùngChưa = usageRepository.existsByUserIdAndCouponCode(userId, code);
            if (đãDùngChưa) {
                throw new BusinessException("Mỗi tài khoản chỉ được phép sử dụng mã này 1 lần!");
            }

            couponRepository.decreaseQuantity(code);
            usageRepository.saveHistory(userId, code);

            logger.info("[FlashSale] Áp dụng thành công mã {} cho user {}. Lượt còn lại: {}",
                    code, userId, coupon.getAvailableQuantity() - 1);

        } catch (BusinessException e) {
            logger.warn("[FlashSale] Từ chối áp dụng mã {} cho user {}. Lý do: {}", code, userId, e.getMessage());

        } catch (Exception e) {
            logger.error("[FlashSale] LỖI HỆ THỐNG NGHIÊM TRỌNG khi xử lý mã: {} cho user: {}.", code, userId, e);
        }
    }
}