// Khi sử dụng Spring Data JPA, tệp model của bạn thường sẽ là các thực thể (entities) được ánh xạ tới các bảng trong cơ sở dữ liệu. Tuy nhiên, bạn cũng có thể sử dụng các lớp DTO (Data Transfer Objects) để chuyển dữ liệu giữa các lớp khác nhau trong ứng dụng của bạn mà không cần phải sử dụng trực tiếp các thực thể.

// Dưới đây là một ví dụ về cách tạo tệp model cho NewWord khi sử dụng Spring Data JPA. Tôi sẽ cung cấp cả thực thể NewWord và lớp DTO NewWordDTO.

package lee.engback.newword;

import java.sql.Date;

public class NewWordDTO {
    private int id;
    private String word;
    private String meaning;
    private Date date;
    private int maMember;

    // Constructors
    public NewWordDTO() {
    }

    public NewWordDTO(int id, String word, String meaning, Date date, int maMember) {
        this.id = id;
        this.word = word;
        this.meaning = meaning;
        this.date = date;
        this.maMember = maMember;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getMaMember() {
        return maMember;
    }

    public void setMaMember(int maMember) {
        this.maMember = maMember;
    }
}