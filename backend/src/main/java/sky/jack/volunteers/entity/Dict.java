package sky.jack.volunteers.entity;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

@ExcelIgnoreUnannotated
public class Dict extends BaseEntity {
    @ExcelProperty(value = "字典ID")
    @TableId(type = IdType.AUTO)
    private Long dictId;
    @ExcelProperty(value = "上级ID")
    private Long parentId;
    @ExcelProperty(value = "字典类型")
    private String dictType;
    @ExcelProperty(value = "字典名称")
    private String dictName;
    @ExcelProperty(value = "关键字")
    private String dictKey;
    @ExcelProperty(value = "关键值")
    private String dictValue;
    @ExcelProperty(value = "样式")
    private String cssClass;

    public Long getDictId() {
        return dictId;
    }

    public void setDictId(Long dictId) {
        this.dictId = dictId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public String getDictKey() {
        return dictKey;
    }

    public void setDictKey(String dictKey) {
        this.dictKey = dictKey;
    }

    public String getDictValue() {
        return dictValue;
    }

    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }

    public String getCssClass() {
        return cssClass;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    @Override
    public String toString() {
        return "Dict{" +
                "dictId=" + dictId +
                ", parentId=" + parentId +
                ", dictType='" + dictType + '\'' +
                ", dictName='" + dictName + '\'' +
                ", dictKey='" + dictKey + '\'' +
                ", dictValue='" + dictValue + '\'' +
                ", cssClass='" + cssClass + '\'' +
                '}';
    }
}
