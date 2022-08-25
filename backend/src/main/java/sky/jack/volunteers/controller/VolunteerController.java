package sky.jack.volunteers.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sky.jack.volunteers.entity.Volunteer;
import sky.jack.volunteers.VO.VolunteerVO;
import sky.jack.volunteers.service.VolunteerService;
import sky.jack.volunteers.tool.AjaxResult;
import sky.jack.volunteers.tool.ChangeBody;
import sky.jack.volunteers.tool.Pagination;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/volunteer")
public class VolunteerController {
    @Autowired
    private VolunteerService volunteerService;

    @SaCheckPermission(value = "volunteer-add", orRole = "admin")
    @PostMapping("/insert")
    private AjaxResult insertVolunteer(@RequestBody Volunteer volunteer) {
        int i = volunteerService.insertVolunteer(volunteer);
        if (i != 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "volunteer-delete", orRole = "admin")
    @DeleteMapping("/{volunteerIds}")
    private AjaxResult deleteVolunteer(@PathVariable Long[] volunteerIds) {
        int i = volunteerService.deleteVolunteer(volunteerIds);
        if (i != 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "volunteer-update", orRole = "admin")
    @PutMapping
    private AjaxResult updateVolunteer(@RequestBody Volunteer volunteer) {
        int i = volunteerService.updateVolunteer(volunteer);
        if (i != 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckRole("volunteer")
    @PutMapping("/update")
    private AjaxResult updateVolunteerInfo(@RequestBody Volunteer volunteer) {
        int i = volunteerService.updateVolunteerInfo(volunteer);
        if (i != 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "volunteer-update", orRole = "admin")
    @PutMapping("/password/{volunteerId}")
    private AjaxResult resetPassword(@PathVariable("volunteerId") Long volunteerId) {
        int i = volunteerService.resetPassword(volunteerId);
        if (i != 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckLogin
    @PutMapping("/password")
    private AjaxResult changePassword(@RequestBody ChangeBody changeBody) {
        int i = volunteerService.changePassword(changeBody);
        if (i != 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "volunteer-get", orRole = "admin")
    @GetMapping("/{volunteerId}")
    private AjaxResult getVolunteer(@PathVariable("volunteerId") Long volunteerId) {
        Volunteer volunteer = volunteerService.getVolunteer(volunteerId);
        if (volunteer != null) {
            return AjaxResult.success(volunteer);
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckLogin
    @GetMapping("/info")
    private AjaxResult getVolunteerInfo() {
        Volunteer volunteer = volunteerService.getVolunteerInfo();
        if (volunteer != null) {
            VolunteerVO volunteerVO = new VolunteerVO(volunteer);
            return AjaxResult.success(volunteerVO);
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "volunteer-get", orRole = "admin")
    @PostMapping("/list")
    private AjaxResult getVolunteerList(Pagination pagination, @RequestBody Volunteer volunteer) {
        Page<VolunteerVO> volunteerVOPage = volunteerService.getVolunteerPage(pagination, volunteer);
        if (volunteerVOPage != null) {
            return AjaxResult.success(volunteerVOPage);
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "volunteer-add", orRole = "admin")
    @PostMapping("/import")
    private AjaxResult importVolunteer(MultipartFile file) {
        int i = volunteerService.importVolunteer(file);
        if (i != 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "volunteer-get", orRole = "admin")
    @GetMapping("/export")
    private void exportVolunteer(HttpServletResponse response) {
        try {
            response.setContentType("application/ms-excel");
            response.addHeader("content-disposition", "filename=excel.xlsx");
            List<Volunteer> volunteerList = volunteerService.getVolunteerList();
            EasyExcel.write(response.getOutputStream(), Volunteer.class).sheet("sheet").doWrite(volunteerList);
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
