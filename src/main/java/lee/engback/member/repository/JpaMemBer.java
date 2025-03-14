package lee.engback.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import lee.engback.member.entity.MemBer;

public interface JpaMemBer extends JpaRepository<MemBer, Integer>
{
// Bạn có thể định nghĩa các phương thức truy vấn tùy chỉnh tại đây
Optional<MemBer> findByEmail(String email); // Thêm phương thức tìm kiếm theo email

}