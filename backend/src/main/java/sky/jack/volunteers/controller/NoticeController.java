package sky.jack.volunteers.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sky.jack.volunteers.entity.Notice;
import sky.jack.volunteers.service.NoticeService;
import sky.jack.volunteers.tool.AjaxResult;
import sky.jack.volunteers.tool.Pagination;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/notice")
public class NoticeController {
    @Autowired
    private NoticeService noticeService;

    @SaCheckPermission(value = "notice-add", orRole = {"admin", "teacher"})
    @PostMapping("/insert")
    private AjaxResult insertNotice(@RequestBody Notice notice) {
        int i = noticeService.insertNotice(notice);
        if (i != 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "notice-delete", orRole = "admin")
    @DeleteMapping("/{noticeIds}")
    private AjaxResult deleteNotice(@PathVariable Long[] noticeIds) {
        int i = noticeService.deleteNotice(noticeIds);
        if (i != 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "notice-update", orRole = "admin")
    @PutMapping
    private AjaxResult updateNotice(@RequestBody Notice notice) {
        int i = noticeService.updateNotice(notice);
        if (i != 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "notice-update", orRole = "admin")
    @PutMapping("/verify/{noticeIds}")
    private AjaxResult verifyNotice(@PathVariable Long[] noticeIds, String status) {
        int i = noticeService.verifyNotice(noticeIds, status);
        if (i != 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @GetMapping("/{noticeId}")
    private AjaxResult getNotice(@PathVariable("noticeId") Long noticeId) {
        Notice notice = noticeService.getNotice(noticeId);
        if (notice != null) {
            return AjaxResult.success(notice);
        } else {
            return AjaxResult.error();
        }
    }

    @GetMapping("/banner")
    private AjaxResult getBannerList() {
        List<Notice> bannerList = noticeService.getBannerList();
        if (bannerList != null) {
            return AjaxResult.success(bannerList);
        } else {
            return AjaxResult.error();
        }
    }

    @PostMapping("/list")
    private AjaxResult getNoticeList(Pagination pagination, @RequestBody Notice notice) {
        Page<Notice> noticePage = noticeService.getNoticePage(pagination, notice);
        if (noticePage != null) {
            return AjaxResult.success(noticePage);
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "notice-add", orRole = {"admin", "teacher"})
    @PostMapping("/import")
    private AjaxResult importNotice(MultipartFile file) {
        int i = noticeService.importNotice(file);
        if (i != 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @GetMapping("/export")
    private void exportNotice(HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.addHeader("content-disposition", "filename=excel.xlsx");
            List<Notice> noticeList = noticeService.getNoticeList();
            EasyExcel.write(response.getOutputStream(), Notice.class).sheet("sheet").doWrite(noticeList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
