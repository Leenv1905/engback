package lee.engback.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lee.engback.member.entity.MemBer;
import lee.engback.member.service.MemBerService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/members")
public class MemBerControllerAPI {

    @Autowired
    private MemBerService memBerService; //liên kết thông qua Dependency Injection của Spring

    @GetMapping
    public List<MemBer> getAllMembers() {
        return memBerService.findAll(); // Gọi phương thức findAll() từ MemBerService
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemBer> getMemberById(@PathVariable int id) {
        Optional<MemBer> member = memBerService.findById(id); // Gọi phương thức findById() từ MemBerService
        return member.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public MemBer createMember(@RequestBody MemBer memBer) {
        return memBerService.saveMemBer(memBer); // Gọi phương thức saveMemBer() từ MemBerService
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemBer> updateMember(@PathVariable int id, @RequestBody MemBer memBerDetails) {
        Optional<MemBer> member = memBerService.findById(id); // Gọi phương thức findById() từ MemBerService
        if (member.isPresent()) {
            MemBer memBer = member.get();
            memBer.setFullName(memBerDetails.getFullName());
            memBer.setPhoneNumber(memBerDetails.getPhoneNumber());
            memBer.setEmail(memBerDetails.getEmail());
            memBer.setPassword(memBerDetails.getPassword());
            memBer.setDateJoin(memBerDetails.getDateJoin());
            memBer.setBirthDay(memBerDetails.getBirthDay());
            return ResponseEntity.ok(memBerService.saveMemBer(memBer)); // Gọi phương thức saveMemBer() từ MemBerService
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable int id) {
        if (memBerService.existsById(id)) { // Gọi phương thức existsById() từ MemBerService
            memBerService.deleteById(id); // Gọi phương thức deleteById() từ MemBerService
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}