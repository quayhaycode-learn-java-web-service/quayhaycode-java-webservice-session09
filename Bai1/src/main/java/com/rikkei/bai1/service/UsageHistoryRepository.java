package com.rikkei.bai1.service;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public class UsageHistoryRepository {

    private static final Set<String> historyDb = new ConcurrentSkipListSet<>();

    static {
        historyDb.add("U001_SALE50");
    }

    public boolean existsByUserIdAndCouponCode(String userId, String code) {
        String key = userId + "_" + code;
        return historyDb.contains(key);
    }

    public void saveHistory(String userId, String code) {
        String key = userId + "_" + code;
        historyDb.add(key);
    }
}