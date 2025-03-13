package lee.engback.member.model;

import java.sql.Date;

public class MemBerDTO {
    private int id; // id của thành viên
    private String fullName;    // Tên đầy đủ member
    private Integer phoneNumber;    // Số điện thoại
    private String email;    // Email - dùng để đăng ký, đăng nhập luôn
    private String password;    // Password
    private Date dateJoin;    // Ngay tham gia
    private Date birthDay;    // Ngay sinh


public MemBerDTO() // hàm khởi tạo
{
}
public MemBerDTO(int id, String fullName, Integer phoneNumber, String email, String password, Date dateJoin, Date birthDay) {
    this.id = id;
    this.fullName = fullName;
    this.phoneNumber = phoneNumber;
    this.email = email;
    this.password = password;
    this.dateJoin = dateJoin;
    this.birthDay = birthDay;
}

// Getters and Setters
public int getId() {
    return id;
}

public void setId(int id) {
    this.id = id;
}

public String getFullName() {
    return fullName;
}

public void setFullName(String fullName) {
    this.fullName = fullName;
}

public Integer getPhoneNumber() {
    return phoneNumber; 
}

public void setPhoneNumber(Integer phoneNumber) {
    this.phoneNumber = phoneNumber;
}

public String getEmail() {
    return email;
}

public void setEmail(String email) {
    this.email = email;
}

public String getPassword() {
    return password;
}

public void setPassword(String password) {
    this.password = password;
}

public Date getDateJoin() {
    return dateJoin;
}

public void setDateJoin(Date dateJoin) {
    this.dateJoin = dateJoin;
}

public Date getBirthDay() {
    return birthDay;
}

public void setBirthDay(Date birthDay) {
    this.birthDay = birthDay;
}


}