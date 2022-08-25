package sky.jack.volunteers.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sky.jack.volunteers.VO.UserPermVO;
import sky.jack.volunteers.service.UserPermService;
import sky.jack.volunteers.tool.AjaxResult;
import sky.jack.volunteers.tool.Pagination;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/userPerm")
public class UserPermController {
    @Autowired
    private UserPermService userPermService;

    @SaCheckPermission(value = "user_perm-add", orRole = "admin")
    @PostMapping("/insert")
    private AjaxResult insertUserPerm(@RequestBody UserPermVO userPermVO) {
        int i = userPermService.insertUserPerm(userPermVO);
        if (i != 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "user_perm-delete", orRole = "admin")
    @DeleteMapping("/{userPermIds}")
    private AjaxResult deleteUserPerm(@PathVariable Long[] userPermIds) {
        int i = userPermService.deleteUserPerm(userPermIds);
        if (i != 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "user_perm-update", orRole = "admin")
    @PutMapping
    private AjaxResult updateUserPerm(@RequestBody UserPermVO userPermVO) {
        int i = userPermService.updateUserPerm(userPermVO);
        if (i != 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "user_perm-get", orRole = "admin")
    @GetMapping("/{userPermId}")
    private AjaxResult getUserPerm(@PathVariable("userPermId") Long userPermId) {
        UserPermVO userPermVO = userPermService.getUserPermVO(userPermId);
        if (userPermVO != null) {
            return AjaxResult.success(userPermVO);
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "user_perm-get", orRole = "admin")
    @PostMapping("/list")
    private AjaxResult getUserPermList(Pagination pagination, @RequestBody UserPermVO userPermVO) {
        Page<UserPermVO> userPermVOPage = userPermService.getUserPermVOPage(pagination, userPermVO);
        if (userPermVOPage != null) {
            return AjaxResult.success(userPermVOPage);
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "user_perm-add", orRole = "admin")
    @PostMapping("/import")
    private AjaxResult importUserPerm(MultipartFile file) {
        int i = userPermService.importUserPerm(file);
        if (i != 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "user_perm-get", orRole = "admin")
    @GetMapping("/export")
    private void exportUserPerm(HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.addHeader("content-disposition", "filename=excel.xlsx");
            List<UserPermVO> userPermVOList = userPermService.getUserPermVOList();
            EasyExcel.write(response.getOutputStream(), UserPermVO.class).sheet("sheet").doWrite(userPermVOList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
