package lee.engback.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import lee.engback.auth.JwtFilter;

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
                .requestMatchers("/api/auth/login").permitAll() // ⚠️ Đảm bảo dòng này có mặt permitAll() để mở API login (không cần JWT)
                .requestMatchers(HttpMethod.POST, "/api/members").permitAll() // Cho phép đăng ký tài khoản mà không cần JWT(phải đặt trên authenticate)
                .requestMatchers("/api/members/**").authenticated() // Yêu cầu có JWT để truy cập các API liên quan đến thành viên (đăng ký thì loại trừ bên JwTFilter)
                .requestMatchers("/api/newwords/**").authenticated() // Các API khác cần đăng nhập(VD đây là các API thêm từ mới)
                .anyRequest().permitAll()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // Thêm JwtFilter
            .build();
    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // khai báo phần này để sử dụng mã hóa mật khẩu

}

// Mở /api/auth/login cho tất cả
// Chặn API khác nếu không có JWT