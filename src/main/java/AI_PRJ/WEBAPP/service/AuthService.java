package AI_PRJ.WEBAPP.service;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import AI_PRJ.WEBAPP.dto.LoginRequest;
import AI_PRJ.WEBAPP.dto.LoginResponse;
import AI_PRJ.WEBAPP.dto.SignUpRequest;
import AI_PRJ.WEBAPP.model.Role;
import AI_PRJ.WEBAPP.model.RoleName;
import AI_PRJ.WEBAPP.model.User;
import AI_PRJ.WEBAPP.repository.RoleRepository;
import AI_PRJ.WEBAPP.repository.UserRepository;

/**
 * Service xử lý authentication (đăng ký, đăng nhập)
 * Đây là bài tập cơ bản nên không sử dụng Spring Security
 */
@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Đăng ký người dùng mới
     * @param signUpRequest Thông tin đăng ký
     * @return User đã được tạo
     * @throws RuntimeException nếu username hoặc email đã tồn tại
     */
    public User registerUser(SignUpRequest signUpRequest) {
        // Kiểm tra username đã tồn tại
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại!");
        }

        // Kiểm tra email đã tồn tại
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new RuntimeException("Email đã được sử dụng!");
        }

        // Tạo user mới
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        // Mã hóa password trước khi lưu
        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());
        user.setPassword(encodedPassword);
        logger.info("Đăng ký user: {} - Password đã mã hóa: {}", user.getUsername(), encodedPassword.substring(0, 10) + "...");
        user.setFullName(signUpRequest.getFullName());
        user.setPhone(signUpRequest.getPhone());
        user.setAddress(signUpRequest.getAddress());

        // Gán role mặc định là CUSTOMER
        Set<Role> roles = new HashSet<>();
        Role customerRole = roleRepository.findByName(RoleName.CUSTOMER)
                .orElseThrow(() -> new RuntimeException("Role CUSTOMER không tồn tại!"));
        roles.add(customerRole);
        user.setRoles(roles);

        // Lưu user
        return userRepository.save(user);
    }

    /**
     * Đăng nhập người dùng
     * @param loginRequest Thông tin đăng nhập
     * @return LoginResponse chứa thông tin user và message
     * @throws RuntimeException nếu thông tin đăng nhập không đúng
     */
    public LoginResponse loginUser(LoginRequest loginRequest) {
        // Tìm user theo username hoặc email
        User user = userRepository.findByUsername(loginRequest.getUsernameOrEmail())
                .or(() -> userRepository.findByEmail(loginRequest.getUsernameOrEmail()))
                .orElseThrow(() -> new RuntimeException("Tên đăng nhập hoặc email không tồn tại!"));

        // Kiểm tra password sử dụng PasswordEncoder
        logger.info("Login attempt - User: {}, Raw password length: {}, Encoded password: {}",
                   user.getUsername(), loginRequest.getPassword().length(),
                   user.getPassword().substring(0, 10) + "...");
        
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            logger.warn("Password mismatch for user: {}", user.getUsername());
            throw new RuntimeException("Mật khẩu không đúng!");
        }
        
        logger.info("Password verification successful for user: {}", user.getUsername());

        // Kiểm tra trạng thái tài khoản
        if (user.getStatus() != User.Status.ACTIVE) {
            throw new RuntimeException("Tài khoản đã bị khóa hoặc chưa được kích hoạt!");
        }

        // Tạo response
        LoginResponse response = new LoginResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setFullName(user.getFullName());
        response.setRoles(user.getRoles());
        response.setMessage("Đăng nhập thành công!");

        return response;
    }

    /**
     * Kiểm tra quyền của user
     * @param user User cần kiểm tra
     * @param roleName Tên role cần kiểm tra
     * @return true nếu user có role đó
     */
    public boolean hasRole(User user, RoleName roleName) {
        return user.getRoles().stream()
                .anyMatch(role -> role.getName() == roleName);
    }

    /**
     * Kiểm tra user có phải admin không
     * @param user User cần kiểm tra
     * @return true nếu là admin
     */
    public boolean isAdmin(User user) {
        return hasRole(user, RoleName.ADMIN);
    }

    /**
     * Kiểm tra user có phải manager không
     * @param user User cần kiểm tra
     * @return true nếu là manager
     */
    public boolean isManager(User user) {
        return hasRole(user, RoleName.MANAGER);
    }

    /**
     * Kiểm tra user có phải staff không
     * @param user User cần kiểm tra
     * @return true nếu là staff
     */
    public boolean isStaff(User user) {
        return hasRole(user, RoleName.STAFF);
    }

    /**
     * Kiểm tra user có phải customer không
     * @param user User cần kiểm tra
     * @return true nếu là customer
     */
    public boolean isCustomer(User user) {
        return hasRole(user, RoleName.CUSTOMER);
    }
}