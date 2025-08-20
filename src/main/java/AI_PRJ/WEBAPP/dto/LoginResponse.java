package AI_PRJ.WEBAPP.dto;

import java.util.Set;

import AI_PRJ.WEBAPP.model.Role;
import lombok.Data;

/**
 * DTO cho response đăng nhập
 * Chứa thông tin user sau khi đăng nhập thành công
 */
@Data
public class LoginResponse {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private Set<Role> roles;
    private String message;
    private String accessToken;
    private String tokenType = "Bearer";

    public LoginResponse() {}

    public LoginResponse(String message) {
        this.message = message;
    }

    public LoginResponse(Long id, String username, String email, String fullName, Set<Role> roles, String message) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.roles = roles;
        this.message = message;
    }

    public LoginResponse(Long id, String username, String email, String fullName, Set<Role> roles, String message, String accessToken) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.roles = roles;
        this.message = message;
        this.accessToken = accessToken;
    }
}