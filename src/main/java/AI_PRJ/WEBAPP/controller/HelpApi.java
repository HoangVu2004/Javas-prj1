package AI_PRJ.WEBAPP.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import AI_PRJ.WEBAPP.model.Help;
import AI_PRJ.WEBAPP.service.HelpService;

/**
 * =============================================
 * HELP API - HỖ TRỢ KỸ THUẬT CHO KHÁCH HÀNG
 * =============================================
 *
 * Phân quyền theo vai trò:
 * - STAFF: Có thể ghi nhận hỗ trợ và xem thống kê
 * - MANAGER, ADMIN: Có thể xem tất cả và quản lý
 * - CUSTOMER: Có thể yêu cầu hỗ trợ (chỉ xem của mình)
 */
@RestController
@RequestMapping("/api/support")
@CrossOrigin(origins = "*", maxAge = 3600)
public class HelpApi {

    @Autowired
    protected HelpService helpService; // Changed from private to protected

    /**
     * Ghi nhận hỗ trợ kỹ thuật
     * Truy cập: STAFF, MANAGER, ADMIN
     */
    @PostMapping("/record")
    @PreAuthorize("hasRole('STAFF') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<String> recordSupport(@RequestParam String user, @RequestParam String lab) {
        try {
            if (!helpService.canSupport(user, lab)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Support limit exceeded");
            }
            helpService.recordSupport(user, lab);
            return ResponseEntity.ok("Support recorded");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error");
        }
    }

    /**
     * Xem số lần hỗ trợ đã thực hiện
     * Truy cập: STAFF, MANAGER, ADMIN
     */
    @GetMapping("/count")
    @PreAuthorize("hasRole('STAFF') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<Integer> getSupportCount(@RequestParam(required = false) String user, @RequestParam(required = false) String lab) {
        if (user == null || lab == null) {
            // Return total support count if parameters are missing
            return ResponseEntity.ok(helpService.getTotalSupportCount());
        }
        int count = helpService.getSupportCount(user, lab);
        return ResponseEntity.ok(count);
    }

    /**
     * Tạo nội dung hỗ trợ mới
     * Truy cập: Tất cả người dùng
     */
    @PostMapping("/content")
    public ResponseEntity<?> createSupportContent(@RequestParam String user, @RequestParam String lab, @RequestParam String content) {
        try {
            Help help = helpService.createSupportContent(user, lab, content);
            return ResponseEntity.ok(help);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi tạo nội dung hỗ trợ");
        }
    }

    /**
     * Cập nhật nội dung hỗ trợ
     * Truy cập: Tất cả người dùng
     */
    @PutMapping("/content")
    public ResponseEntity<?> updateSupportContent(@RequestParam String user, @RequestParam String lab, @RequestParam String content) {
        try {
            Help help = helpService.updateSupportContent(user, lab, content);
            if (help != null) {
                return ResponseEntity.ok(help);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy nội dung hỗ trợ");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi cập nhật nội dung hỗ trợ");
        }
    }

    /**
     * Xóa nội dung hỗ trợ
     * Truy cập: Tất cả người dùng
     */
    @DeleteMapping("/content")
    public ResponseEntity<String> deleteSupportContent(@RequestParam String user, @RequestParam String lab) {
        try {
            helpService.deleteSupportContent(user, lab);
            return ResponseEntity.ok("Đã xóa nội dung hỗ trợ");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa nội dung hỗ trợ");
        }
    }

    /**
     * Xem nội dung hỗ trợ cụ thể
     * Truy cập: Tất cả người dùng
     */
    @GetMapping("/content")
    public ResponseEntity<?> getSupportContent(@RequestParam String user, @RequestParam String lab) {
        try {
            Help help = helpService.getSupportContent(user, lab);
            if (help != null) {
                return ResponseEntity.ok(help);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy nội dung hỗ trợ");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi lấy nội dung hỗ trợ");
        }
    }

    /**
     * Xem tất cả lịch sử hỗ trợ
     * Truy cập: MANAGER, ADMIN (để báo cáo và quản lý)
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<Help>> getAllHelps() {
        List<Help> helps = helpService.getAllHelps();
        return ResponseEntity.ok(helps);
    }

    // Add reset endpoint for testing to clear all help data
    /**
     * Reset dữ liệu hỗ trợ (chỉ dùng cho testing)
     * Truy cập: Chỉ ADMIN
     */
    @DeleteMapping("/reset")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> resetHelpData() {
        helpService.deleteAllHelps();
        return ResponseEntity.ok("Help data reset");
    }

    /**
     * Hỗ trợ người dùng chưa test API - cung cấp hướng dẫn chi tiết
     * Truy cập: Tất cả người dùng
     */
    @GetMapping("/api-testing-guide")
    public ResponseEntity<?> getApiTestingGuide(@RequestParam(required = false) String userId) {
        try {
            Map<String, Object> guide = helpService.getApiTestingGuide(userId);
            return ResponseEntity.ok(guide);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi lấy hướng dẫn test API: " + e.getMessage());
        }
    }

    /**
     * Đánh dấu người dùng đã hoàn thành test API
     * Truy cập: Tất cả người dùng
     */
    @PostMapping("/api-testing-complete")
    public ResponseEntity<String> markApiTestingComplete(@RequestParam String userId) {
        try {
            helpService.markApiTestingComplete(userId);
            return ResponseEntity.ok("Đã đánh dấu hoàn thành test API");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi đánh dấu hoàn thành test API: " + e.getMessage());
        }
    }

}
