# Hướng dẫn Test API - STEM Kit Lab

## 📋 Giới thiệu
Tài liệu này cung cấp hướng dẫn chi tiết để test các API endpoint trong hệ thống STEM Kit Lab dành cho người dùng mới.

## 🚀 API Endpoints mới cho Testing

### 1. Lấy hướng dẫn test API
**Endpoint:** `GET /api/support/api-testing-guide`

**Parameters:**
- `userId` (optional): ID của người dùng

**Response:**
```json
{
  "title": "Hướng dẫn Test API cho Người dùng Mới",
  "description": "Tài liệu hướng dẫn chi tiết cách test các API endpoint...",
  "endpoints": {...},
  "testing_tools": [...],
  "testing_steps": [...],
  "common_issues": {...}
}
```

### 2. Đánh dấu hoàn thành test API
**Endpoint:** `POST /api/support/api-testing-complete`

**Parameters:**
- `userId` (required): ID của người dùng đã hoàn thành test

**Response:** `"Đã đánh dấu hoàn thành test API"`

## 🛠️ Các công cụ test API được đề xuất

1. **Postman** - Tool phổ biến nhất để test API
2. **Insomnia** - Alternative cho Postman
3. **curl** - Command line tool
4. **Thunder Client** (VSCode extension) - Tiện lợi cho developers

## 📝 Các bước test API

1. **Đảm bảo server đang chạy** trên port 8080
2. **Sử dụng tool test API** yêu thích
3. **Test endpoint đăng nhập** trước để lấy JWT token
4. **Thêm Authorization header** với Bearer token cho các request cần xác thực
5. **Test từng endpoint** theo thứ tự từ đơn giản đến phức tạp
6. **Kiểm tra response status code** và data structure

## 🔍 Các lỗi thường gặp và giải pháp

| Mã lỗi | Nguyên nhân | Giải pháp |
|--------|-------------|-----------|
| 401 Unauthorized | Token không hợp lệ hoặc hết hạn | Đăng nhập lại để lấy token mới |
| 403 Forbidden | Không có quyền truy cập endpoint | Kiểm tra role và quyền của user |
| 404 Not Found | Endpoint không tồn tại hoặc parameter sai | Kiểm tra lại URL và parameters |
| 500 Internal Error | Lỗi server | Kiểm tra logs server |

## 📞 Hỗ trợ kỹ thuật
Liên hệ: support@stemkitlab.com nếu cần hỗ trợ thêm

## 🎯 Các endpoint chính cần test

### Authentication
- `POST /api/auth/signin` - Đăng nhập
- `POST /api/auth/signup` - Đăng ký

### Products
- `GET /api/kits` - Lấy danh sách Kit
- `GET /api/labs` - Lấy danh sách Lab

### Shopping Cart
- `POST /api/cart/add` - Thêm vào giỏ hàng

### Support
- `POST /api/support/record` - Ghi nhận hỗ trợ kỹ thuật
- `GET /api/support/api-testing-guide` - Hướng dẫn test API (mới)
- `POST /api/support/api-testing-complete` - Đánh dấu hoàn thành test (mới)

## 💡 Mẹo test hiệu quả

1. **Bắt đầu với endpoints không cần authentication** trước
2. **Lưu responses** để so sánh và debug
3. **Test edge cases** (dữ liệu không hợp lệ, missing parameters)
4. **Kiểm tra error handling** của từng endpoint
5. **Verify response structure** theo documentation

## 📊 Theo dõi tiến độ test
Sử dụng endpoint `POST /api/support/api-testing-complete` để đánh dấu khi hoàn thành test mỗi user.

Happy Testing! 🎉
