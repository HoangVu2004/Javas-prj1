package AI_PRJ.WEBAPP.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import AI_PRJ.WEBAPP.security.CustomUserDetailsService;

/**
 * Cấu hình Spring Security cho hệ thống phân quyền
 * Định nghĩa các rule truy cập theo role: ADMIN, MANAGER, STAFF, CUSTOMER
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    /**
     * Cấu hình Security Filter Chain
     * Định nghĩa quyền truy cập cho từng endpoint
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF cho API REST
            .csrf(csrf -> csrf.disable())
            
            // Cấu hình session management - STATELESS cho API
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // Cấu hình authorization
            .authorizeHttpRequests(auth -> auth
                // Public endpoints - không cần authentication
                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/signup").permitAll()
                .requestMatchers("/api/test/**").permitAll()
                
                // Temporarily permit all for testing HTML files
                .requestMatchers("/api/kits/**").permitAll()
                .requestMatchers("/api/labs/**").permitAll()
                .requestMatchers("/help/**").permitAll()
                .requestMatchers("/api/ship/**").permitAll()

                // User management endpoints - phân quyền theo role
                .requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/users/**").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers(HttpMethod.PUT, "/api/users/*/roles/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/users/*/roles/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/users/**").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/users/*/status/**").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers(HttpMethod.GET, "/api/users/role/**").hasAnyRole("ADMIN", "MANAGER")
                
                // Tất cả requests khác cần authentication
                .anyRequest().authenticated()
            )
            
            // Cấu hình HTTP Basic Authentication (cho testing)
            .httpBasic(basic -> basic.realmName("STEM Kit API"))
            
            // Tắt hoàn toàn form login cho REST API
            .formLogin(form -> form.disable())
            
            // Cấu hình logout
            .logout(logout -> logout
                .logoutUrl("/api/auth/logout")
                .logoutSuccessUrl("/")
                .permitAll()
            )
            
            // Sử dụng custom authentication provider
            .authenticationProvider(authenticationProvider());

        return http.build();
    }

    /**
     * Bean cho password encoder
     * Sử dụng BCrypt để mã hóa password
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean cho authentication provider
     * Kết nối UserDetailsService với PasswordEncoder
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Bean cho authentication manager
     * Cần thiết cho authentication process
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}