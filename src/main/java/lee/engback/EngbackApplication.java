package lee.engback;

// Đây là cách lấy mật khẩu mã hóa
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.CommandLineRunner;
// import lee.engback.member.service.MemBerService;
// Đây là cách lấy mật khẩu mã hóa


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
public class EngbackApplication {


    public static void main(String[] args) {
        SpringApplication.run(EngbackApplication.class, args);
    }
}

// @SpringBootApplication
// public class EngbackApplication implements CommandLineRunner {

//     @Autowired
//     private MemBerService memBerService;

//     public static void main(String[] args) {
//         SpringApplication.run(EngbackApplication.class, args);
//     }

//     @Override
//     public void run(String... args) throws Exception {
//         memBerService.testEncodePassword();
//     }
// }

// Đây là cách lấy mật khẩu mã hóa
