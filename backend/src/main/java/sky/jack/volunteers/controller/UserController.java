package sky.jack.volunteers.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sky.jack.volunteers.entity.User;
import sky.jack.volunteers.VO.UserVO;
import sky.jack.volunteers.service.UserPermService;
import sky.jack.volunteers.service.UserService;
import sky.jack.volunteers.tool.AjaxResult;
import sky.jack.volunteers.tool.ChangeBody;
import sky.jack.volunteers.tool.Pagination;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserPermService userPermService;

    @SaCheckPermission(value = "user-add", orRole = "admin")
    @PostMapping("/insert")
    private AjaxResult insertUser(@RequestBody User user) {
        int i = userService.insertUser(user);
        if (i != 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "user-delete", orRole = "admin")
    @DeleteMapping("/{userIds}")
    private AjaxResult deleteUser(@PathVariable Long[] userIds) {
        int i = userService.deleteUser(userIds);
        if (i != 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "user-update", orRole = "admin")
    @PutMapping
    private AjaxResult updateUser(@RequestBody User user) {
        int i = userService.updateUser(user);
        if (i != 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(orRole = {"admin", "operator", "teacher"})
    @PutMapping("/update")
    private AjaxResult updateUserInfo(@RequestBody User user) {
        int i = userService.updateUserInfo(user);
        if (i != 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "user-update", orRole = "admin")
    @PutMapping("/password/{userId}")
    private AjaxResult resetPassword(@PathVariable("userId") Long userId) {
        int i = userService.resetPassword(userId);
        if (i != 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckLogin
    @PutMapping("/password")
    private AjaxResult changePassword(@RequestBody ChangeBody changeBody) {
        int i = userService.changePassword(changeBody);
        if (i != 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "user-get", orRole = "admin")
    @GetMapping("/{userId}")
    private AjaxResult getUser(@PathVariable("userId") Long userId) {
        User user = userService.getUser(userId);
        if (user != null) {
            UserVO userVO = new UserVO(user);
            return AjaxResult.success(userVO);
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckLogin
    @GetMapping("/info")
    private AjaxResult getUserInfo() {
        User user = userService.getUserInfo();
        if (user != null) {
            UserVO userVO = new UserVO(user);
            return AjaxResult.success(userVO);
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckLogin
    @GetMapping("/role")
    public AjaxResult getUserRole() {
        String userName = StpUtil.getLoginIdAsString();
        List<String> list = userPermService.getUserRoleList(userName);
        if (list != null) {
            return AjaxResult.success(list.get(0));
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "user-get", orRole = "admin")
    @PostMapping("/list")
    private AjaxResult getUserList(Pagination pagination, @RequestBody User user) {
        Page<UserVO> userVOPage = userService.getUserPage(pagination, user);
        if (userVOPage != null) {
            return AjaxResult.success(userVOPage);
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "user-add", orRole = "admin")
    @PostMapping("/import")
    private AjaxResult importUser(MultipartFile file) {
        int i = userService.importUser(file);
        if (i != 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "user-get", orRole = "admin")
    @GetMapping("/export")
    private void exportUser(HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.addHeader("content-disposition", "filename=excel.xlsx");
            List<User> userList = userService.getUserList();
            EasyExcel.write(response.getOutputStream(), User.class).sheet("sheet").doWrite(userList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SaCheckLogin
    @GetMapping("/logout")
    public void userLogout() {
        StpUtil.logout();
    }
}
