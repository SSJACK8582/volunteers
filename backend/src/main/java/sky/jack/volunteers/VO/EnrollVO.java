package sky.jack.volunteers.VO;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.format.annotation.DateTimeFormat;
import sky.jack.volunteers.entity.BaseEntity;

import java.util.Date;

@TableName("enroll_vo")
@ExcelIgnoreUnannotated
public class EnrollVO extends BaseEntity {
    @ExcelProperty(value = "报名ID")
    @TableId(type = IdType.AUTO)
    private Long enrollId;
    @ExcelProperty(value = "活动ID")
    private Long activityId;
    @ExcelProperty(value = "标题")
    private String title;
    @ExcelProperty(value = "要求")
    private String demand;
    @ExcelProperty(value = "人数")
    private Integer peopleNum;
    @ExcelProperty(value = "活动开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date actStartTime;
    @ExcelProperty(value = "活动结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date actEndTime;
    @ExcelProperty(value = "义工ID")
    private Long volunteerId;
    @ExcelProperty(value = "真实姓名")
    private String realName;
    @ExcelProperty(value = "专业")
    private String major;
    @ExcelProperty(value = "简介")
    private String introduction;
    @ExcelProperty(value = "头像")
    private String avatar;
    @ExcelProperty(value = "用户ID")
    private Long userId;
    @ExcelProperty(value = "评论")
    private String comment;
    @ExcelProperty(value = "评论时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date commentTime;
    @ExcelProperty(value = "评分")
    private Double score;
    @ExcelProperty(value = "报名状态")
    private String enrollStatus;
    @ExcelProperty(value = "参加状态")
    private String joinStatus;

    public Long getEnrollId() {
        return enrollId;
    }

    public void setEnrollId(Long enrollId) {
        this.enrollId = enrollId;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDemand() {
        return demand;
    }

    public void setDemand(String demand) {
        this.demand = demand;
    }

    public Integer getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(Integer peopleNum) {
        this.peopleNum = peopleNum;
    }

    public Date getActStartTime() {
        return actStartTime;
    }

    public void setActStartTime(Date actStartTime) {
        this.actStartTime = actStartTime;
    }

    public Date getActEndTime() {
        return actEndTime;
    }

    public void setActEndTime(Date actEndTime) {
        this.actEndTime = actEndTime;
    }

    public Long getVolunteerId() {
        return volunteerId;
    }

    public void setVolunteerId(Long volunteerId) {
        this.volunteerId = volunteerId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getEnrollStatus() {
        return enrollStatus;
    }

    public void setEnrollStatus(String enrollStatus) {
        this.enrollStatus = enrollStatus;
    }

    public String getJoinStatus() {
        return joinStatus;
    }

    public void setJoinStatus(String joinStatus) {
        this.joinStatus = joinStatus;
    }
}
