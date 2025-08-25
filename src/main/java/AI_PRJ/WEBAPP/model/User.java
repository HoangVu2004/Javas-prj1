package AI_PRJ.WEBAPP.model;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity User - Bảng người dùng
 * Chứa thông tin cơ bản của người dùng và liên kết với bảng Role
 */
@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Tên đăng nhập - duy nhất trong hệ thống
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    // Email - duy nhất trong hệ thống
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    // Mật khẩu đã mã hóa
    @Column(nullable = false, length = 255)
    private String password;

    // Họ và tên đầy đủ
    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    // Số điện thoại (không bắt buộc)
    @Column(length = 15)
    private String phone;

    // Địa chỉ (không bắt buộc)
    @Column(columnDefinition = "TEXT")
    private String address;

    // Trạng thái tài khoản
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('ACTIVE', 'INACTIVE', 'BLOCKED') DEFAULT 'ACTIVE'")
    private Status status = Status.ACTIVE;

    // Thời gian tạo tài khoản
    @Column(name = "created_at", updatable = false, insertable = false, 
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    // Thời gian cập nhật cuối
    @Column(name = "updated_at", updatable = false, insertable = false, 
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Timestamp updatedAt;

    // Liên kết Many-to-Many với bảng Role
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    /**
     * Enum trạng thái tài khoản
     * ACTIVE: Hoạt động bình thường
     * INACTIVE: Tạm ngừng hoạt động
     * BLOCKED: Bị khóa vĩnh viễn
     */
    public enum Status {
        ACTIVE,     // Hoạt động
        INACTIVE,   // Tạm ngừng
        BLOCKED     // Bị khóa
    }
}