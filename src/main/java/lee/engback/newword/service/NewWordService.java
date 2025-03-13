package lee.engback.newword.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lee.engback.newword.entity.NewWord;
import lee.engback.newword.repository.JpaNewWord;

import java.util.List;
import java.util.Optional;

@Service
public class NewWordService {

    @Autowired
    private JpaNewWord jpaNewWord;

    public NewWord saveNewWord(NewWord newWord) {
        newWord.setDate(new java.sql.Date(System.currentTimeMillis()));
        return jpaNewWord.save(newWord);
    }
    // Hiện tại ở đây ngày được cập nhật tự động khi thêm mới từ mới
    // Nên không sửa được ngày

    public List<NewWord> findAll() {
        return jpaNewWord.findAll();
    }

    public Optional<NewWord> findById(int id) {
        return jpaNewWord.findById(id);
    }

    public void deleteById(int id) {
        jpaNewWord.deleteById(id);
    }

    public boolean existsById(int id) {
        return jpaNewWord.existsById(id);
    }

    // Các phương thức khác như tìm kiếm, cập nhật, xóa có thể được thêm vào đây
}