
-- 1. THÊM ROLES
INSERT INTO roles (name, description) VALUES 
('ADMIN', 'Quản trị hệ thống - quyền cao nhất'),
('MANAGER', 'Quản lý sản phẩm, giao nhận, báo cáo'),
('STAFF', 'Nhân viên hỗ trợ kỹ thuật'),
('CUSTOMER', 'Khách hàng mua sản phẩm');

-- 2. THÊM USERS (password = "123456" không mã hóa)
INSERT INTO users (username, email, password, full_name, phone, address) VALUES 
('admin', 'admin@stemkit.com', '$2a$12$TZTsft1k1Xm6pzUu/4aywe6glQfw3EBzkx4liDKhSwcjg6wO/xZYi', 'System Administrator', '0901234567', 'Hà Nội'),
('manager1', 'manager@stemkit.com', '$2a$12$TZTsft1k1Xm6pzUu/4aywe6glQfw3EBzkx4liDKhSwcjg6wO/xZYi', 'Nguyễn Văn Manager', '0901234568', 'TP.HCM'),
('staff1', 'staff1@stemkit.com', '$2a$12$TZTsft1k1Xm6pzUu/4aywe6glQfw3EBzkx4liDKhSwcjg6wO/xZYi', 'Trần Thị Staff', '0901234569', 'Đà Nẵng'),
('staff2', 'staff2@stemkit.com', '$2a$12$TZTsft1k1Xm6pzUu/4aywe6glQfw3EBzkx4liDKhSwcjg6wO/xZYi', 'Lê Văn Support', '0901234570', 'Cần Thơ'),
('customer1', 'customer1@gmail.com', '$2a$12$TZTsft1k1Xm6pzUu/4aywe6glQfw3EBzkx4liDKhSwcjg6wO/xZYi', 'Phạm Thị Khách', '0901234571', '123 Nguyễn Huệ, Q1, TP.HCM'),
('customer2', 'customer2@gmail.com', '$2a$12$TZTsft1k1Xm6pzUu/4aywe6glQfw3EBzkx4liDKhSwcjg6wO/xZYi', 'Hoàng Văn Mua', '0901234572', '456 Lê Lợi, Hà Nội');

-- 3. GÁN ROLES CHO USERS
INSERT INTO user_roles (user_id, role_id) VALUES 
(1, 1), -- admin có role ADMIN
(2, 2), -- manager1 có role MANAGER  
(3, 3), -- staff1 có role STAFF
(4, 3), -- staff2 có role STAFF
(5, 4), -- customer1 có role CUSTOMER
(6, 4); -- customer2 có role CUSTOMER

-- 4. THÊM CATEGORIES
INSERT INTO categories (name, description) VALUES 
('Robotics', 'Kit chế tạo và lập trình robot'),
('Electronics', 'Kit học tập điện tử, mạch điện'),
('Programming', 'Kit học lập trình, coding'),
('Science', 'Kit thí nghiệm khoa học'),
('Engineering', 'Kit kỹ thuật, xây dựng');

-- 5. THÊM KITS
INSERT INTO kits (name, description, price, stock_quantity, category_id, difficulty_level, age_range, created_by) VALUES 
('Robot Car Basic', 'Kit lắp ráp xe robot điều khiển cơ bản với Arduino', 299000, 50, 1, 'BEGINNER', '10-14 tuổi', 2),
('LED Circuit Kit', 'Kit học mạch điện LED với breadboard', 199000, 100, 2, 'BEGINNER', '8-12 tuổi', 2),
('Smart Home IoT', 'Kit xây dựng mô hình nhà thông minh', 599000, 30, 3, 'ADVANCED', '14-18 tuổi', 2),
('Chemistry Lab', 'Kit thí nghiệm hóa học an toàn cho trẻ em', 399000, 40, 4, 'INTERMEDIATE', '12-16 tuổi', 2),
('Bridge Building', 'Kit xây dựng cầu treo mô hình', 249000, 60, 5, 'INTERMEDIATE', '10-15 tuổi', 2);

-- 6. THÊM LABS
INSERT INTO labs (title, description, content, duration_minutes, difficulty_level, max_support_count, created_by) VALUES 
('Lắp ráp khung xe robot', 'Hướng dẫn lắp ráp khung cơ bản cho robot car', 'Bước 1: Chuẩn bị linh kiện...\nBước 2: Lắp ráp khung xe...', 60, 'BEGINNER', 3, 2),
('Lập trình điều khiển robot', 'Lập trình Arduino để điều khiển robot di chuyển', 'Code Arduino:\nvoid setup() {\n  // Khởi tạo\n}\nvoid loop() {\n  // Điều khiển\n}', 90, 'BEGINNER', 3, 2),
('Kết nối Bluetooth', 'Kết nối robot với smartphone qua Bluetooth', 'Hướng dẫn kết nối module Bluetooth HC-05...', 45, 'INTERMEDIATE', 2, 2),

('Mạch điện LED đơn giản', 'Tạo mạch điện LED cơ bản', 'Kết nối LED với pin và điện trở...', 30, 'BEGINNER', 3, 2),
('Mạch LED nhấp nháy', 'Lập trình LED nhấp nháy với timer', 'Sử dụng IC 555 để tạo dao động...', 45, 'BEGINNER', 3, 2),

