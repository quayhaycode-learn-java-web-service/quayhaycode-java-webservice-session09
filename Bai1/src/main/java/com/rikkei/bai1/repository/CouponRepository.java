package com.rikkei.bai1.repository;

import com.rikkei.bai1.entity.Coupon;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CouponRepository {

    private static final Map<String, Coupon> couponDb = new ConcurrentHashMap<>();

    static {
        couponDb.put("SALE50", new Coupon("SALE50", 5,
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now().plusHours(2)));

        couponDb.put("HETLUOT", new Coupon("HETLUOT", 0,
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now().plusHours(2)));

        couponDb.put("QUAHAN", new Coupon("QUAHAN", 10,
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1)));
    }

    public Coupon findByCode(String code) {
        return couponDb.get(code);
    }

    public void decreaseQuantity(String code) {
        Coupon coupon = couponDb.get(code);
        if (coupon != null) {
            synchronized (coupon) {
                int currentQty = coupon.getAvailableQuantity();
                if (currentQty > 0) {
                    coupon.setAvailableQuantity(currentQty - 1);
                }
            }
        }
    }
}