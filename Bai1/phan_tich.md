# Phân tích: Tại sao tuyệt đối không dùng System.out.println() trên Production?

Dưới đây là 3 lý do cốt lõi khiến việc dùng `System.out.println()` và `e.printStackTrace()` trở thành "tối kỵ" khi triển
khai ứng dụng thực tế:

* **Gây nghẽn hiệu năng hệ thống (Blocking I/O & Synchronization):** Hàm `System.out.println()` bên dưới sử dụng cơ chế
`synchronized` và ghi trực tiếp vào luồng xuất tiêu chuẩn (Standard Output). Trong các kịch bản lượng truy cập cao như
**Flash Sale**, hàng ngàn thread xử lý sẽ phải xếp hàng chờ nhau để được in log. Điều này làm giảm nghiêm trọng 
throughput (băng thông xử lý) của hệ thống và gây tăng latency (độ trễ).
* **Mất dấu vết log và không thể quản lý:** `System.out.println()` chỉ đẩy dữ liệu ra console. Khi server hoặc container
(Docker) bị khởi động lại, toàn bộ dữ liệu này sẽ biến mất vĩnh viễn. Ngoài ra, bạn không thể cấu hình để log tự động 
ghi vào file, tự động xoay vòng (rotation) theo ngày, hoặc phân tách mức độ nghiêm trọng (INFO, WARN, ERROR) để lọc khi 
cần thiết.
* **Làm rối loạn dữ liệu log trong môi trường đa luồng (Multi-threading):** Khi dùng `e.printStackTrace()`, dữ liệu lỗi
được đẩy vào luồng `System.err`. Trong môi trường high-concurrency, các dòng stack trace của các request khác nhau sẽ bị
đan xen vào nhau vô tội vạ, biến log thành một mớ hỗn độn không thể đọc nổi. Bạn sẽ không thể biết dòng lỗi này là của 
User nào hay của mã giảm giá nào.