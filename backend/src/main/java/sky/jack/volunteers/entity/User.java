package sky.jack.volunteers.entity;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@ExcelIgnoreUnannotated
public class User extends BaseEntity {
    @ExcelProperty(value = "用户ID")
    @TableId(type = IdType.AUTO)
    private Long userId;
    @ExcelProperty(value = "用户名称")
    private String userName;
    @ExcelProperty(value = "真实姓名")
    private String realName;
    @ExcelProperty(value = "用户类型")
    private String userType;
    @ExcelProperty(value = "性别")
    private String sex;
    @ExcelProperty(value = "手机号码")
    private String phoneNumber;
    @ExcelProperty(value = "邮箱")
    private String email;
    @ExcelProperty(value = "密码")
    private String password;
    @ExcelProperty(value = "头像")
    private String avatar;
    @ExcelProperty(value = "登录IP")
    private String loginIp;
    @ExcelProperty(value = "登录时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date loginDate;
    @ExcelProperty(value = "用户状态")
    private String userStatus;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", realName='" + realName + '\'' +
                ", userType='" + userType + '\'' +
                ", sex='" + sex + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", avatar='" + avatar + '\'' +
                ", loginIp='" + loginIp + '\'' +
                ", loginDate=" + loginDate +
                ", userStatus='" + userStatus + '\'' +
                '}';
    }
}
