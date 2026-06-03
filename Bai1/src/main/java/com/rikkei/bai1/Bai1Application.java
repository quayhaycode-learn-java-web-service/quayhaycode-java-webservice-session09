package com.rikkei.bai1;

import com.rikkei.bai1.service.FlashSaleService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Bai1Application {

    public static void main(String[] args) {
        FlashSaleService service = new FlashSaleService();

        service.applyDiscount("U001", "SALE50");
    }
}
