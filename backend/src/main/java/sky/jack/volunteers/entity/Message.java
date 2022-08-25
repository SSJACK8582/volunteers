package sky.jack.volunteers.entity;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

@ExcelIgnoreUnannotated
public class Message extends BaseEntity {
    @ExcelProperty(value = "消息ID")
    @TableId(type = IdType.AUTO)
    private Long messageId;
    @ExcelProperty(value = "义工ID")
    private Long volunteerId;
    @ExcelProperty(value = "标题")
    private String title;
    @ExcelProperty(value = "内容")
    private String content;

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getVolunteerId() {
        return volunteerId;
    }

    public void setVolunteerId(Long volunteerId) {
        this.volunteerId = volunteerId;
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

    @Override
    public String toString() {
        return "Message{" +
                "messageId=" + messageId +
                ", volunteerId=" + volunteerId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
