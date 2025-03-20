package lee.engback.auth;
// FILE NÀY ĐỂ TẠO RA TOKEN JWT
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
// import io.jsonwebtoken.io.Decoders;
// import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

// import java.security.Key;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    // private static final SecretKey key = Jwts.SIG.HS256.key().build(); // Tạo khóa mạnh >= 256 bits, được tạo ngẫu nhiên sau mỗi lần khởi động
    private final SecretKey key;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes()); // Khóa cố định từ application.properties
    }
    // Tạo khóa cố định, đọc từ application.properties

    public String generateToken(String email, String roles) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .claim("roles", roles) // Thêm danh sách vai trò vào token
                .expiration(new Date(System.currentTimeMillis() + 86400000)) // 1 ngày
                // .expiration(new Date(System.currentTimeMillis() + 1000)) // 1 ngày

                .signWith(key) // Dùng khóa mạnh
                .signWith(key, Jwts.SIG.HS384) // Rõ ràng dùng HS384
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
    }

    // @SuppressWarnings("unchecked")
    public String extractRoles(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("roles", String.class); // Lấy roles dưới dạng String
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    // public String generateToken(String email, String roles) {
    //     // Để sử dụng dạng List<String> roles, cần thay đổi phương thức này
    //     throw new UnsupportedOperationException("Unimplemented method 'generateToken'");
    // }
}
