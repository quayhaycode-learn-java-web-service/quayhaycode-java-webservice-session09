# Phân Tích: Parameterized Logging vs String Concatenation (+)

### 1. Vấn đề của phép cộng chuỗi (`+`)
Khi dùng toán tử `+` (ví dụ: `"SP: " + productId`), Java bắt buộc phải khởi tạo một `StringBuilder` (hoặc tối ưu trực 
tiếp tùy phiên bản Java) để nối các chuỗi lại với nhau **trước khi** truyền tham số vào hàm `logger.info()`.

* **Lãng phí tài nguyên:** Giả sử dòng log đó ở mức `debug` (`logger.debug(...)`) nhưng cấu hình hệ thống hiện tại chỉ 
ghi log từ mức `info`. Lúc này, log không được in ra, nhưng CPU và bộ nhớ (Heap) **vẫn phải tốn công xử lý phép cộng 
chuỗi** và tạo ra các đối tượng chuỗi rác (Garbage Collection phải dọn dẹp sau đó).
* **Code rườm rà:** Khó đọc, dễ viết nhầm dấu nháy và dấu cộng.

### 2. Ưu điểm của Parameterized Logging (`{}`)
Khi sử dụng chuẩn `{}` của SLF4J (ví dụ: `log.info("SP: {}, SL: {}", productId, qty)`):

* **Tối ưu hiệu năng (Lazy Evaluation):** Hệ thống sẽ kiểm tra xem log level (`info`, `debug`,...) có đang được bật hay 
không **trước**. Nếu log level đó đang bị tắt, SLF4J sẽ bỏ qua luôn và hoàn toàn không tốn một chút tài nguyên nào để 
nối chuỗi.
* **Tránh tạo đối tượng rác:** Chuỗi chỉ được định dạng (format) khi và chỉ khi log đó thực sự được ghi ra file 
hoặc console.
* **Clean Code:** Code ngắn gọn, dễ đọc, dễ bảo trì và hạn chế tối đa sai sót.