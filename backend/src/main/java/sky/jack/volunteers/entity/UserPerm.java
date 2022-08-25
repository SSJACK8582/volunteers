package sky.jack.volunteers.entity;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

@ExcelIgnoreUnannotated
public class UserPerm {
    @ExcelProperty(value = "关联ID")
    @TableId(type = IdType.AUTO)
    private Long userPermId;
    @ExcelProperty(value = "权限ID")
    private Long permissionId;
    @ExcelProperty(value = "用户ID")
    private Long userId;

    public Long getUserPermId() {
        return userPermId;
    }

    public void setUserPermId(Long userPermId) {
        this.userPermId = userPermId;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserPerm{" +
                "userPermId=" + userPermId +
                ", permissionId=" + permissionId +
                ", userId=" + userId +
                '}';
    }
}
