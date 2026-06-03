# Phân tích Phân loại Log Levels - Bài 2

## 1. Thực trạng vấn đề
Hiện tại, hệ thống đang sử dụng `logger.info()` cho tất cả các tình huống. Điều này dẫn đến việc file log bị quá tải 
thông tin ("ngập rác"), khiến người quản trị hệ thống (Sếp/DevOps/SRE) không thể phân biệt được đâu là lỗi nghiêm trọng 
cần xử lý gấp và đâu là các hoạt động bình thường hoặc lỗi do phía người dùng.

## 2. Xác định cấp độ Log đúng theo quy tắc nghiệp vụ

Dựa trên quy tắc nghiệp vụ được đưa ra, các trường hợp được phân loại lại như sau:

| Trường hợp | Mô tả tình huống | Cấp độ Log đúng | Lý do lựa chọn                                                                                                                                                                                             |
| :--- | :--- | :--- |:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Trường hợp 1** | Áp dụng mã "VIP" thành công cho user. | **`INFO`** | Đây là luồng chạy bình thường, thành công <br/>của hệ thống. Log này dùng để ghi nhận trạng thái hoạt động chính xác (để tracking/audit khi cần).                                                               |
| **Trường hợp 2** | Người dùng nhập mã đã hết hạn (`EXPIRED`). | **`WARN`** | Đây là **Lỗi nghiệp vụ** <br/>(Business Exception) do phía người dùng thao tác sai, hệ thống vẫn hoạt động đúng logic. Sử dụng `WARN` để cảnh báo nhưng không cần kích hoạt hệ thống báo động lỗi nghiêm trọng. |
| **Trường hợp 3** | Lỗi mất kết nối Database khi kiểm tra mã. | **`ERROR`** | Đây là **Lỗi hệ thống** <br/>(System Exception). Lỗi này làm chết logic, ảnh hưởng trực tiếp đến trải nghiệm người dùng và vận hành của ứng dụng, cần đội ngũ kỹ thuật can thiệp và sửa chữa ngay lập tức.      |