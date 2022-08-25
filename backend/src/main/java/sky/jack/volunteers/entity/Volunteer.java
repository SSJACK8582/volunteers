package sky.jack.volunteers.entity;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@ExcelIgnoreUnannotated
public class Volunteer extends BaseEntity {
    @ExcelProperty(value = "义工ID")
    @TableId(type = IdType.AUTO)
    private Long volunteerId;
    @ExcelProperty(value = "学号")
    private String studentNo;
    @ExcelProperty(value = "真实姓名")
    private String realName;
    @ExcelProperty(value = "性别")
    private String sex;
    @ExcelProperty(value = "年龄")
    private Integer age;
    @ExcelProperty(value = "专业")
    private String major;
    @ExcelProperty(value = "班级")
    private String grade;
    @ExcelProperty(value = "手机号码")
    private String phoneNumber;
    @ExcelProperty(value = "邮箱")
    private String email;
    @ExcelProperty(value = "义工时长")
    private Double workTime;
    @ExcelProperty(value = "评分")
    private Double score;
    @ExcelProperty(value = "密码")
    private String password;
    @ExcelProperty(value = "简介")
    private String introduction;
    @ExcelProperty(value = "头像")
    private String avatar;
    @ExcelProperty(value = "登录IP")
    private String loginIp;
    @ExcelProperty(value = "登录时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date loginDate;
    @ExcelProperty(value = "义工状态")
    private String volStatus;

    public Long getVolunteerId() {
        return volunteerId;
    }

    public void setVolunteerId(Long volunteerId) {
        this.volunteerId = volunteerId;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
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

    public Double getWorkTime() {
        return workTime;
    }

    public void setWorkTime(Double workTime) {
        this.workTime = workTime;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
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

    public String getVolStatus() {
        return volStatus;
    }

    public void setVolStatus(String volStatus) {
        this.volStatus = volStatus;
    }

    @Override
    public String toString() {
        return "Volunteer{" +
                "volunteerId=" + volunteerId +
                ", studentNo='" + studentNo + '\'' +
                ", realName='" + realName + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                ", major='" + major + '\'' +
                ", grade='" + grade + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", workTime=" + workTime +
                ", score=" + score +
                ", password='" + password + '\'' +
                ", introduction='" + introduction + '\'' +
                ", avatar='" + avatar + '\'' +
                ", loginIp='" + loginIp + '\'' +
                ", loginDate=" + loginDate +
                ", volStatus='" + volStatus + '\'' +
                '}';
    }
}
