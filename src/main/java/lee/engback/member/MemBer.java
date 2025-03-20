package lee.engback.member;

// Nếu bạn dùng Hibernate để tự động tạo bảng, chỉ cần khởi động lại ứng dụng sau khi sửa entity.
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

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
    private String fullName; // Tên đầy đủ member
    private Integer phoneNumber; // Số điện thoại
    private String email; // Email - dùng để đăng ký, đăng nhập luôn
    private String password; // Password
    private Date dateJoin; // Ngay tham gia
    private Date birthDay; // Ngay sinh
    private String roles; // Vai trò người dùng
    // Nếu ứng dụng phức tạp thì nên tách roles thành một bảng riêng để quản lý

    public MemBer() // hàm khởi tạo
    {
        // đảm bảo rằng một số trường thông tin quan trọng không bị NULL
        // khi nó được dùng để cung cấp dữ liệu ra bên ngoài.
        // Ví dụ: từ mới chưa cập nhật thông tin member.
        this.fullName = "(chưa xác định)";
        this.roles = "ROLE_USER"; // Vai trò mặc định là người dùng
    }

    public Boolean KhongHopLe() {
        var khl = false;

        // if (this.fullName.length() < 2) {
        // khl = true;
        // print("\n Lỗi->Tên phải từ 2 kí tự trở lên: ");
        // }

        // if (this.fullName.length() > 22) {
        // khl = true;
        // print("\n Lỗi->Tên phải không quá 22 kí tự. ");
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
        // Khai báo phương thức print
        throw new UnsupportedOperationException("Unimplemented method 'print'");
    }

    // Phương thức để lấy danh sách vai trò dưới dạng List từ chuỗi roles
    // Phải có phương thức này để lưu đồng bộ với cơ sở dữ liệu
    public List<String> getRoleList() {
        if (this.roles == null || this.roles.isEmpty()) {
            return new ArrayList<>();
        }
        return List.of(this.roles.split(","));
    }
    // public List<String> getRoleList() {
    //     if (this.roles == null || this.roles.isEmpty()) {
    //         return List.of();
    //     }
    //     return List.of(this.roles.split(","));
    // }
    //     Thêm trường roles kiểu String để lưu vai trò (ví dụ: "ROLE_USER" hoặc "ROLE_USER,ROLE_ADMIN").
    // Thêm phương thức getRoleList() để chuyển chuỗi roles thành List<String> khi cần.

}
