package AI_PRJ.WEBAPP.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ResponseEntity<Integer> getSupportCount(@RequestParam String user, @RequestParam String lab) {
        int count = helpService.getSupportCount(user, lab);
        return ResponseEntity.ok(count);
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
}
