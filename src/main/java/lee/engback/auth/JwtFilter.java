package lee.engback.auth;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.lang.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain; // Đổi từ javax.servlet -> jakarta.servlet do phiên bản spring boot đã nâng cấp
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        logger.debug("Processing request: {}", request.getRequestURI());

        String path = request.getRequestURI();
        String method = request.getMethod();

        // Bỏ qua yêu cầu OPTIONS
        if ("OPTIONS".equalsIgnoreCase(method)) {
            logger.debug("Bypassing JwtFilter for OPTIONS request: {}", path);
            filterChain.doFilter(request, response);
            return;
        }
        // Bỏ qua yêu cầu OPTIONS


        // if (path.equals("/api/auth/login") || 
        //     ("POST".equalsIgnoreCase(method) && path.equals("/api/members"))) {
        //     logger.debug("Bypassing JwtFilter for endpoint: {}", path);
        //     filterChain.doFilter(request, response);
        //     return;
        // }

        // ✅ Nếu là /api/auth/login và đăng ký tài khoản (POST /api/members) → Bỏ qua
        // kiểm tra JWT
        if (request.getRequestURI().equals("/api/auth/login")
                || ("POST".equalsIgnoreCase(method) && path.equals("/api/members"))) {
            logger.debug("Bypassing JwtFilter for login endpoint");
            filterChain.doFilter(request, response);
            return;
        }
        // ✅ Nếu là /api/auth/login và đăng ký tài khoản (POST /api/members) → Bỏ qua
        // kiểm tra JWT

        // ✅ Nếu là đăng ký tài khoản (POST /api/members) → Bỏ qua kiểm tra JWT
        // ĐẶT Ở DƯỚI NÀY THÌ LẠI SAI
        // if ("POST".equalsIgnoreCase(method) && path.equals("/api/members")) {
        // filterChain.doFilter(request, response);
        // return;
        // }
        // ✅ Nếu là đăng ký tài khoản (POST /api/members) → Bỏ qua kiểm tra JWT

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        logger.debug("Authorization header: {}", token);

        if (token == null || !token.startsWith("Bearer ")) {
            logger.warn("Missing or invalid Authorization header");
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Bạn cần đăng nhập!");
            return;
        }

        token = token.substring(7);  // Loại bỏ tiền tố "Bearer "
        logger.debug("Extracted token: {}", token);

        try {
            String email = jwtUtil.extractEmail(token);
            String roles = jwtUtil.extractRoles(token); // Lấy roles dưới dạng String
            List<String> roleList = (roles == null || roles.isEmpty())
                    ? Collections.emptyList()
                    : Arrays.asList(roles.split(",")); // Chuyển String thành List, phân tách bằng dấu phẩy
            logger.debug("Extracted email: {}, roles: {}", email, roleList);

            // Lấy danh sách vai trò từ token bằng jwtUtil.extractRoles(token)
            // Chuyển roles thành GrantedAuthority
            List<SimpleGrantedAuthority> authorities = roleList.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            logger.debug("Extracted roles: {}", authorities);

            // Tạo Authentication và lưu vào SecurityContextHolder
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, null,
                    authorities);
            // Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authToken);
            logger.debug("Set authentication for email: {}", email);

            // Thêm email vào request attribute để controller sử dụng, tạo điều kiện lấy email từ token mà không cần truy vấn database
            request.setAttribute("userEmail", email);

            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            logger.error("Invalid token: {}", e.getMessage());
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Token không hợp lệ!");
            // redirectToLogin(response);  // điều hướng
        }
    }

    // Ở đây Filter đang bắt tất cả truy vấn đến các api nếu chưa đăng nhập thì ko cho làm gì cả
    // ví dụ ở trang Learn trên frontend có truy cập đến /api/newwords
    // Tuy nhiên kể cả chưa bấm save vẫn bị bắt sống
    // private void redirectToLogin(HttpServletResponse response) throws IOException {
    //     response.setStatus(HttpStatus.FOUND.value()); // HTTP 302 Found
    //     response.setHeader("Location", "http://localhost:3000/user/login"); // URL trang đăng nhập
    // }
}

// Filter này giúp:

// Chặn tất cả API nếu không có Token.
// Kiểm tra Token hợp lệ trước khi tiếp tục.
// Điểm quan trọng:
// Giải mã token bằng khóa bí mật (SECRET_KEY).
// Lấy email của người dùng từ token mà không cần truy vấn database.
// Nếu token hợp lệ → Cho phép tiếp tục xử lý request.
// Nếu token không hợp lệ → Chặn request (401 Unauthorized).