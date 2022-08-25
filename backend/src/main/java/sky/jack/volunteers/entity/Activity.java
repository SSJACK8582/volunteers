package sky.jack.volunteers.entity;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@ExcelIgnoreUnannotated
public class Activity extends BaseEntity {
    @ExcelProperty(value = "活动ID")
    @TableId(type = IdType.AUTO)
    private Long activityId;
    @ExcelProperty(value = "用户ID")
    private Long userId;
    @ExcelProperty(value = "封面")
    private String cover;
    @ExcelProperty(value = "标题")
    private String title;
    @ExcelProperty(value = "要求")
    private String demand;
    @ExcelProperty(value = "内容")
    private String content;
    @ExcelProperty(value = "地址")
    private String address;
    @ExcelProperty(value = "人数")
    private Integer peopleNum;
    @ExcelProperty(value = "活动类型")
    private String actType;
    @ExcelProperty(value = "报名开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date enrollStartTime;
    @ExcelProperty(value = "报名结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date enrollEndTime;
    @ExcelProperty(value = "活动开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date actStartTime;
    @ExcelProperty(value = "活动结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date actEndTime;
    @ExcelProperty(value = "活动状态")
    private String actStatus;

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(Integer peopleNum) {
        this.peopleNum = peopleNum;
    }

    public String getActType() {
        return actType;
    }

    public void setActType(String actType) {
        this.actType = actType;
    }

    public Date getEnrollStartTime() {
        return enrollStartTime;
    }

    public void setEnrollStartTime(Date enrollStartTime) {
        this.enrollStartTime = enrollStartTime;
    }

    public Date getEnrollEndTime() {
        return enrollEndTime;
    }

    public void setEnrollEndTime(Date enrollEndTime) {
        this.enrollEndTime = enrollEndTime;
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

    public String getActStatus() {
        return actStatus;
    }

    public void setActStatus(String actStatus) {
        this.actStatus = actStatus;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "activityId=" + activityId +
                ", userId=" + userId +
                ", cover='" + cover + '\'' +
                ", title='" + title + '\'' +
                ", demand='" + demand + '\'' +
                ", content='" + content + '\'' +
                ", address='" + address + '\'' +
                ", peopleNum=" + peopleNum +
                ", actType='" + actType + '\'' +
                ", enrollStartTime=" + enrollStartTime +
                ", enrollEndTime=" + enrollEndTime +
                ", actStartTime=" + actStartTime +
                ", actEndTime=" + actEndTime +
                ", actStatus='" + actStatus + '\'' +
                '}';
    }
}
