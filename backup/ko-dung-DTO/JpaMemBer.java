package lee.engback.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import lee.engback.member.entity.MemBer;

public interface JpaMemBer extends JpaRepository<MemBer, Integer>
{
// Bạn có thể định nghĩa các phương thức truy vấn tùy chỉnh tại đây

}