package lee.engback.auth;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.lang.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain; // Đổi từ javax.servlet -> jakarta.servlet do phiên bản spring boot đã nâng cấp
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component // Thêm annotation này để Spring biết đây là một Bean
public class JwtFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // @Override
    // protected void doFilterInternal(HttpServletRequest request,
    // HttpServletResponse response, FilterChain filterChain)
    // throws ServletException, IOException {

    // String token = request.getHeader(HttpHeaders.AUTHORIZATION);

    // if (token == null || !token.startsWith("Bearer ")) {
    // response.sendError(HttpStatus.UNAUTHORIZED.value(), "Bạn cần đăng nhập!");
    // return;
    // }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
                String path = request.getRequestURI();
                String method = request.getMethod();
        logger.debug("Processing request: {}", request.getRequestURI());

// ✅ Nếu là /api/auth/login và đăng ký tài khoản (POST /api/members) → Bỏ qua kiểm tra JWT
        if (request.getRequestURI().equals("/api/auth/login") || ("POST".equalsIgnoreCase(method) && path.equals("/api/members"))) {
            logger.debug("Bypassing JwtFilter for login endpoint");
            filterChain.doFilter(request, response);
            return;
        }
// ✅ Nếu là /api/auth/login và đăng ký tài khoản (POST /api/members) → Bỏ qua kiểm tra JWT


        // ✅ Nếu là đăng ký tài khoản (POST /api/members) → Bỏ qua kiểm tra JWT
        // ĐẶT Ở DƯỚI NÀY THÌ LẠI SAI
        // if ("POST".equalsIgnoreCase(method) && path.equals("/api/members")) {
        //     filterChain.doFilter(request, response);
        //     return;
        // }
        // ✅ Nếu là đăng ký tài khoản (POST /api/members) → Bỏ qua kiểm tra JWT

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        logger.debug("Authorization header: {}", token);

        if (token == null || !token.startsWith("Bearer ")) {
            logger.warn("Missing or invalid Authorization header");
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Bạn cần đăng nhập!");
            return;
        }

        token = token.substring(7);
        logger.debug("Extracted token: {}", token);

        try {
            String email = jwtUtil.extractEmail(token);
            logger.debug("Extracted email: {}", email);
            request.setAttribute("userEmail", email);
            // Tạo Authentication và lưu vào SecurityContextHolder
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, null,
                    Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authToken);
            logger.debug("Set authentication for email: {}", email);

            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            logger.error("Invalid token: {}", e.getMessage());
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Token không hợp lệ!");
        }
    }
}

// Filter này giúp:

// Chặn tất cả API nếu không có Token.
// Kiểm tra Token hợp lệ trước khi tiếp tục.
// Điểm quan trọng:
// Giải mã token bằng khóa bí mật (SECRET_KEY).
// Lấy email của người dùng từ token mà không cần truy vấn database.
// Nếu token hợp lệ → Cho phép tiếp tục xử lý request.
// Nếu token không hợp lệ → Chặn request (401 Unauthorized).