package sky.jack.volunteers.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import sky.jack.volunteers.tool.AjaxResult;

@ControllerAdvice
public class GlobalException {
    @ResponseBody
    @ExceptionHandler
    public AjaxResult handlerException(Exception e) throws Exception {
        System.out.println(e.getMessage());
        return AjaxResult.error(e.getMessage());
    }
}
