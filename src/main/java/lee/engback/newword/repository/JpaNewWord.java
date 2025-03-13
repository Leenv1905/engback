package lee.engback.newword.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import lee.engback.newword.entity.NewWord;

public interface JpaNewWord extends JpaRepository<NewWord, Integer>
{
// Bạn có thể định nghĩa các phương thức truy vấn tùy chỉnh tại đây

}