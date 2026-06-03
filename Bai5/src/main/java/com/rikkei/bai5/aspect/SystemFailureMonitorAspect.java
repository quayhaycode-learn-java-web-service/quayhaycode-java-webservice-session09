package com.rikkei.bai5.aspect;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Aspect
@Component
public class SystemFailureMonitorAspect {

    private static final Logger logger = LoggerFactory.getLogger(SystemFailureMonitorAspect.class);

    // Bắt toàn bộ ngoại lệ ném ra từ tất cả các phương thức bên trong OrderService
    @AfterThrowing(pointcut = "execution(* com.rikkei.bai5.service.OrderService.*(..))", throwing = "ex")
    public void logAfterThrowing(Throwable ex) {
        try {
            // 1. Tạo và gán requestId duy nhất vào MDC để phục vụ truy vết (Tracing)
            String requestId = UUID.randomUUID().toString();
            MDC.put("requestId", requestId);

            // 2. Ghi log lỗi tập trung đúng 1 lần duy nhất kèm theo thông tin Exception
            logger.error("Xảy ra lỗi hệ thống nghiêm trọng tại OrderService. Chi tiết: {}", ex.getMessage(), ex);

        } finally {
            // 3. Bắt buộc phải xóa MDC sau khi xử lý xong để tránh rò rỉ dữ liệu (ThreadLocal Leak)
            MDC.clear();
        }
    }
}