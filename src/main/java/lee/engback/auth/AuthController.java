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

    // CHỖ NÀY THÊM TẠM, XONG PHẢI XÓA ĐI
    // @GetMapping("/encode/{password}")
    // public String encodePassword(@PathVariable String password) {
    //     return memBerService.encodePassword(password);
    // }
    // CHỖ NÀY THÊM TẠM, XONG PHẢI XÓA ĐI

    @Autowired
    private MemBerService memBerService;

    @Autowired
    private JwtUtil jwtUtil;

    // @PostMapping("/login")
    // public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
    // Optional<MemBer> user = memBerService.findByEmail(loginRequest.getEmail());

    // if (user.isPresent() &&
    // memBerService.checkPassword(loginRequest.getPassword(),
    // user.get().getPassword())) {
    // String token = jwtUtil.generateToken(user.get().getEmail());
    // return ResponseEntity.ok(new AuthResponse(token));
    // }

    // return ResponseEntity.status(401).body("Email hoặc mật khẩu không đúng!");
    // }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("🔍 Đang xử lý đăng nhập...");
        System.out.println("📩 Email đăng nhập: " + loginRequest.getEmail());
        System.out.println("🔑 Mật khẩu nhập vào: " + loginRequest.getPassword());

        Optional<MemBer> user = memBerService.findByEmail(loginRequest.getEmail());

        if (user.isPresent()) {
            System.out.println("✅ User tồn tại: " + user.get().getEmail());
            System.out.println("🔐 Mật khẩu trong database: " + user.get().getPassword());

            if (memBerService.checkPassword(loginRequest.getPassword(), user.get().getPassword())) {
                String token = jwtUtil.generateToken(user.get().getEmail());
                System.out.println("🔑 Token đã tạo: " + token);
                return ResponseEntity.ok(new AuthResponse(token));
            } else {
                System.out.println("❌ Mật khẩu không khớp!");
            }
        } else {
            System.out.println("❌ Không tìm thấy user với email: " + loginRequest.getEmail());
        }

        return ResponseEntity.status(401).body("Email hoặc mật khẩu không đúng!");
    }

}

// Không cần định nghĩa LoginRequest ở đây nữa!
// Đã tách ra file riêng: src/main/java/lee/engback/auth/LoginRequest.java

class AuthResponse {
    private String token;

    public AuthResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}

// Cách hoạt động:

// Người dùng gửi email & password.
// Nếu đúng → Hệ thống trả về JWT Token.
// Nếu sai → Báo lỗi 401 Unauthorized.
