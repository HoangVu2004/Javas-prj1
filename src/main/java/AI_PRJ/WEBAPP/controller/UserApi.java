package AI_PRJ.WEBAPP.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import AI_PRJ.WEBAPP.model.RoleName;
import AI_PRJ.WEBAPP.model.User;
import AI_PRJ.WEBAPP.service.UserService;

/**
 * REST API cho quản lý User
 * Cung cấp các endpoint để quản lý người dùng và phân quyền
 */
@RestController
@RequestMapping("/api/users")
public class UserApi {

    @Autowired
    private UserService userService;

    /**
     * Lấy danh sách tất cả người dùng
     * GET /api/users
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Lấy thông tin người dùng theo ID
     * GET /api/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Lấy người dùng theo username
     * GET /api/users/username/{username}
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.getUserByUsername(username);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Gán role cho người dùng
     * PUT /api/users/{id}/roles/{roleName}
     */
    @PutMapping("/{id}/roles/{roleName}")
    public ResponseEntity<?> assignRole(@PathVariable Long id, @PathVariable RoleName roleName) {
        try {
            User updatedUser = userService.assignRole(id, roleName);
            return ResponseEntity.ok("Đã gán role " + roleName + " cho user " + updatedUser.getUsername());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    /**
     * Xóa role của người dùng
     * DELETE /api/users/{id}/roles/{roleName}
     */
    @DeleteMapping("/{id}/roles/{roleName}")
    public ResponseEntity<?> removeRole(@PathVariable Long id, @PathVariable RoleName roleName) {
        try {
            User updatedUser = userService.removeRole(id, roleName);
            return ResponseEntity.ok("Đã xóa role " + roleName + " của user " + updatedUser.getUsername());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    /**
     * Cập nhật thông tin người dùng
     * PUT /api/users/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User userUpdate) {
        try {
            User updatedUser = userService.updateUser(id, userUpdate);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    /**
     * Thay đổi trạng thái người dùng (ACTIVE/INACTIVE/BLOCKED)
     * PUT /api/users/{id}/status/{status}
     */
    @PutMapping("/{id}/status/{status}")
    public ResponseEntity<?> changeUserStatus(@PathVariable Long id, @PathVariable User.Status status) {
        try {
            User updatedUser = userService.changeUserStatus(id, status);
            return ResponseEntity.ok("Đã thay đổi trạng thái user " + updatedUser.getUsername() + " thành " + status);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    /**
     * Xóa người dùng
     * DELETE /api/users/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("Đã xóa người dùng thành công");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    /**
     * Lấy danh sách người dùng theo role
     * GET /api/users/role/{roleName}
     */
    @GetMapping("/role/{roleName}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable RoleName roleName) {
        List<User> users = userService.getUsersByRole(roleName);
        return ResponseEntity.ok(users);
    }
}