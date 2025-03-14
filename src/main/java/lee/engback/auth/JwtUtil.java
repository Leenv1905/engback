package lee.engback.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
// import io.jsonwebtoken.io.Decoders;
// import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

// import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    // private static final SecretKey key = Jwts.SIG.HS256.key().build(); // Tạo khóa mạnh >= 256 bits, được tạo ngẫu nhiên sau mỗi lần khởi động
    private final SecretKey key;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }
    // Tạo khóa cố định, đọc từ application.properties

    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000)) // 1 ngày
                .signWith(key) // Dùng khóa mạnh
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
