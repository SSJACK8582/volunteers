package sky.jack.volunteers.entity;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@ExcelIgnoreUnannotated
public class Enroll extends BaseEntity {
    @ExcelProperty(value = "报名ID")
    @TableId(type = IdType.AUTO)
    private Long enrollId;
    @ExcelProperty(value = "活动ID")
    private Long activityId;
    @ExcelProperty(value = "义工ID")
    private Long volunteerId;
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

    public Long getVolunteerId() {
        return volunteerId;
    }

    public void setVolunteerId(Long volunteerId) {
        this.volunteerId = volunteerId;
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

    @Override
    public String toString() {
        return "Enroll{" +
                "enrollId=" + enrollId +
                ", activityId=" + activityId +
                ", volunteerId=" + volunteerId +
                ", comment='" + comment + '\'' +
                ", commentTime=" + commentTime +
                ", score=" + score +
                ", enrollStatus='" + enrollStatus + '\'' +
                ", joinStatus='" + joinStatus + '\'' +
                '}';
    }
}
