package lee.engback.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lee.engback.member.entity.MemBer; // Dùng cả Entity và DTO
import lee.engback.member.model.MemBerDTO; // Dùng cả Entity và DTO
import lee.engback.member.service.MemBerService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors; // Thêm thư viện này để sử dụng phương thức map() trong hàm getAllMembers()

@RestController
@RequestMapping("/api/members")
public class MemBerControllerAPI {

    @Autowired
    private MemBerService memBerService; //liên kết thông qua Dependency Injection của Spring

    // KHÔNG DÙNG DTO
    // @GetMapping
    // public List<MemBer> getAllMembers() {  
    //     return memBerService.findAll(); // Gọi phương thức findAll() từ MemBerService
    // }

    @GetMapping
    public List<MemBerDTO> getAllMembers() {
        return memBerService.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    // KHÔNG DÙNG DTO
    // @GetMapping("/{id}")
    // public ResponseEntity<MemBer> getMemberById(@PathVariable int id) {
    //     Optional<MemBer> member = memBerService.findById(id); // Gọi phương thức findById() từ MemBerService
    //     return member.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    // }
    @GetMapping("/{id}")
    public ResponseEntity<MemBerDTO> getMemberById(@PathVariable int id) {
        Optional<MemBer> member = memBerService.findById(id);
        return member.map(value -> ResponseEntity.ok(convertToDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // KHÔNG DÙNG DTO
    // @PostMapping
    // public MemBer createMember(@RequestBody MemBer memBer) {
    //     return memBerService.saveMemBer(memBer); // Gọi phương thức saveMemBer() từ MemBerService
    // }
    @PostMapping
    public MemBerDTO createMember(@RequestBody MemBerDTO memBerDTO) {
        MemBer memBer = convertToEntity(memBerDTO);
        return convertToDTO(memBerService.saveMemBer(memBer));
    }

    // KHÔNG DÙNG DTO
    // @PutMapping("/{id}")
    // public ResponseEntity<MemBer> updateMember(@PathVariable int id, @RequestBody MemBer memBerDetails) {
    //     Optional<MemBer> member = memBerService.findById(id); // Gọi phương thức findById() từ MemBerService
    //     if (member.isPresent()) {
    //         MemBer memBer = member.get();
    //         memBer.setFullName(memBerDetails.getFullName());
    //         memBer.setPhoneNumber(memBerDetails.getPhoneNumber());
    //         memBer.setEmail(memBerDetails.getEmail());
    //         memBer.setPassword(memBerDetails.getPassword());
    //         memBer.setDateJoin(memBerDetails.getDateJoin());
    //         memBer.setBirthDay(memBerDetails.getBirthDay());
    //         return ResponseEntity.ok(memBerService.saveMemBer(memBer)); // Gọi phương thức saveMemBer() từ MemBerService
    //     } else {
    //         return ResponseEntity.notFound().build();
    //     }
    // }
    @PutMapping("/{id}")
    public ResponseEntity<MemBerDTO> updateMember(@PathVariable int id, @RequestBody MemBerDTO memBerDTO) {
        Optional<MemBer> member = memBerService.findById(id);
        if (member.isPresent()) {
            MemBer memBer = member.get();
            memBer.setFullName(memBerDTO.getFullName());
            memBer.setPhoneNumber(memBerDTO.getPhoneNumber());
            memBer.setEmail(memBerDTO.getEmail());
            memBer.setPassword(memBerDTO.getPassword());
            memBer.setDateJoin(memBerDTO.getDateJoin());
            memBer.setBirthDay(memBerDTO.getBirthDay());
            return ResponseEntity.ok(convertToDTO(memBerService.saveMemBer(memBer)));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // KHÔNG DÙNG DTO
    // @DeleteMapping("/{id}")
    // public ResponseEntity<Void> deleteMember(@PathVariable int id) {
    //     if (memBerService.existsById(id)) { // Gọi phương thức existsById() từ MemBerService
    //         memBerService.deleteById(id); // Gọi phương thức deleteById() từ MemBerService
    //         return ResponseEntity.noContent().build();
    //     } else {
    //         return ResponseEntity.notFound().build();
    //     }
    // }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable int id) {
        if (memBerService.existsById(id)) {
            memBerService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // thêm phương thức convertToDTO và convertToEntity vào lớp MemBerControllerAPI:

    private MemBerDTO convertToDTO(MemBer memBer) {
        return new MemBerDTO(
                memBer.getId(),
                memBer.getFullName(),
                memBer.getPhoneNumber(),
                memBer.getEmail(),
                memBer.getPassword(),
                memBer.getDateJoin(),
                memBer.getBirthDay()
        );
    }

    private MemBer convertToEntity(MemBerDTO memBerDTO) {
        MemBer memBer = new MemBer();
        memBer.setId(memBerDTO.getId());
        memBer.setFullName(memBerDTO.getFullName());
        memBer.setPhoneNumber(memBerDTO.getPhoneNumber());
        memBer.setEmail(memBerDTO.getEmail());
        memBer.setPassword(memBerDTO.getPassword());
        memBer.setDateJoin(memBerDTO.getDateJoin());
        memBer.setBirthDay(memBerDTO.getBirthDay());
        return memBer;
    }


}