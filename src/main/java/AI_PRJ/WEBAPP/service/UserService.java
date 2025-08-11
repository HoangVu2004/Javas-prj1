package AI_PRJ.WEBAPP.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import AI_PRJ.WEBAPP.model.Role;
import AI_PRJ.WEBAPP.model.RoleName;
import AI_PRJ.WEBAPP.model.User;
import AI_PRJ.WEBAPP.repository.RoleRepository;
import AI_PRJ.WEBAPP.repository.UserRepository;

/**
 * Service quản lý người dùng
 * Xử lý các thao tác CRUD và phân quyền cho User
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    /**
     * Lấy tất cả người dùng
     * @return List<User>
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Tìm người dùng theo ID
     * @param id ID người dùng
     * @return Optional<User>
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Tìm người dùng theo username
     * @param username Tên đăng nhập
     * @return Optional<User>
     */
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Tìm người dùng theo email
     * @param email Email
     * @return Optional<User>
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Gán role cho người dùng
     * @param userId ID người dùng
     * @param roleName Tên role cần gán
     * @return User đã được cập nhật
     */
    public User assignRole(Long userId, RoleName roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + userId));

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy role: " + roleName));

        Set<Role> roles = user.getRoles();
        roles.add(role);
        user.setRoles(roles);

        return userRepository.save(user);
    }

    /**
     * Xóa role của người dùng
     * @param userId ID người dùng
     * @param roleName Tên role cần xóa
     * @return User đã được cập nhật
     */
    public User removeRole(Long userId, RoleName roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + userId));

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy role: " + roleName));

        Set<Role> roles = user.getRoles();
        roles.remove(role);
        user.setRoles(roles);

        return userRepository.save(user);
    }

    /**
     * Cập nhật thông tin người dùng
     * @param userId ID người dùng
     * @param updatedUser Thông tin mới
     * @return User đã được cập nhật
     */
    public User updateUser(Long userId, User updatedUser) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + userId));

        // Cập nhật các field được phép
        if (updatedUser.getFullName() != null) {
            user.setFullName(updatedUser.getFullName());
        }
        if (updatedUser.getPhone() != null) {
            user.setPhone(updatedUser.getPhone());
        }
        if (updatedUser.getAddress() != null) {
            user.setAddress(updatedUser.getAddress());
        }
        if (updatedUser.getStatus() != null) {
            user.setStatus(updatedUser.getStatus());
        }

        return userRepository.save(user);
    }

    /**
     * Khóa/mở khóa tài khoản người dùng
     * @param userId ID người dùng
     * @param status Trạng thái mới
     * @return User đã được cập nhật
     */
    public User changeUserStatus(Long userId, User.Status status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + userId));
        
        user.setStatus(status);
        return userRepository.save(user);
    }

    /**
     * Xóa người dùng
     * @param userId ID người dùng
     */
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("Không tìm thấy người dùng với ID: " + userId);
        }
        userRepository.deleteById(userId);
    }

    /**
     * Lấy danh sách người dùng theo role
     * @param roleName Tên role
     * @return List<User>
     */
    public List<User> getUsersByRole(RoleName roleName) {
        return userRepository.findAll().stream()
                .filter(user -> user.getRoles().stream()
                        .anyMatch(role -> role.getName() == roleName))
                .toList();
    }
}