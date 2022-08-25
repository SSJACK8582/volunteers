package sky.jack.volunteers.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sky.jack.volunteers.entity.Enroll;
import sky.jack.volunteers.VO.EnrollVO;
import sky.jack.volunteers.service.EnrollService;
import sky.jack.volunteers.tool.AjaxResult;
import sky.jack.volunteers.tool.Pagination;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/enroll")
public class EnrollController {
    @Autowired
    private EnrollService enrollService;

    @SaCheckRole("volunteer")
    @PostMapping("/insert")
    private AjaxResult insertEnroll(@RequestBody Enroll enroll) {
        int i = enrollService.insertEnroll(enroll);
        if (i != 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "enroll-delete", orRole = {"admin"})
    @DeleteMapping("/{enrollIds}")
    private AjaxResult deleteEnroll(@PathVariable Long[] enrollIds) {
        int i = enrollService.deleteEnroll(enrollIds);
        if (i != 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "enroll-update", orRole = {"admin"})
    @PutMapping
    private AjaxResult updateEnroll(@RequestBody Enroll enroll) {
        int i = enrollService.updateEnroll(enroll);
        if (i != 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckRole("volunteer")
    @PutMapping("/cancel")
    private AjaxResult cancelEnroll(@RequestBody Long activityId) {
        int i = enrollService.cancelEnroll(activityId);
        if (i != 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "enroll-update", orRole = {"admin", "teacher"})
    @PutMapping("/verify/{enrollIds}")
    private AjaxResult verifyEnroll(@PathVariable Long[] enrollIds, String status) {
        int i = enrollService.verifyEnroll(enrollIds, status);
        if (i != 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckRole("volunteer")
    @GetMapping("/sign/{activityId}")
    private AjaxResult signEnroll(@PathVariable Long activityId) {
        int i = enrollService.signEnroll(activityId);
        if (i != 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "enroll-update", orRole = {"admin", "teacher"})
    @PutMapping("/score/{enrollIds}")
    private AjaxResult scoreEnroll(@PathVariable Long[] enrollIds, Double score) {
        int i = enrollService.scoreEnroll(enrollIds, score);
        if (i != 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckRole("volunteer")
    @PutMapping("/comment/{activityId}")
    private AjaxResult commentEnroll(@PathVariable Long activityId, @RequestBody String comment) {
        int i = enrollService.commentEnroll(activityId, comment);
        if (i != 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "enroll-get", orRole = "admin")
    @GetMapping("/{enrollId}")
    private AjaxResult getEnrollVO(@PathVariable("enrollId") Long enrollId) {
        EnrollVO enrollVO = enrollService.getEnrollVO(enrollId);
        if (enrollVO != null) {
            return AjaxResult.success(enrollVO);
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "enroll-get", orRole = {"admin", "teacher", "volunteer"})
    @PostMapping("/list")
    private AjaxResult getEnrollVOList(Pagination pagination, @RequestBody EnrollVO enrollVO) {
        Page<EnrollVO> enrollVOPage = enrollService.getEnrollVOPage(pagination, enrollVO);
        if (enrollVOPage != null) {
            return AjaxResult.success(enrollVOPage);
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "enroll-get", orRole = {"admin", "teacher", "volunteer"})
    @PostMapping("/score/list")
    private AjaxResult getEnrollVOScoreList(Pagination pagination, @RequestBody EnrollVO enrollVO) {
        Page<EnrollVO> enrollVOPage = enrollService.getEnrollVOScorePage(pagination, enrollVO);
        if (enrollVOPage != null) {
            return AjaxResult.success(enrollVOPage);
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "enroll-get", orRole = "admin")
    @GetMapping("/export")
    private void exportEnrollVO(HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.addHeader("content-disposition", "filename=excel.xlsx");
            List<EnrollVO> enrollVOList = enrollService.getEnrollVOList();
            EasyExcel.write(response.getOutputStream(), EnrollVO.class).sheet("sheet").doWrite(enrollVOList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
