package com.rikkei.bai1.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Coupon {
    private String code;
    private int availableQuantity;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

}