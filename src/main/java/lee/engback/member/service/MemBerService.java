package lee.engback.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
// Bảo mật mật khẩu bằng spring security
import org.springframework.stereotype.Service;
import lee.engback.auth.AuthController;
import lee.engback.member.entity.MemBer;
import lee.engback.member.repository.JpaMemBer;

import java.util.List;
import java.util.Optional;

@Service
// Sử dụng constructor injection thay vì field injection:
public class MemBerService {
    private final JpaMemBer jpaMemBer;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MemBerService(JpaMemBer jpaMemBer, PasswordEncoder passwordEncoder) {
        this.jpaMemBer = jpaMemBer;
        this.passwordEncoder = passwordEncoder;
    }

    // @Autowired
    // private JpaMemBer jpaMemBer;

    // // @Autowired
    // // private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // @Autowired
    // private PasswordEncoder passwordEncoder; // không dùng final và không khởi tạo trực tiếp

    // MemBerService(AuthController authController) {
    //     this.authController = authController;
    // } // Inject PasswordEncoder
    // Bảo mật mật khẩu bằng spring security

    // public MemBer saveMemBer(MemBer memBer) {
    // KHÔNG MÃ HÓA MẬT KHẨU
    // memBer.setDateJoin(new java.sql.Date(System.currentTimeMillis()));
    // return jpaMemBer.save(memBer); }

    // MÃ HÓA MẬT KHẨU TRƯỚC KHI LƯU
    public MemBer saveMemBer(MemBer memBer) {
        memBer.setDateJoin(new java.sql.Date(System.currentTimeMillis()));
        memBer.setPassword(passwordEncoder.encode(memBer.getPassword())); // Mã hóa mật khẩu
        if (memBer.getRoles() == null || memBer.getRoles().isEmpty()) {
            memBer.setRoles("ROLE_USER");  // Gán vai trò mặc định trước khi lưu
        }
        return jpaMemBer.save(memBer);
    }

    // kiểm tra mật khẩu bằng passwordEncoder.matches(), chứ không so sánh trực tiếp
    // chuỗi
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword); // Dùng BCrypt
        // để kiểm tra mật khẩu
        // return rawPassword.equals(encodedPassword); // Không mã hóa, so sánh trực
        // tiếp, DÙng tạm khi mật khẩu trong
        // database chưa mã hóa
    }
    // BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    // return passwordEncoder.matches(rawPassword, encodedPassword);

    public Optional<MemBer> findByEmail(String email) { // Phương thức tìm kiếm theo email
        return jpaMemBer.findByEmail(email);
    }

    public List<MemBer> findAll() {
        return jpaMemBer.findAll();
    }

    public Optional<MemBer> findById(int id) {
        return jpaMemBer.findById(id);
    }

    public void deleteById(int id) {
        jpaMemBer.deleteById(id);
    }

    public boolean existsById(int id) {
        return jpaMemBer.existsById(id);
    }
 //phương thức lấy member theo vai trò
    public List<MemBer> findByRoles(String roles) {
        return jpaMemBer.findByRolesContaining(roles);
    }

    // CHỖ NÀY THÊM TẠM, XONG PHẢI XÓA ĐI
    // public void testEncodePassword() {
    //     String rawPassword = "123456";
    //     String encodedPassword = passwordEncoder.encode(rawPassword);
    //     System.out.println("Mật khẩu mã hóa: " + encodedPassword);} 
    // CHỖ NÀY THÊM TẠM, XONG PHẢI XÓA ĐI

    // ĐOẠN NÀY THÊM ĐỂ PHỤC VỤ ĐOẠN TẠM BÊN AUTHCONTROLLER
    // public String encodePassword(String password) {
    // // TODO Auto-generated method stub
    // throw new UnsupportedOperationException("Unimplemented method
    // 'encodePassword'");
    // }
    // ĐOẠN NÀY THÊM ĐỂ PHỤC VỤ ĐOẠN TAMK BÊN AUTHCONTROLLER

}