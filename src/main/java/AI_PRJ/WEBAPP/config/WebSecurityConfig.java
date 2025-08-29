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

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



import AI_PRJ.WEBAPP.security.CustomUserDetailsService;

import AI_PRJ.WEBAPP.security.JwtAuthenticationEntryPoint;

import AI_PRJ.WEBAPP.security.JwtAuthenticationFilter;



/**

 * =============================================

 * SPRING SECURITY CONFIGURATION - JWT ONLY

 * =============================================

 * 

 * Cấu hình bảo mật cho hệ thống STEM Kit sử dụng JWT

 * 

 * Tính năng:

 * - JWT Authentication (không dùng session)

 * - Role-based Authorization: ADMIN, MANAGER, STAFF, CUSTOMER

 * - Stateless security

 */

@Configuration

@EnableWebSecurity

public class WebSecurityConfig {



    @Autowired

    private CustomUserDetailsService userDetailsService;



    @Autowired

    private JwtAuthenticationEntryPoint unauthorizedHandler;



    /**

     * JWT Authentication Filter Bean

     */

    @Bean

    public JwtAuthenticationFilter authenticationJwtTokenFilter() {

        return new JwtAuthenticationFilter();

    }



    /**

     * MAIN SECURITY CONFIGURATION

     * ===========================

     */

    @Bean

    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http


                // ========== STATELESS JWT SETUP ==========


                .csrf(csrf -> csrf.disable())


                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))





                // ========== DISABLE FORM & BASIC AUTH ==========


                .formLogin(form -> form.disable())


                .httpBasic(basic -> basic.disable())





                // ========== API AUTHORIZATION RULES ==========


                .authorizeHttpRequests(auth -> auth


                        // Public endpoints (không cần JWT)


                        .requestMatchers(HttpMethod.POST, "/api/auth/login", "/api/auth/signup").permitAll()


                        .requestMatchers("/api/test/**").permitAll()


                        .requestMatchers("/index.html", "/auth/**", "/index.html", "/public/products.html",


                                "/public/product-detail.html", "/**")


                        .permitAll()





                        .requestMatchers(HttpMethod.POST, "/auth/forgot-password", "/auth/reset-password").permitAll()


                        .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()


                        // Ensure root path is accessible without login


                        .requestMatchers("/").permitAll()


                        // .requestMatchers("/api/labs/**").permitAll() // Allow access to labs without


                        // auth





                        // User management endpoints - phân quyền theo role


                        .requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")


                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")


                        .requestMatchers(HttpMethod.PUT, "/api/users/*/roles/**").hasRole("ADMIN")


                        .requestMatchers(HttpMethod.DELETE, "/api/users/*/roles/**").hasRole("ADMIN")


                        .requestMatchers("/api/admin/**").hasRole("ADMIN")





                        // ========== MANAGER PERMISSIONS ==========


                        // Quản lý users


                        .requestMatchers(HttpMethod.GET, "/api/users/**").hasAnyRole("ADMIN", "MANAGER")


                        .requestMatchers(HttpMethod.PUT, "/api/users/**").hasAnyRole("ADMIN", "MANAGER")


                        .requestMatchers(HttpMethod.PUT, "/api/users/*/status/**").hasAnyRole("ADMIN", "MANAGER")


                        .requestMatchers(HttpMethod.GET, "/api/users/role/**").hasAnyRole("ADMIN", "MANAGER")





                        // Quản lý KIT và LAB


                        .requestMatchers("/api/labs/**").hasAnyRole("ADMIN", "MANAGER")





                        // Quản lý giao nhận


                        .requestMatchers("/api/shipments/**").hasAnyRole("ADMIN", "MANAGER")


                        .requestMatchers("/api/orders/*/status/**").hasAnyRole("ADMIN", "MANAGER")





                        // Báo cáo và thống kê


                        .requestMatchers("/api/reports/**").hasAnyRole("ADMIN", "MANAGER")


                        .requestMatchers("/api/statistics/**").hasAnyRole("ADMIN", "MANAGER")





                        // ========== STAFF PERMISSIONS ==========


                        // Hỗ trợ kỹ thuật


                        .requestMatchers("/api/support/**").hasAnyRole("ADMIN", "MANAGER", "STAFF")


                        .requestMatchers(HttpMethod.GET, "/api/labs/*/help").hasAnyRole("ADMIN", "MANAGER", "STAFF")


                        .requestMatchers(HttpMethod.POST, "/api/support/tickets")


                        .hasAnyRole("ADMIN", "MANAGER", "STAFF")


                        .requestMatchers(HttpMethod.PUT, "/api/support/tickets/**")


                        .hasAnyRole("ADMIN", "MANAGER", "STAFF")





                        // Xem thông tin khách hàng (để hỗ trợ)


                        .requestMatchers(HttpMethod.GET, "/api/customers/**").hasAnyRole("ADMIN", "MANAGER", "STAFF")





                        // ========== CUSTOMER PERMISSIONS ==========


                        // Mua sắm


                        .requestMatchers(HttpMethod.GET, "/api/kits")


                        .hasAnyRole("ADMIN", "MANAGER", "STAFF", "CUSTOMER")


                        .requestMatchers(HttpMethod.GET, "/api/kits/*")


                        .hasAnyRole("ADMIN", "MANAGER", "STAFF", "CUSTOMER")


                        .requestMatchers(HttpMethod.POST, "/api/kits").authenticated()


                        .requestMatchers("/api/orders").hasAnyRole("ADMIN", "MANAGER", "CUSTOMER")


                        .requestMatchers("/api/cart/**").hasRole("CUSTOMER")





                        // Truy cập bài LAB (chỉ khi đã mua KIT)


                        .requestMatchers(HttpMethod.GET, "/api/my-labs/**").hasRole("CUSTOMER")


                        .requestMatchers(HttpMethod.POST, "/api/support/request").hasRole("CUSTOMER")





                        // Profile cá nhân


                        .requestMatchers(HttpMethod.GET, "/api/profile").authenticated()


                        .requestMatchers(HttpMethod.PUT, "/api/profile").authenticated()





                        // Tất cả API khác cần authentication


                        .anyRequest().authenticated())





                // ========== JWT EXCEPTION HANDLING ==========


                .exceptionHandling(exceptions -> exceptions


                        .authenticationEntryPoint(unauthorizedHandler))





                // ========== JWT FILTER ==========


                .authenticationProvider(authenticationProvider())


                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);



        return http.build();

    }



    /**

     * ========== REQUIRED BEANS ==========

     */




    @Bean

    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();

    }



    @Bean

    public DaoAuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);

        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;

    }



    @Bean

    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {

        return config.getAuthenticationManager();

    }
}