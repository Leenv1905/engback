package lee.engback.auth;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain; // Đổi từ javax.servlet -> jakarta.servlet do phiên bản spring boot đã nâng cấp
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component // Thêm annotation này để Spring biết đây là một Bean
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (token == null || !token.startsWith("Bearer ")) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Bạn cần đăng nhập!");
            return;
        }

        token = token.substring(7);

        try {
            String email = jwtUtil.extractEmail(token);
            request.setAttribute("userEmail", email);
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Token không hợp lệ!");
        }
    }
}




// Filter này giúp:

// Chặn tất cả API nếu không có Token.
// Kiểm tra Token hợp lệ trước khi tiếp tục.
// Điểm quan trọng:
//  Giải mã token bằng khóa bí mật (SECRET_KEY).
//  Lấy email của người dùng từ token mà không cần truy vấn database.
//  Nếu token hợp lệ → Cho phép tiếp tục xử lý request.
//  Nếu token không hợp lệ → Chặn request (401 Unauthorized).