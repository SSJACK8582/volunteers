package sky.jack.volunteers.VO;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("user_perm_vo")
@ExcelIgnoreUnannotated
public class UserPermVO {
    @ExcelProperty(value = "关联ID")
    @TableId(type = IdType.AUTO)
    private Long userPermId;
    @ExcelProperty(value = "用户ID")
    private Long userId;
    @ExcelProperty(value = "权限ID")
    private Long permissionId;
    @ExcelProperty(value = "用户名称")
    private String userName;
    @ExcelProperty(value = "用户类型")
    private String userType;
    @ExcelProperty(value = "权限名称")
    private String permName;
    @ExcelProperty(value = "权限标识")
    private String permCode;

    public Long getUserPermId() {
        return userPermId;
    }

    public void setUserPermId(Long userPermId) {
        this.userPermId = userPermId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getPermName() {
        return permName;
    }

    public void setPermName(String permName) {
        this.permName = permName;
    }

    public String getPermCode() {
        return permCode;
    }

    public void setPermCode(String permCode) {
        this.permCode = permCode;
    }
}
