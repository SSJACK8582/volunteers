package sky.jack.volunteers.tool;

public class AjaxResult<T> {
    private Integer code = 0;
    private String msg;
    private T data;

    private AjaxResult() {
    }

    private AjaxResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private AjaxResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static AjaxResult success() {
        return new AjaxResult(200, "操作成功");
    }

    public static <T> AjaxResult<T> success(T data) {
        return new AjaxResult<>(200, "操作成功", data);
    }

    public static AjaxResult success(Integer code, String msg) {
        return new AjaxResult(code, msg);
    }

    public static AjaxResult error() {
        return new AjaxResult(500, "操作失败");
    }

    public static <T> AjaxResult<T> error(T data) {
        return new AjaxResult<>(500, "操作失败", data);
    }

    public static AjaxResult error(Integer code, String msg) {
        return new AjaxResult(code, msg);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "AjaxResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
