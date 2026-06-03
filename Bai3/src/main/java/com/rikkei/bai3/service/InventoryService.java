package com.rikkei.bai3.service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InventoryService {

    public void updateStock(String productId, int qty) {
        // Sử dụng chuẩn Parameterized {}, tuyệt đối không dùng toán tử +
        log.info("Bắt đầu cập nhật kho cho SP: {}, SL: {}", productId, qty);
    }
}