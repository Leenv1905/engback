package lee.engback.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lee.engback.auth.JwtFilter;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Autowired
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable()) // Tắt CSRF để tránh lỗi với Postman
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Cho phép preflight requests. cho phép OPTIONS request (cho CORS) và cho phép truy cập /api/auth/login mà không cần token:
                // khi frontend gửi sẽ gửi option trước header nên ko có sự cho phéo này sẽ lỗi
                .requestMatchers("/api/auth/login").permitAll() // ⚠️ Đảm bảo dòng này có mặt permitAll() để mở API login (không cần JWT)
                .requestMatchers(HttpMethod.POST, "/api/members").permitAll() // Cho phép đăng ký tài khoản mà không cần JWT(phải đặt trên authenticate)
                .requestMatchers(HttpMethod.GET, "/api/members/**").hasRole("ADMIN") // Chỉ admin xem danh sách member
                // .requestMatchers(HttpMethod.POST, "/api/newwords/**").hasRole("USER") // User thêm từ mới
                // Dùng .hasRole("USER") hoặc .hasRole("ADMIN") (không cần tiền tố ROLE_ vì Spring tự thêm)
                .requestMatchers("/api/members/**").authenticated() // Yêu cầu có JWT để truy cập các API liên quan đến thành viên (đăng ký thì loại trừ bên JwTFilter)
                // .requestMatchers("/api/members/**").hasRole("ADMIN") // Admin quản lý member
                .requestMatchers("/api/newwords/**").authenticated() // Các API khác cần đăng nhập(VD đây là các API thêm từ mới)
                .anyRequest().permitAll()
            )
            // .exceptionHandling(exception -> exception
            //     .authenticationEntryPoint(customAuthenticationEntryPoint()) // Sử dụng Entry Point tùy chỉnh để bắt đăng nhập khi chưa đăng nhập
            // )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // Thêm JwtFilter
            .build();
    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // khai báo phần này để sử dụng mã hóa mật khẩu

    // Phần này để điều hướng đến login khi chưa đăng nhập
//  @Bean
//     public AuthenticationEntryPoint customAuthenticationEntryPoint() {
//         return new AuthenticationEntryPoint() {
//             @Override
//             public void commence(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException authException) throws IOException {
//                 response.setStatus(HttpServletResponse.SC_FOUND); // HTTP 302 Found
//                 response.setHeader("Location", "http://localhost:3000/user/login"); // URL trang đăng nhập
//             }
//         };
//     }
}

// Mở /api/auth/login cho tất cả
// Chặn API khác nếu không có JWT