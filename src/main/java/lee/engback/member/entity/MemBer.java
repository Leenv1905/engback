package lee.engback.member.entity;

import java.sql.Date;
// import static java.io.IO.print;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MemBer {
    @Id // Đánh dấu id là khóa chính
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tự động tăng
    private int id; // id của thành viên
    private String fullName;    // Tên đầy đủ member
    private Integer phoneNumber;    // Số điện thoại
    private String email;    // Email - dùng để đăng ký, đăng nhập luôn
    private String password;    // Password
    private Date dateJoin;    // Ngay tham gia
    private Date birthDay;    // Ngay sinh

    public MemBer() // hàm khởi tạo
    {
        // đảm bảo rằng một số trường thông tin quan trọng không bị NULL
        // khi nó được dùng để cung cấp dữ liệu ra bên ngoài.
        // Ví dụ: Cầu Thủ chưa cập nhật thông tin tỉnh thành.
        this.fullName = "(chưa xác định)";
    }

    public Boolean KhongHopLe() {
        var khl = false;

                // if (this.fullName.length() < 2) {
                //     khl = true;
                //     print("\n Lỗi->Tên phải từ 2 kí tự trở lên: ");
                // }
        
                // if (this.fullName.length() > 22) {
                //     khl = true;
                //     print("\n Lỗi->Tên phải không quá 22 kí tự. ");
                // }

        if (this.fullName.length() < 2) {
            khl = true;
            print("\n Lỗi->Tên phải từ 2 kí tự trở lên: ");
                    }
            
                    if (this.fullName.length() > 22) {
                        khl = true;
                        print("\n Lỗi->Tên phải không quá 22 kí tự. ");
                    }
            
                    return khl;
                }

                private void print(String string) {
                    // TODO Auto-generated method stub
                    throw new UnsupportedOperationException("Unimplemented method 'print'");
                }

}
