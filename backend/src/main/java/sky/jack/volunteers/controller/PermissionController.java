package sky.jack.volunteers.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sky.jack.volunteers.entity.Permission;
import sky.jack.volunteers.service.PermissionService;
import sky.jack.volunteers.tool.AjaxResult;
import sky.jack.volunteers.tool.Pagination;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/permission")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @SaCheckPermission(value = "permission-add", orRole = "admin")
    @PostMapping("/insert")
    private AjaxResult insertPermission(@RequestBody Permission permission) {
        int i = permissionService.insertPermission(permission);
        if (i != 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "permission-delete", orRole = "admin")
    @DeleteMapping("/{permissionIds}")
    private AjaxResult deletePermission(@PathVariable Long[] permissionIds) {
        int i = permissionService.deletePermission(permissionIds);
        if (i != 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "permission-update", orRole = "admin")
    @PutMapping
    private AjaxResult updatePermission(@RequestBody Permission permission) {
        int i = permissionService.updatePermission(permission);
        if (i != 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "permission-get", orRole = "admin")
    @GetMapping("/{permissionId}")
    private AjaxResult getPermission(@PathVariable("permissionId") Long permissionId) {
        Permission permission = permissionService.getPermission(permissionId);
        if (permission != null) {
            return AjaxResult.success(permission);
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "permission-get", orRole = "admin")
    @PostMapping("/list")
    private AjaxResult getPermissionList(Pagination pagination, @RequestBody Permission permission) {
        Page<Permission> permissionPage = permissionService.getPermissionPage(pagination, permission);
        if (permissionPage != null) {
            return AjaxResult.success(permissionPage);
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "permission-add", orRole = "admin")
    @PostMapping("/import")
    private AjaxResult importPermission(MultipartFile file) {
        int i = permissionService.importPermission(file);
        if (i != 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "permission-get", orRole = "admin")
    @GetMapping("/export")
    private void exportPermission(HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.addHeader("content-disposition", "filename=excel.xlsx");
            List<Permission> permissionList = permissionService.getPermissionList();
            EasyExcel.write(response.getOutputStream(), Permission.class).sheet("sheet").doWrite(permissionList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
