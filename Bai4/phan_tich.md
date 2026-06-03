# Phân Tích Cấu Hình Log Đa Môi Trường (Dev vs Prod)

## 1. Bản chất và Mục đích của việc tách biệt môi trường Log
Trong vòng đời phát triển phần mềm, môi trường Phát triển (Dev) và môi trường Vận hành (Prod) có những đặc thù, tài 
nguyên và mục tiêu hoàn toàn khác nhau. Việc áp dụng chung một cấu hình log cho cả hai sẽ dẫn đến hiệu năng kém trên 
Prod hoặc khó khăn khi debug ở Dev.

### Bảng so sánh tiêu chí cấu hình:

| Tiêu chí | Môi trường DEV | Môi trường PROD |
| :--- | :--- | :--- |
| **Mục đích chính** | Hỗ trợ lập trình viên theo dõi luồng chạy, phát hiện lỗi nhanh chóng khi viết code. | Theo dõi sức khỏe hệ thống, lưu vết (audit trail) và phục vụ việc điều tra sự cố (troubleshooting) từ xa. |
| **Hình thức xuất (Appender)** | **Console (Màn hình IDE)**<br>- Hiển thị trực quan, tức thời.<br>- Không cần lưu trữ lâu dài. | **Rolling File (Ghi ra file + Tự động cắt nén)**<br>- Tránh làm mất log khi container/ứng dụng bị restart.<br>- File được nén để tiết kiệm dung lượng ổ cứng. |
| **Mức độ chi tiết (Log Level)** | **DEBUG / TRACE**<br>- Ghi lại chi tiết từng câu lệnh SQL, dữ liệu truyền vào/ra, các bước xử lý nhỏ nhất. | **INFO / WARN / ERROR**<br>- Chỉ ghi lại các cột mốc quan trọng (App start, Request hoàn thành) hoặc lỗi.<br>- Giảm thiểu "nhiễu" thông tin. |
| **Quản lý tài nguyên (Disk Space)**| Không đáng kể vì log chủ yếu nằm trên RAM (Console) hoặc bị xóa khi tắt IDE. | **Bắt buộc kiểm soát** nghiêm ngặt thông qua cơ chế **Rotation** (giới hạn dung lượng file và số ngày lưu giữ) để tránh gây tràn ổ cứng (Disk Full) làm sập hệ thống. |

---

## 2. Giải pháp kỹ thuật áp dụng trong `logback-spring.xml`
- **`<springProfile>`**: Sử dụng tính năng tích hợp của Spring Boot để kích hoạt các block cấu hình log động dựa trên 
biến môi trường `spring.profiles.active` (`dev` hoặc `prod`).
- **`ConsoleAppender`**: Xuất luồng log ra tiêu chuẩn `System.out` trên Dev giúp tối ưu tốc độ đọc của lập trình viên.
- **`RollingFileAppender` kết hợp `SizeAndTimeBasedRollingPolicy`**:
    - Vừa cắt file theo ngày (giữ tối đa 30 ngày lịch sử) vừa cắt theo dung lượng (max 10MB/file).
    - Tự động đóng gói và nén log cũ thành file định dạng `.gz` để tiết kiệm dung lượng lưu trữ trên server.