('Cảm biến nhiệt độ', 'Đọc và hiển thị nhiệt độ', 'Kết nối cảm biến DHT22 với Arduino...', 60, 'ADVANCED', 2, 2),
('Điều khiển đèn qua app', 'Tạo app điều khiển đèn từ xa', 'Lập trình ESP32 và tạo web interface...', 120, 'ADVANCED', 2, 2),

('Thí nghiệm acid-base', 'Kiểm tra tính acid/base của dung dịch', 'Sử dụng giấy quỳ tím để kiểm tra...', 40, 'INTERMEDIATE', 3, 2),
('Tạo tinh thể muối', 'Nuôi tinh thể muối trong nước bão hòa', 'Pha dung dịch muối bão hòa...', 30, 'INTERMEDIATE', 3, 2),

('Thiết kế cầu treo', 'Tính toán và thiết kế cầu treo', 'Nguyên lý cấu trúc cầu treo...', 75, 'INTERMEDIATE', 2, 2),
('Test sức chịu tải', 'Kiểm tra sức chịu tải của cầu', 'Đặt tải trọng và đo độ võng...', 45, 'INTERMEDIATE', 2, 2);

-- 7. LIÊN KẾT KIT VỚI LAB
INSERT INTO kit_labs (kit_id, lab_id, lab_order) VALUES 
-- Robot Car Basic có 3 lab
(1, 1, 1), (1, 2, 2), (1, 3, 3),
-- LED Circuit Kit có 2 lab  
(2, 4, 1), (2, 5, 2),
-- Smart Home IoT có 2 lab
(3, 6, 1), (3, 7, 2),
-- Chemistry Lab có 2 lab
(4, 8, 1), (4, 9, 2),
-- Bridge Building có 2 lab
(5, 10, 1), (5, 11, 2);

-- 8. THÊM ĐỚN HÀNG MẪU
INSERT INTO orders (order_code, customer_id, total_amount, shipping_address, shipping_phone, payment_method, order_status, confirmed_by, confirmed_at) VALUES 
('ORD20240701001', 5, 498000, '123 Nguyễn Huệ, Q1, TP.HCM', '0901234571', 'COD', 'DELIVERED', 2, '2024-07-01 10:30:00'),
('ORD20240702002', 6, 299000, '456 Lê Lợi, Hà Nội', '0901234572', 'BANK_TRANSFER', 'SHIPPING', 2, '2024-07-02 14:15:00'),
('ORD20240703003', 5, 599000, '123 Nguyễn Huệ, Q1, TP.HCM', '0901234571', 'COD', 'PENDING', NULL, NULL);

-- 9. CHI TIẾT ĐƠN HÀNG
INSERT INTO order_items (order_id, kit_id, quantity, unit_price, total_price) VALUES 
-- Đơn hàng 1: Robot Car + LED Kit
(1, 1, 1, 299000, 299000),
(1, 2, 1, 199000, 199000),
-- Đơn hàng 2: Robot Car
(2, 1, 1, 299000, 299000),
-- Đơn hàng 3: Smart Home IoT
(3, 3, 1, 599000, 599000);

-- 10. THÔNG TIN GIAO HÀNG
INSERT INTO deliveries (order_id, tracking_code, shipping_method, delivery_status, shipped_date, delivered_date) VALUES 
(1, 'GHN123456789', 'GIAO_HANG_NHANH', 'DELIVERED', '2024-07-01 16:00:00', '2024-07-02 09:30:00'),
(2, 'GHN123456790', 'GIAO_HANG_NHANH', 'SHIPPED', '2024-07-02 17:00:00', NULL);

-- 11. KÍCH HOẠT LAB (chỉ khi đã giao hàng thành công)
INSERT INTO lab_activations (customer_id, kit_id, lab_id, order_id, is_activated, activated_at, activation_code) VALUES 
-- Customer 1 đã nhận Robot Car (order 1) -> kích hoạt các lab
(5, 1, 1, 1, TRUE, '2024-07-02 10:00:00', 'ACT-ROB-001-LAB1'),
(5, 1, 2, 1, TRUE, '2024-07-02 10:00:00', 'ACT-ROB-001-LAB2'),
(5, 1, 3, 1, TRUE, '2024-07-02 10:00:00', 'ACT-ROB-001-LAB3'),
-- Customer 1 đã nhận LED Kit (order 1) -> kích hoạt các lab
(5, 2, 4, 1, TRUE, '2024-07-02 10:00:00', 'ACT-LED-001-LAB1'),
(5, 2, 5, 1, TRUE, '2024-07-02 10:00:00', 'ACT-LED-001-LAB2');

-- 12. HỖ TRỢ KỸ THUẬT MẪU
INSERT INTO support_tickets (ticket_code, customer_id, lab_activation_id, staff_id, title, description, priority, status, support_count, assigned_at) VALUES 
('SUP20240702001', 5, 1, 3, 'Robot không di chuyển được', 'Em lắp ráp xong nhưng robot không chạy. Có thể do đâu ạ?', 'NORMAL', 'RESOLVED', 1, '2024-07-02 11:00:00'),
('SUP20240703002', 5, 4, 4, 'LED không sáng', 'LED trong mạch không sáng, em đã kiểm tra pin rồi', 'HIGH', 'IN_PROGRESS', 2, '2024-07-03 09:30:00');
