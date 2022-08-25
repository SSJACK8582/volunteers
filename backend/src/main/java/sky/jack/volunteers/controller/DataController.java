package sky.jack.volunteers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sky.jack.volunteers.service.DataService;
import sky.jack.volunteers.tool.AjaxResult;

import java.util.HashMap;

@CrossOrigin
@RestController
@RequestMapping("/data")
public class DataController {
    @Autowired
    private DataService dataService;

    @GetMapping("/activity")
    private AjaxResult getActivityData() {
        HashMap hashMap = dataService.getActivityData();
        if (hashMap != null) {
            return AjaxResult.success(hashMap);
        } else {
            return AjaxResult.error();
        }
    }

    @GetMapping("/enroll")
    private AjaxResult getEnrollData() {
        HashMap hashMap = dataService.getEnrollData();
        if (hashMap != null) {
            return AjaxResult.success(hashMap);
        } else {
            return AjaxResult.error();
        }
    }

    @GetMapping("/volunteer")
    private AjaxResult getVolunteerData() {
        HashMap hashMap = dataService.getVolunteerData();
        if (hashMap != null) {
            return AjaxResult.success(hashMap);
        } else {
            return AjaxResult.error();
        }
    }
}
