package lee.engback.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lee.engback.member.entity.MemBer;
import lee.engback.member.service.MemBerService;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private MemBerService memBerService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Optional<MemBer> user = memBerService.findByEmail(loginRequest.getEmail());

        if (user.isPresent() && memBerService.checkPassword(loginRequest.getPassword(), user.get().getPassword())) {
            String token = jwtUtil.generateToken(user.get().getEmail());
            return ResponseEntity.ok(new AuthResponse(token));
        }

        return ResponseEntity.status(401).body("Email hoặc mật khẩu không đúng!");
    }
}

// Không cần định nghĩa LoginRequest ở đây nữa!
// Đã tách ra file riêng: src/main/java/lee/engback/auth/LoginRequest.java

class AuthResponse {
    private String token;
    public AuthResponse(String token) { this.token = token; }
    public String getToken() { return token; }
}


// Cách hoạt động:

// Người dùng gửi email & password.
// Nếu đúng → Hệ thống trả về JWT Token.
// Nếu sai → Báo lỗi 401 Unauthorized.
