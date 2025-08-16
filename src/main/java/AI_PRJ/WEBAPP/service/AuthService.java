package AI_PRJ.WEBAPP.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import AI_PRJ.WEBAPP.security.JwtUtils;

/**
 * Service xử lý authentication (đăng ký, đăng nhập)
 * Sử dụng Spring Security với JWT token
 */
@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

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
     * @return LoginResponse chứa thông tin user và JWT token
     * @throws RuntimeException nếu thông tin đăng nhập không đúng
     */
    public LoginResponse loginUser(LoginRequest loginRequest) {
        logger.info("Attempting login for user: {}", loginRequest.getUsernameOrEmail());
        
        try {
            // Sử dụng Spring Security để authenticate
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsernameOrEmail(),
                    loginRequest.getPassword()
                )
            );

            logger.info("Authentication successful for user: {}", loginRequest.getUsernameOrEmail());

            // Set authentication vào SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Tạo JWT token
            String jwt = jwtUtils.generateJwtToken(authentication);

            // Lấy thông tin user từ database
            User user = userRepository.findByUsername(loginRequest.getUsernameOrEmail())
                    .or(() -> userRepository.findByEmail(loginRequest.getUsernameOrEmail()))
                    .orElseThrow(() -> new RuntimeException("User không tồn tại!"));

            logger.info("User {} đăng nhập thành công", user.getUsername());

            // Tạo response với JWT token
            LoginResponse response = new LoginResponse();
            response.setId(user.getId());
            response.setUsername(user.getUsername());
            response.setEmail(user.getEmail());
            response.setFullName(user.getFullName());
            response.setRoles(user.getRoles());
            response.setMessage("Đăng nhập thành công!");
            response.setAccessToken(jwt);
            response.setTokenType("Bearer");

            return response;

        } catch (Exception e) {
            logger.error("Lỗi đăng nhập cho user {}: {}", loginRequest.getUsernameOrEmail(), e.getMessage());
            logger.error("Exception type: {}", e.getClass().getSimpleName());
            throw new RuntimeException("Thông tin đăng nhập không đúng!");
        }
    }
}