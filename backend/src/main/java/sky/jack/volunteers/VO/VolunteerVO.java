package sky.jack.volunteers.VO;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import org.springframework.format.annotation.DateTimeFormat;
import sky.jack.volunteers.entity.Volunteer;

import java.util.Date;

@ExcelIgnoreUnannotated
public class VolunteerVO {
    private Long volunteerId;
    private String studentNo;
    private String realName;
    private String sex;
    private Integer age;
    private String major;
    private String grade;
    private String phoneNumber;
    private String email;
    private Double workTime;
    private Double score;
    private String introduction;
    private String avatar;
    private String loginIp;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date loginDate;
    private String volStatus;

    public VolunteerVO(Volunteer volunteer) {
        this.volunteerId = volunteer.getVolunteerId();
        this.studentNo = volunteer.getStudentNo();
        this.realName = volunteer.getRealName();
        this.sex = volunteer.getSex();
        this.age = volunteer.getAge();
        this.major = volunteer.getMajor();
        this.grade = volunteer.getGrade();
        this.phoneNumber = volunteer.getPhoneNumber();
        this.email = volunteer.getEmail();
        this.workTime = volunteer.getWorkTime();
        this.score = volunteer.getScore();
        this.introduction = volunteer.getIntroduction();
        this.avatar = volunteer.getAvatar();
        this.loginIp = volunteer.getLoginIp();
        this.loginDate = volunteer.getLoginDate();
        this.volStatus = volunteer.getVolStatus();
    }

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
}
