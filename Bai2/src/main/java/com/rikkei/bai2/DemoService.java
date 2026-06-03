package com.rikkei.bai2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemoService {

    private static final Logger logger = LoggerFactory.getLogger(DemoService.class);


    public void checkPromotionCode(String code, String userId) {

        if (code.equals("VIP")) {
            logger.info("Áp dụng thành công cho user: {}", userId);

        } else if (code.equals("EXPIRED")) {
            logger.warn("Mã giảm giá đã hết hạn: {}", code);

        } else {
            logger.error("Lỗi mất kết nối DB khi check mã: {}", code);
        }

    }
}