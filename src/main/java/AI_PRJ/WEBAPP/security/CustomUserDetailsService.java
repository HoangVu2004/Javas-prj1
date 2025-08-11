package AI_PRJ.WEBAPP.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import AI_PRJ.WEBAPP.model.User;
import AI_PRJ.WEBAPP.repository.UserRepository;

/**
 * Custom UserDetailsService để Spring Security authenticate user từ database
 * Implements UserDetailsService interface của Spring Security
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Load user by username hoặc email
     * Spring Security sẽ gọi method này khi user login
     * 
     * @param usernameOrEmail username hoặc email
     * @return UserDetails object chứa thông tin authentication
     * @throws UsernameNotFoundException nếu user không tồn tại
     */
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        // Tìm user theo username hoặc email
        User user = userRepository.findByUsername(usernameOrEmail)
                .or(() -> userRepository.findByEmail(usernameOrEmail))
                .orElseThrow(() -> new UsernameNotFoundException(
                    "User không tồn tại với username/email: " + usernameOrEmail));

        // Kiểm tra trạng thái tài khoản
        if (user.getStatus() != User.Status.ACTIVE) {
            throw new UsernameNotFoundException("Tài khoản đã bị khóa hoặc chưa được kích hoạt: " + usernameOrEmail);
        }

        // Convert roles thành GrantedAuthority
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().name()))
                .collect(Collectors.toList());

        // Tạo UserDetails object
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(user.getStatus() != User.Status.ACTIVE)
                .credentialsExpired(false)
                .disabled(user.getStatus() != User.Status.ACTIVE)
                .build();
    }
}