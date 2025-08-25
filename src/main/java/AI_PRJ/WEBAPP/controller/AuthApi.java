package AI_PRJ.WEBAPP.controller;

import AI_PRJ.WEBAPP.dto.LoginRequest;
import AI_PRJ.WEBAPP.dto.LoginResponse;
import AI_PRJ.WEBAPP.dto.SignUpRequest;
import AI_PRJ.WEBAPP.model.User;
import AI_PRJ.WEBAPP.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST API cho Authentication
 * Đăng ký và đăng nhập hệ thống
 */
@RestController
@RequestMapping("/api/auth")
public class AuthApi {

    @Autowired
    private AuthService authService;

    /**
     * API đăng ký tài khoản mới
     * POST /api/auth/signup
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
            try {
                User user = authService.registerUser(signUpRequest);
                return ResponseEntity.ok(java.util.Map.of("message", "Đăng ký thành công! Tài khoản: " + user.getUsername()));
            } catch (RuntimeException e) {
                return ResponseEntity.badRequest().body(java.util.Map.of("message", "Lỗi đăng ký: " + e.getMessage()));
            }
        }

    /**
     * API đăng nhập hệ thống
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse response = authService.loginUser(loginRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Lỗi đăng nhập: " + e.getMessage());
        }
    }
}