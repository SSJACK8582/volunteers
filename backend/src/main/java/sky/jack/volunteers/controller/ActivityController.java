package sky.jack.volunteers.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sky.jack.volunteers.VO.EnrollVO;
import sky.jack.volunteers.entity.Activity;
import sky.jack.volunteers.service.ActivityService;
import sky.jack.volunteers.service.EnrollService;
import sky.jack.volunteers.tool.AjaxResult;
import sky.jack.volunteers.tool.Pagination;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/activity")
public class ActivityController {
    @Autowired
    private ActivityService activityService;
    @Autowired
    private EnrollService enrollService;

    @SaCheckPermission(value = "activity-add", orRole = {"admin", "teacher"})
    @PostMapping("/insert")
    private AjaxResult insertActivity(@RequestBody Activity activity) {
        int i = activityService.insertActivity(activity);
        if (i != 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "activity-delete", orRole = "admin")
    @DeleteMapping("/{activityIds}")
    private AjaxResult deleteActivity(@PathVariable Long[] activityIds) {
        int i = activityService.deleteActivity(activityIds);
        if (i != 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "activity-update", orRole = "admin")
    @PutMapping
    private AjaxResult updateActivity(@RequestBody Activity activity) {
        int i = activityService.updateActivity(activity);
        if (i != 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "activity-update", orRole = "admin")
    @PutMapping("/verify/{activityIds}")
    private AjaxResult verifyActivity(@PathVariable Long[] activityIds, String status) {
        int i = activityService.verifyActivity(activityIds, status);
        if (i != 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @GetMapping("/{activityId}")
    private AjaxResult getActivity(@PathVariable("activityId") Long activityId) {
        Activity activity = activityService.selectActivity(activityId);
        if (activity != null) {
            return AjaxResult.success(activity);
        } else {
            return AjaxResult.error();
        }
    }

    @PostMapping("/list")
    private AjaxResult getActivityList(Pagination pagination, @RequestBody Activity activity) {
        Page<Activity> activityPage = activityService.getActivityPage(pagination, activity);
        if (activityPage != null) {
            return AjaxResult.success(activityPage);
        } else {
            return AjaxResult.error();
        }
    }

    @PostMapping("/comment/list")
    private AjaxResult getCommentList(Pagination pagination, @RequestBody Long activityId) {
        Page<EnrollVO> enrollVOPage = enrollService.getCommentPage(pagination, activityId);
        if (enrollVOPage != null) {
            return AjaxResult.success(enrollVOPage);
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "activity-add", orRole = {"admin", "teacher"})
    @PostMapping("/import")
    private AjaxResult importActivity(MultipartFile file) {
        int i = activityService.importActivity(file);
        if (i != 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @GetMapping("/export")
    private void exportActivity(HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.addHeader("content-disposition", "filename=excel.xlsx");
            List<Activity> activityList = activityService.getActivityList();
            EasyExcel.write(response.getOutputStream(), Activity.class).sheet("sheet").doWrite(activityList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
