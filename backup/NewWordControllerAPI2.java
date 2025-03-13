package lee.engback.newword.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import lee.engback.EngbackApplication;
import lee.engback.newword.entity.NewWord;
import lee.engback.newword.model.NewWordDTO;
import lee.engback.newword.service.NewWordService;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/newword")
public class NewWordControllerAPI2 {

    @Autowired
    private NewWordService newWordService; // liên kết thông qua Dependency Injection của Spring

    private NewWordDTO convertToDTO (NewWord newWord) {
        return new NewWordDTO(
                newWord.getId(),
                newWord.getWord(),
                newWord.getMeaning(),
                newWord.getDate()
                );
    }
    // PHẢI GÕ HẾT CẢ getMaMember() VÀO ĐÂY, NẾU KO SẼ BỊ LỖI
    // VÌ TRONG ENTITY CÓ THÊM THUỘC TÍNH MAMEMBER

    private NewWord convertToEntity(NewWordDTO newWordDTO) {
    }

    // Ở PHIÊN BẢN CHO NEWWORD, SẼ DÙNG PHƯƠNG THỨC GIAO TIẾP TRUNG GIAN QUA DTO
    // NẾU MUỐN THAN KHẢO CÁCH DÙNG KO QUA DTO, THAM KHẢO BÊN MEMBERS
    @GetMapping
    public List<NewWordDTO> getAllNewWords() {
        return newWordService.findAll().stream(); // Gọi phương thức findAll() từ NewWordService
                .map(this::convertToDTO)
                .collect(Collecttors.toList());
    }

}