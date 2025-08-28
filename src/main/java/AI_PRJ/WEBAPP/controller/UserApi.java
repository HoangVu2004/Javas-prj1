package AI_PRJ.WEBAPP.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import AI_PRJ.WEBAPP.model.Role;
import AI_PRJ.WEBAPP.model.RoleName;
import AI_PRJ.WEBAPP.model.User;
import AI_PRJ.WEBAPP.service.UserService;

/**
 * =============================================
 * USER API - QUẢN LÝ TÀI KHOẢN NGƯỜI DÙNG  
 * =============================================
 * 
 * Phân quyền nghiêm ngặt theo yêu cầu đề bài:
 * - ADMIN: Toàn quyền quản lý users và phân quyền
 * - MANAGER: Quản lý tài khoản người dùng (khách hàng & nhân viên)
 * - STAFF, CUSTOMER: Chỉ xem thông tin cá nhân
 * 
 * Bảo mật:
 * - Chỉ ADMIN có thể gán/xóa roles
 * - MANAGER có thể quản lý user accounts nhưng không chỉnh sửa ADMIN
 * - Method-level security với @PreAuthorize
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserApi {

    @Autowired
    private UserService userService;

    /**
     * Xem tất cả users - Dành cho ADMIN
     * Truy cập: Chỉ ADMIN (toàn quyền)
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(new ApiResponse(true, 
                "Lấy danh sách users thành công", users));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Lỗi server: " + e.getMessage()));
        }
    }

    /**
     * Xem chi tiết user - ADMIN và MANAGER
     * Truy cập: ADMIN (tất cả), MANAGER (quản lý users)
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            Optional<User> userOpt = userService.getUserById(id);
            
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Không tìm thấy user với ID: " + id));
            }
            
            return ResponseEntity.ok(new ApiResponse(true, 
                "Lấy thông tin user thành công", userOpt.get()));
                
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Lỗi server: " + e.getMessage()));
        }
    }

    /**
     * Cập nhật user
     * Truy cập: ADMIN và MANAGER (quản lý tài khoản người dùng)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(id, user);
            return ResponseEntity.ok(new ApiResponse(true, 
                "Cập nhật user thành công", updatedUser));
                
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Lỗi server: " + e.getMessage()));
        }
    }

    /**
     * Xóa user - Chỉ ADMIN
     * Truy cập: Chỉ ADMIN (quyền cao nhất)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(new ApiResponse(true, "Xóa user thành công"));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Lỗi server: " + e.getMessage()));
        }
    }

    /**
     * Xem users theo role - ADMIN và MANAGER
     * Truy cập: ADMIN, MANAGER (để quản lý theo vai trò)
     */
    @GetMapping("/role/{roleName}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<?> getUsersByRole(@PathVariable String roleName) {
        try {
            RoleName roleEnum = RoleName.valueOf(roleName.toUpperCase());
            List<User> users = userService.getUsersByRole(roleEnum);
            return ResponseEntity.ok(new ApiResponse(true, 
                "Lấy users theo role thành công", users));
                
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Lỗi server: " + e.getMessage()));
        }
    }

    /**
     * Gán role cho user - Chỉ ADMIN
     * Truy cập: Chỉ ADMIN (quản lý phân quyền)
     */
    @PutMapping("/{userId}/roles/{roleNameStr}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addRoleToUser(@PathVariable Long userId, @PathVariable String roleNameStr) {
        try {
            RoleName roleName = RoleName.valueOf(roleNameStr.toUpperCase());
            User user = userService.assignRole(userId, roleName);
            
            return ResponseEntity.ok(new ApiResponse(true, 
                "Gán role cho user thành công", user));
                
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Lỗi server: " + e.getMessage()));
        }
    }

    /**
     * Xóa role khỏi user - Chỉ ADMIN
     * Truy cập: Chỉ ADMIN (quản lý phân quyền)
     */
    @DeleteMapping("/{userId}/roles/{roleNameStr}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeRoleFromUser(@PathVariable Long userId, @PathVariable String roleNameStr) {
        try {
            RoleName roleName = RoleName.valueOf(roleNameStr.toUpperCase());
            User user = userService.removeRole(userId, roleName);
            
            return ResponseEntity.ok(new ApiResponse(true, 
                "Xóa role khỏi user thành công", user));
                
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Lỗi server: " + e.getMessage()));
        }
    }

    /**
     * Cập nhật trạng thái user (enable/disable) - ADMIN và MANAGER
     * Truy cập: ADMIN, MANAGER (quản lý tài khoản người dùng)
     */
    @PutMapping("/{id}/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<?> updateUserStatus(@PathVariable Long id, @PathVariable String status) {
        try {
            User.Status userStatus = User.Status.valueOf(status.toUpperCase());
            User user = userService.changeUserStatus(id, userStatus);
            
            return ResponseEntity.ok(new ApiResponse(true, 
                "Cập nhật trạng thái user thành công", user));
                
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Lỗi server: " + e.getMessage()));
        }
    }

    /**
     * Lấy tất cả roles của user theo ID
     * Truy cập: ADMIN, MANAGER (quản lý users), hoặc chính user đó
     */
    @GetMapping("/{userId}/roles")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or authentication.name == @userService.getUserById(#userId).orElse(new AI_PRJ.WEBAPP.model.User()).username")
    public ResponseEntity<?> getUserRoles(@PathVariable Long userId) {
        try {
            Optional<User> userOpt = userService.getUserById(userId);
            
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Không tìm thấy user với ID: " + userId));
            }
            
            User user = userOpt.get();
            
            // Tạo response chỉ chứa thông tin roles
            var roleResponse = new Object() {
                public final Long userId = user.getId();
                public final String username = user.getUsername();
                public final String email = user.getEmail();
                public final Set<Role> roles = user.getRoles();
            };
            
            return ResponseEntity.ok(new ApiResponse(true,
                "Lấy roles của user thành công", roleResponse));
                
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Lỗi server: " + e.getMessage()));
        }
    }

    /**
     * Inner class for API Response
     */
    private static class ApiResponse {
        private boolean success;
        private String message;
        private Object data;

        public ApiResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public ApiResponse(boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }

        // Getters
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public Object getData() { return data; }
    }
}