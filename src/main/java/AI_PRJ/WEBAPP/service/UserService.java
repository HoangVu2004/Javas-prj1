package AI_PRJ.WEBAPP.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import AI_PRJ.WEBAPP.model.Role;
import AI_PRJ.WEBAPP.model.RoleName;
import AI_PRJ.WEBAPP.model.User;
import AI_PRJ.WEBAPP.repository.RoleRepository;
import AI_PRJ.WEBAPP.repository.UserRepository;

/**
 * Service quản lý người dùng với kiểm soát phân quyền nghiêm ngặt
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
     * @return User
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

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
     * Gán role cho người dùng với kiểm tra phân cấp quyền nghiêm ngặt
     * @param userId ID người dùng
     * @param roleName Tên role cần gán
     * @return User đã được cập nhật
     */
    public User assignRole(Long userId, RoleName roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + userId));

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy role: " + roleName));

        // Lấy thông tin user hiện tại đang thực hiện hành động
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();
        User currentUser = findByUsername(currentUsername);
        
        // Kiểm tra quyền phân quyền
        validateRoleAssignment(currentUser, user, roleName);
        
        // Thực hiện gán role (Single Role - xóa role cũ, chỉ giữ 1 role)
        user.getRoles().clear();
        user.getRoles().add(role);

        return userRepository.save(user);
    }

    /**
     * Kiểm tra logic phân cấp quyền nghiêm ngặt
     * @param currentUser User hiện tại thực hiện hành động
     * @param targetUser User được gán role
     * @param newRole Role mới được gán
     */
    private void validateRoleAssignment(User currentUser, User targetUser, RoleName newRole) {
        // Lấy role cao nhất của user hiện tại
        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(r -> r.getName() == RoleName.ADMIN);
        boolean isManager = currentUser.getRoles().stream()
                .anyMatch(r -> r.getName() == RoleName.MANAGER);
        
        // Kiểm tra target user có phải Admin không
        boolean targetIsAdmin = targetUser.getRoles().stream()
                .anyMatch(r -> r.getName() == RoleName.ADMIN);

        if (isAdmin) {
            // ADMIN có toàn quyền - có thể gán bất kỳ role nào kể cả ADMIN
            System.out.println("ADMIN đang thực hiện phân quyền: " + newRole + " cho user ID: " + targetUser.getId());
            return;
        }
        
        if (isManager) {
            // MANAGER bị giới hạn quyền hạn
            if (newRole == RoleName.ADMIN) {
                throw new RuntimeException("Manager không có quyền gán role Admin. Chỉ Admin mới có thể tạo Admin mới.");
            }
            
            if (newRole != RoleName.CUSTOMER && newRole != RoleName.STAFF) {
                throw new RuntimeException("Manager chỉ có thể gán role Customer hoặc Staff. Role được yêu cầu: " + newRole);
            }
            
            // MANAGER không được can thiệp vào tài khoản Admin hiện có
            if (targetIsAdmin) {
                throw new RuntimeException("Manager không có quyền thay đổi tài khoản Admin. Tài khoản này thuộc quyền quản lý của Admin.");
            }
            
            System.out.println("MANAGER đang thực hiện phân quyền: " + newRole + " cho user ID: " + targetUser.getId());
            return;
        }
        
        // STAFF và CUSTOMER không có quyền phân quyền
        throw new RuntimeException("Bạn không có quyền phân quyền cho người dùng khác. Chỉ Admin và Manager mới có quyền này.");
    }

    /**
     * Xóa role của người dùng với kiểm tra phân cấp quyền
     * @param userId ID người dùng
     * @param roleName Tên role cần xóa
     * @return User đã được cập nhật
     */
    public User removeRole(Long userId, RoleName roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + userId));

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy role: " + roleName));

        // Kiểm tra quyền trước khi xóa
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();
        User currentUser = findByUsername(currentUsername);
        
        validateRoleRemoval(currentUser, user, roleName);

        Set<Role> roles = user.getRoles();
        roles.removeIf(r -> r.getName().equals(roleName));
        user.setRoles(roles);

        return userRepository.save(user);
    }

    /**
     * Kiểm tra quyền xóa role
     * @param currentUser User hiện tại
     * @param targetUser User bị xóa role
     * @param roleToRemove Role cần xóa
     */
    private void validateRoleRemoval(User currentUser, User targetUser, RoleName roleToRemove) {
        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(r -> r.getName() == RoleName.ADMIN);
        boolean isManager = currentUser.getRoles().stream()
                .anyMatch(r -> r.getName() == RoleName.MANAGER);

        if (isAdmin) {
            // ADMIN có toàn quyền xóa bất kỳ role nào
            return; 
        }
        
        if (isManager) {
            // MANAGER chỉ được xóa CUSTOMER và STAFF
            if (roleToRemove == RoleName.ADMIN) {
                throw new RuntimeException("Manager không có quyền xóa role Admin");
            }
            
            // Không được can thiệp vào Admin account
            boolean targetIsAdmin = targetUser.getRoles().stream()
                    .anyMatch(r -> r.getName() == RoleName.ADMIN);
            if (targetIsAdmin) {
                throw new RuntimeException("Manager không có quyền thay đổi tài khoản Admin");
            }
            
            return;
        }
        
        throw new RuntimeException("Bạn không có quyền xóa role của người dùng khác");
    }

    /**
     * Lấy users theo role
     */
    public List<User> getUsersByRole(RoleName roleName) {
        return userRepository.findByRolesName(roleName);
    }

    /**
     * Xóa user
     */
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    /**
     * Thay đổi trạng thái user
     */
    public User changeUserStatus(Long id, User.Status status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus(status);
        return userRepository.save(user);
    }

    /**
     * Cập nhật thông tin user
     */
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (userDetails.getUsername() != null) {
            user.setUsername(userDetails.getUsername());
        }
        if (userDetails.getEmail() != null) {
            user.setEmail(userDetails.getEmail());
        }
        if (userDetails.getFullName() != null) {
            user.setFullName(userDetails.getFullName());
        }
        if (userDetails.getPhone() != null) {
            user.setPhone(userDetails.getPhone());
        }
        if (userDetails.getAddress() != null) {
            user.setAddress(userDetails.getAddress());
        }

        return userRepository.save(user);
    }
}
