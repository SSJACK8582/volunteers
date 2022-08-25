package sky.jack.volunteers.entity;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

@ExcelIgnoreUnannotated
public class Notice extends BaseEntity {
    @ExcelProperty(value = "通知ID")
    @TableId(type = IdType.AUTO)
    private Long noticeId;
    @ExcelProperty(value = "用户ID")
    private Long userId;
    @ExcelProperty(value = "封面")
    private String cover;
    @ExcelProperty(value = "标题")
    private String title;
    @ExcelProperty(value = "内容")
    private String content;
    @ExcelProperty(value = "通知类型")
    private String noticeType;
    @ExcelProperty(value = "通知状态")
    private String noticeStatus;

    public Long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Long noticeId) {
        this.noticeId = noticeId;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public String getNoticeStatus() {
        return noticeStatus;
    }

    public void setNoticeStatus(String noticeStatus) {
        this.noticeStatus = noticeStatus;
    }

    @Override
    public String toString() {
        return "Notice{" +
                "noticeId=" + noticeId +
                ", userId=" + userId +
                ", cover='" + cover + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", noticeType='" + noticeType + '\'' +
                ", noticeStatus='" + noticeStatus + '\'' +
                '}';
    }
}
