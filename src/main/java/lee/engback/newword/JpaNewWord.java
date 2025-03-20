package lee.engback.newword;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaNewWord extends JpaRepository<NewWord, Integer>
{
// Bạn có thể định nghĩa các phương thức truy vấn tùy chỉnh tại đây
List<NewWord> findByMaMember(int maMember);
}