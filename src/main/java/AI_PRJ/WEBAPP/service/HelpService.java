package AI_PRJ.WEBAPP.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import AI_PRJ.WEBAPP.model.Help;
import AI_PRJ.WEBAPP.repository.HelpRepo;

@Service
public class HelpService {

    private static final int MAX_SUPPORT_COUNT = 5; // max allowed support times

    @Autowired
    private HelpRepo helpRepo;

    public boolean canSupport(String user, String lab) {
        Help help = helpRepo.findByUserAndLab(user, lab);
        if (help == null) {
            return true;
        }
        return help.getSupportCount() < MAX_SUPPORT_COUNT;
    }

    @Transactional
    public void recordSupport(String user, String lab) throws IllegalStateException {
        Help help = helpRepo.findByUserAndLab(user, lab);
        if (help == null) {
            help = new Help(user, lab, 1);
        } else {
            if (help.getSupportCount() >= MAX_SUPPORT_COUNT) {
                throw new IllegalStateException("Support limit exceeded");
            }
            help.setSupportCount(help.getSupportCount() + 1);
        }
        helpRepo.save(help);
    }

    public int getTotalSupportCount() {
        return helpRepo.findAll().stream()
                .mapToInt(Help::getSupportCount)
                .sum();
    }

    @Transactional
    public Help createSupportContent(String user, String lab, String content) {
        Help help = new Help(user, lab, 1, content);
        return helpRepo.save(help);
    }

    @Transactional
    public Help updateSupportContent(String user, String lab, String content) {
        Help help = helpRepo.findByUserAndLab(user, lab);
        if (help != null) {
            help.setContent(content);
            return helpRepo.save(help);
        }
        return null; // or throw an exception
    }

    @Transactional
    public void deleteSupportContent(String user, String lab) {
        Help help = helpRepo.findByUserAndLab(user, lab);
        if (help != null) {
            helpRepo.delete(help);
        }
    }

    public Help getSupportContent(String user, String lab) {
        return helpRepo.findByUserAndLab(user, lab);
    }

    public int getSupportCount(String user, String lab) {
        Help help = helpRepo.findByUserAndLab(user, lab);
        if (help == null) {
            return 0;
        }
        return help.getSupportCount();
    }

    public java.util.List<Help> getAllHelps() {
        return helpRepo.findAll();
    }

    @Transactional
    public void deleteAllHelps() {
        helpRepo.deleteAll();
    }

    /**
     * Cung cấp hướng dẫn test API cho người dùng mới
     */
    public Map<String, Object> getApiTestingGuide(String userId) {
        Map<String, Object> guide = new HashMap<>();
        
        guide.put("title", "Hướng dẫn Test API cho Người dùng Mới");
        guide.put("description", "Tài liệu hướng dẫn chi tiết cách test các API endpoint trong hệ thống STEM Kit Lab");
        
        // Danh sách các API endpoint cần test
        Map<String, Object> endpoints = new HashMap<>();
        endpoints.put("Đăng nhập", Map.of(
            "method", "POST",
            "url", "/api/auth/signin",
            "parameters", "username, password",
            "expected_response", "JWT token và thông tin user"
        ));
        
        endpoints.put("Đăng ký", Map.of(
            "method", "POST", 
            "url", "/api/auth/signup",
            "parameters", "username, email, password, roles",
            "expected_response", "User được tạo thành công"
        ));
        
        endpoints.put("Lấy danh sách Kit", Map.of(
            "method", "GET",
            "url", "/api/kits",
            "parameters", "None (có thể có pagination)",
            "expected_response", "Danh sách các STEM kit"
        ));
        
        endpoints.put("Lấy danh sách Lab", Map.of(
            "method", "GET", 
            "url", "/api/labs",
            "parameters", "None (có thể có pagination)",
            "expected_response", "Danh sách các phòng lab"
        ));
        
        endpoints.put("Thêm vào giỏ hàng", Map.of(
            "method", "POST",
            "url", "/api/cart/add",
            "parameters", "kitId, quantity",
            "expected_response", "Sản phẩm được thêm vào giỏ hàng"
        ));
        
        endpoints.put("Hỗ trợ kỹ thuật", Map.of(
            "method", "POST",
            "url", "/api/support/record", 
            "parameters", "user, lab",
            "expected_response", "Ghi nhận hỗ trợ thành công"
        ));
        
        guide.put("endpoints", endpoints);
        
        // Tools recommendation
        guide.put("testing_tools", List.of(
            "Postman - Tool phổ biến nhất để test API",
            "Insomnia - Alternative cho Postman",
            "curl - Command line tool",
            "Thunder Client (VSCode extension) - Tiện lợi cho developers"
        ));
        
        // Testing steps
        guide.put("testing_steps", List.of(
            "1. Đảm bảo server đang chạy trên port 8080",
            "2. Sử dụng tool test API yêu thích",
            "3. Test endpoint đăng nhập trước để lấy JWT token",
            "4. Thêm Authorization header với Bearer token cho các request cần xác thực",
            "5. Test từng endpoint theo thứ tự từ đơn giản đến phức tạp",
            "6. Kiểm tra response status code và data structure"
        ));
        
        // Common issues and solutions
        guide.put("common_issues", Map.of(
            "401 Unauthorized", "Token không hợp lệ hoặc hết hạn",
            "403 Forbidden", "Không có quyền truy cập endpoint",
            "404 Not Found", "Endpoint không tồn tại hoặc parameter sai",
            "500 Internal Error", "Lỗi server, kiểm tra logs"
        ));
        
        guide.put("support_contact", "Liên hệ support@stemkitlab.com nếu cần hỗ trợ thêm");
        
        return guide;
    }

    /**
     * Đánh dấu người dùng đã hoàn thành test API
     * (Trong thực tế, có thể lưu vào database hoặc hệ thống tracking)
     */
    @Transactional
    public void markApiTestingComplete(String userId) {
        // Ghi nhận vào hệ thống rằng user đã hoàn thành test API
        // Có thể lưu vào bảng Help hoặc tạo bảng mới cho tracking API testing
        System.out.println("User " + userId + " đã hoàn thành test API");
        
        // Tạo một bản ghi hỗ trợ đặc biệt để đánh dấu
        Help help = new Help(userId, "API_TESTING", 1);
        help.setContent("User đã hoàn thành test API thành công");
        helpRepo.save(help);
    }

}
