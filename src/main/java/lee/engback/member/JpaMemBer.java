package lee.engback.member;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMemBer extends JpaRepository<MemBer, Integer>
{
// Bạn có thể định nghĩa các phương thức truy vấn tùy chỉnh tại đây
Optional<MemBer> findByEmail(String email); // Thêm phương thức tìm kiếm theo email
List<MemBer> findByRolesContaining(String roles);  // Tìm member theo vai trò
}