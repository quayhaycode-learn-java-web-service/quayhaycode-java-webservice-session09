# Phân Tích: JSON Log vs Text Log Trong Hệ Thống Giám Sát (ELK / Datadog)

Khi hệ thống chuyển đổi từ kiến trúc Monolith sang Microservices, cách tiếp cận ghi log truyền thống (Text log) bộc lộ 
nhiều hạn chế. Dưới đây là 2 lý do cốt lõi giải thích tại sao **JSON Log** hoàn toàn vượt trội hơn **Text Log** khi đẩy 
lên các hệ thống giám sát tập trung:

### 1. Khả năng Phân tích cú pháp (Parsing) và Đánh chỉ mục (Indexing) tự động
* **Với Text Log:** Các hệ thống như Logstash (trong ELK) phải sử dụng các biểu thức chính quy phức tạp (Grok Patterns) 
để bóc tách từng trường dữ liệu (Timestamp, Log Level, Message, Thread...). Nếu lập trình viên vô tình thay đổi định 
dạng in log (ví dụ: đổi dấu cách thành dấu gạch ngang), bộ cấu hình Grok sẽ bị lỗi, dẫn đến việc không thể phân tích dữ 
liệu.
* **Với JSON Log:** Định dạng JSON là cấu trúc dữ liệu chuẩn được các hệ thống như Elasticsearch hay Datadog **hỗ trợ 
mặc định**. Hệ thống giám sát có thể tự động parse các cặp `key-value` 
(ví dụ: `"level": "ERROR"`, `"requestId": "12345"`) và đánh chỉ mục (inde


### 2. Tối ưu hóa Truy vấn (Querying), Bộ lọc (Filtering) và Cảnh báo (Alerting)
* **Với Text Log:** Để tìm các lỗi nghiêm trọng, bạn phải thực hiện tìm kiếm toàn văn (Full-text search) cụm từ "ERROR".
Việc này cực kỳ tốn tài nguyên và dễ sót dữ liệu nếu log text không đồng nhất.
* **Với JSON Log:** Bạn có thể thực hiện các câu lệnh truy vấn chính xác tuyệt đối và nhanh chóng dựa trên các trường dữ
liệu cụ thể (ví dụ: `level == "ERROR" AND service == "order-service"`). Điều này giúp lập trình viên dễ dàng thiết lập 
các biểu đồ thời gian thực (Dashboard) hoặc cấu hình hệ thống tự động gửi cảnh báo (Alert) qua Slack/Telegram khi một
mã lỗi (`errorCode`) cụ thể vượt ngưỡng cho phép.