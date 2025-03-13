package lee.engback.member.service;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.crypto.password.PasswordEncoder;
// Bảo mật mật khẩu bằng spring security
import org.springframework.stereotype.Service;
import lee.engback.member.entity.MemBer;
import lee.engback.member.repository.JpaMemBer;

import java.util.List;
import java.util.Optional;

@Service
public class MemBerService {

    @Autowired
    private JpaMemBer jpaMemBer;

    public MemBer saveMemBer(MemBer memBer) {
        memBer.setDateJoin(new java.sql.Date(System.currentTimeMillis()));
        return jpaMemBer.save(memBer);
    }
    // public MemBer saveMemBer(MemBer memBer) {
    //     memBer.setDateJoin(new java.sql.Date(System.currentTimeMillis()));
    //     memBer.setPassword(passwordEncoder.encode(memBer.getPassword())); // Mã hóa mật khẩu
    //     return jpaMemBer.save(memBer);
    // }

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
}