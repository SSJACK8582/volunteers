package sky.jack.volunteers.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sky.jack.volunteers.entity.Dict;
import sky.jack.volunteers.service.DictService;
import sky.jack.volunteers.tool.AjaxResult;
import sky.jack.volunteers.tool.Pagination;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/dict")
public class DictController {
    @Autowired
    private DictService dictService;

    @SaCheckPermission(value = "dict-add", orRole = "admin")
    @PostMapping("/insert")
    private AjaxResult insertDict(@RequestBody Dict dict) {
        int i = dictService.insertDict(dict);
        if (i != 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "dict-delete", orRole = "admin")
    @DeleteMapping("/{dictIds}")
    private AjaxResult deleteDict(@PathVariable Long[] dictIds) {
        int i = dictService.deleteDict(dictIds);
        if (i != 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "dict-update", orRole = "admin")
    @PutMapping
    private AjaxResult updateDict(@RequestBody Dict dict) {
        int i = dictService.updateDict(dict);
        if (i != 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @GetMapping("/{dictId}")
    private AjaxResult getDict(@PathVariable("dictId") Long dictId) {
        Dict dict = dictService.getDict(dictId);
        if (dict != null) {
            return AjaxResult.success(dict);
        } else {
            return AjaxResult.error();
        }
    }

    @GetMapping
    private AjaxResult getDictByType(String dictType) {
        HashMap hashMap = dictService.getDictByType(dictType);
        if (hashMap != null) {
            return AjaxResult.success(hashMap);
        } else {
            return AjaxResult.error();
        }
    }

    @PostMapping
    private AjaxResult getDictByTypeList(@RequestBody List<String> dictTypeList) {
        HashMap hashMap = dictService.getDictByTypeList(dictTypeList);
        if (hashMap != null) {
            return AjaxResult.success(hashMap);
        } else {
            return AjaxResult.error();
        }
    }

    @PostMapping("/list")
    private AjaxResult getDictList(Pagination pagination, @RequestBody Dict dict) {
        Page<Dict> dictPage = dictService.getDictPage(pagination, dict);
        if (dictPage != null) {
            return AjaxResult.success(dictPage);
        } else {
            return AjaxResult.error();
        }
    }

    @SaCheckPermission(value = "dict-add", orRole = "admin")
    @PostMapping("/import")
    private AjaxResult importDict(MultipartFile file) {
        int i = dictService.importDict(file);
        if (i != 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @GetMapping("/export")
    private void exportDict(HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.addHeader("content-disposition", "filename=excel.xlsx");
            List<Dict> dictList = dictService.getDictList();
            EasyExcel.write(response.getOutputStream(), Dict.class).sheet("sheet").doWrite(dictList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
