package sky.jack.volunteers.VO;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import org.springframework.format.annotation.DateTimeFormat;
import sky.jack.volunteers.entity.User;

import java.util.Date;

@ExcelIgnoreUnannotated
public class UserVO {
    private Long userId;
    private String userName;
    private String realName;
    private String userType;
    private String sex;
    private String phoneNumber;
    private String email;
    private String avatar;
    private String loginIp;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date loginDate;
    private String userStatus;

    public UserVO(User user) {
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.realName = user.getRealName();
        this.userType = user.getUserType();
        this.sex = user.getSex();
        this.phoneNumber = user.getPhoneNumber();
        this.email = user.getEmail();
        this.avatar = user.getAvatar();
        this.loginIp = user.getLoginIp();
        this.loginDate = user.getLoginDate();
        this.userStatus = user.getUserStatus();
    }

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
}
