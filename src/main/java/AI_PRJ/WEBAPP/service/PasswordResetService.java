package AI_PRJ.WEBAPP.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import AI_PRJ.WEBAPP.model.PasswordResetToken;
import AI_PRJ.WEBAPP.model.User;
import AI_PRJ.WEBAPP.repository.PasswordResetTokenRepository;
import AI_PRJ.WEBAPP.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    public void createPasswordResetToken(String email) {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Email không tồn tại trong hệ thống"));

            // Xóa token cũ nếu có
            tokenRepository.findByUser(user).ifPresent(tokenRepository::delete);

            String token = UUID.randomUUID().toString();
            PasswordResetToken resetToken = new PasswordResetToken(token, user, LocalDateTime.now().plusMinutes(15));
            tokenRepository.save(resetToken);

            // Gửi email
            String resetLink = "http://localhost:8080/auth/reset-password.html?token=" + token;
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject("Đặt lại mật khẩu - STEM Kit Lab");
            message.setText("Xin chào " + user.getUsername() + ",\n\n" +
                    "Bạn đã yêu cầu đặt lại mật khẩu. Vui lòng click vào link bên dưới để đặt lại mật khẩu:\n\n" +
                    resetLink + "\n\n" +
                    "Link này sẽ hết hạn sau 15 phút.\n\n" +
                    "Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này.\n\n" +
                    "Trân trọng,\nTeam STEM Kit Lab");
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi gửi email reset mật khẩu: " + e.getMessage());
        }
    }

    public void resetPassword(String token, String newPassword) {
        if (token == null || token.trim().isEmpty()) {
            throw new RuntimeException("Token không được để trống");
        }
        
        if (newPassword == null || newPassword.length() < 6) {
            throw new RuntimeException("Mật khẩu phải có ít nhất 6 ký tự");
        }

        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token không hợp lệ hoặc đã được sử dụng"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(resetToken); // Xóa token hết hạn
            throw new RuntimeException("Token đã hết hạn. Vui lòng yêu cầu đặt lại mật khẩu mới");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Xóa token sau khi sử dụng thành công
        tokenRepository.delete(resetToken);
    }
}
