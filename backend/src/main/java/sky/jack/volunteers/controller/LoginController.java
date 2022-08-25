package sky.jack.volunteers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sky.jack.volunteers.service.LoginService;
import sky.jack.volunteers.tool.AjaxResult;
import sky.jack.volunteers.tool.LoginBody;

@CrossOrigin
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping("/user")
    public AjaxResult userLogin(@RequestBody LoginBody loginBody) {
        String token = loginService.userLogin(loginBody.getUserName(), loginBody.getPassword());
        if (token != null) {
            return AjaxResult.success(token);
        } else {
            return AjaxResult.error();
        }
    }

    @PostMapping("/volunteer")
    public AjaxResult volunteerLogin(@RequestBody LoginBody loginBody) {
        String token = loginService.volunteerLogin(loginBody.getStudentNo(), loginBody.getPassword());
        if (token != null) {
            return AjaxResult.success(token);
        } else {
            return AjaxResult.error();
        }
    }
}
