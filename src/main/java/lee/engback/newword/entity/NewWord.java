package lee.engback.newword.entity;

import java.sql.Date;
import java.text.SimpleDateFormat;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lee.engback.member.entity.MemBer;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "new_word") // Chỉ định đúng tên bảng
@Getter
@Setter
public class NewWord {
    @Id // Đánh dấu id là khóa chính
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tự động tăng
    private int id; // id của từ mới

    private String word; // Từ mới
    private String meaning; // Nghĩa của từ mới
    private Date date; // Ngày thêm từ mới

    // #region khóa ngoại
    private int maMember; // Mã thành viên thêm từ mới
    @ManyToOne  // Nhiều từ mới thuộc về một thành viên
    @JoinColumn(name = "maMember", insertable = false, updatable = false)
    // Chỉ định maMember là khóa ngoại liên kết với bảng MemBer
    // không thể cập nhật hoặc chèn dữ liệu vào trường này
    private MemBer member;
    // Đây là một trường dữ liệu có kiểu MemBer, đại diện cho đối tượng MemBer liên kết với đối tượng NewWord.
    // Trường này sẽ chứa thông tin chi tiết về thành viên đã thêm từ mới.
    // #endregion

    public NewWord() // hàm khởi tạo
    {
        // đảm bảo rằng một số trường thông tin quan trọng không bị NULL
        // khi nó được dùng để cung cấp dữ liệu ra bên ngoài.
        // Ví dụ: Cầu Thủ chưa cập nhật thông tin tỉnh thành.
        // this.word = "(chưa xác định)";
        // this.date = new Date(System.currentTimeMillis());
        // Tự động thiết lập ngày hiện tại khi tạo đối tượng mới
    }

    // Hàm đổi ngày tháng sang kiểu Việt Nam
      public String getDateVi() 
    {
        return new SimpleDateFormat("dd/MM/yyyy").format(this.date);
    }

    public Boolean KhongHopLe() {
        var khl = false;

        if (this.word.length() < 2) {
            khl = true;
            System.out.println("\n Lỗi->Từ phải từ 2 kí tự trở lên: ");
        }

        if (this.word.length() > 22) {
            khl = true;
            System.out.println("\n Lỗi->Từ phải không quá 22 kí tự. ");
        }

        return khl;
    }

}