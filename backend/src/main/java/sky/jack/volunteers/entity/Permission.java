package sky.jack.volunteers.entity;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

@ExcelIgnoreUnannotated
public class Permission extends BaseEntity {
    @ExcelProperty(value = "权限ID")
    @TableId(type = IdType.AUTO)
    private Long permissionId;
    @ExcelProperty(value = "上级ID")
    private Long parentId;
    @ExcelProperty(value = "权限名称")
    private String permName;
    @ExcelProperty(value = "权限标识")
    private String permCode;

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
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

    @Override
    public String toString() {
        return "Permission{" +
                "permissionId=" + permissionId +
                ", parentId=" + parentId +
                ", permName='" + permName + '\'' +
                ", permCode='" + permCode + '\'' +
                '}';
    }
}
