package lee.engback.newword;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lee.engback.member.MemBer;
import lee.engback.member.MemBerService;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
// Ở PHIÊN BẢN CHO NEWWORD, SẼ DÙNG PHƯƠNG THỨC GIAO TIẾP TRUNG GIAN QUA DTO
// NẾU MUỐN THAN KHẢO CÁCH DÙNG KO QUA DTO, THAM KHẢO BÊN MEMBERS

@RestController
@RequestMapping("/api/newwords")
public class NewWordControllerAPI {

    @Autowired
    private NewWordService newWordService; // liên kết thông qua Dependency Injection của Spring

    @Autowired
    private MemBerService memBerService;

    //   🔹 Lấy tất cả từ vựng của user từ token 
    @GetMapping
    public List<NewWordDTO> getUserNewWords(@RequestAttribute("userEmail") String email) {
        int maMember = getMaMemberFromEmail(email);
        return newWordService.findByMaMember(maMember).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
        // Lấy từ vựng theo ID, chỉ cho phép xem của chính user
    @GetMapping("/{id}")
    public ResponseEntity<NewWordDTO> getNewWordById(@PathVariable int id, @RequestAttribute("userEmail") String email) {
        int maMember = getMaMemberFromEmail(email);
        Optional<NewWord> newWord = newWordService.findById(id);
        return newWord.filter(word -> word.getMaMember() == maMember)
                .map(value -> ResponseEntity.ok(convertToDTO(value)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.FORBIDDEN).build());
    }

// 🔹 Thêm từ vựng (tự động gán maMember từ token)
@PostMapping // POST /api/newwords
public ResponseEntity<NewWordDTO> createNewWord(@RequestAttribute("userEmail") String email, @RequestBody NewWordDTO newWordDTO) {
    int maMember = getMaMemberFromEmail(email);
    NewWord newWord = convertToEntity(newWordDTO);
    newWord.setMaMember(maMember);
    return ResponseEntity.ok(convertToDTO(newWordService.saveNewWord(newWord)));
}

    // 🔹 Cập nhật từ vựng, chỉ cho phép chỉnh sửa của chính user
    @PutMapping("/{id}")
    public ResponseEntity<NewWordDTO> updateNewWord(@PathVariable int id, @RequestAttribute("userEmail") String email, @RequestBody NewWordDTO newWordDTO) {
        int maMember = getMaMemberFromEmail(email);
        Optional<NewWord> newWord = newWordService.findById(id);

        if (newWord.isPresent() && newWord.get().getMaMember() == maMember) {
            NewWord updatedNewWord = newWord.get();
            updatedNewWord.setWord(newWordDTO.getWord());
            updatedNewWord.setMeaning(newWordDTO.getMeaning());
            updatedNewWord.setDate(newWordDTO.getDate());
            return ResponseEntity.ok(convertToDTO(newWordService.saveNewWord(updatedNewWord)));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    // 🔹 Xóa từ vựng, chỉ cho phép xóa của chính user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNewWord(@PathVariable int id, @RequestAttribute("userEmail") String email) {
        int maMember = getMaMemberFromEmail(email);
        Optional<NewWord> newWord = newWordService.findById(id);

        if (newWord.isPresent() && newWord.get().getMaMember() == maMember) {
            newWordService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    // 🔹 Chuyển email -> maMember (nếu không tìm thấy, trả lỗi 403)
    private int getMaMemberFromEmail(String email) {
        return memBerService.findByEmail(email)
                .map(MemBer::getId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized"));
    }

    private NewWordDTO convertToDTO(NewWord newWord) {
        return new NewWordDTO(
                newWord.getId(),
                newWord.getWord(),
                newWord.getMeaning(),
                newWord.getDate(),
                newWord.getMaMember());
    }
    // PHẢI GÕ HẾT CẢ getMaMember() VÀO ĐÂY, NẾU KO SẼ BỊ LỖI
    // VÌ TRONG ENTITY CÓ THÊM THUỘC TÍNH MAMEMBER

    private NewWord convertToEntity(NewWordDTO newWordDTO) {
        NewWord newWord = new NewWord();
        newWord.setId(newWordDTO.getId());
        newWord.setWord(newWordDTO.getWord());
        newWord.setMeaning(newWordDTO.getMeaning());
        newWord.setDate(newWordDTO.getDate());
        newWord.setMaMember(newWordDTO.getMaMember());
        return newWord;
    }
}