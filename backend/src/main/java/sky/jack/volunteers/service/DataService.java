package sky.jack.volunteers.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import sky.jack.volunteers.VO.EnrollVO;
import sky.jack.volunteers.entity.Activity;
import sky.jack.volunteers.entity.Volunteer;

import java.util.HashMap;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
public class DataService {
    private static final String WAIT = "0";
    private static final String MAN = "0";
    private static final String FAIL = "1";
    private static final String WOMAN = "1";
    private static final String JOIN = "1";
    private static final String CANCEL = "3";
    @Autowired
    ActivityService activityService;
    @Autowired
    EnrollService enrollService;
    @Autowired
    VolunteerService volunteerService;

    public HashMap getActivityData() {
        HashMap<String, Integer> hashMap = new HashMap<>();
        List<Activity> activityList = activityService.getActivityList();
        int total = activityList.size();
        int wait = 0;
        int fail = 0;
        int pass = activityList.size();
        for (Activity activity : activityList) {
            if (WAIT.equals(activity.getActStatus())) {
                wait++;
                pass--;
            }
            if (FAIL.equals(activity.getActStatus())) {
                fail++;
                pass--;
            }
        }
        hashMap.put("total", total);
        hashMap.put("wait", wait);
        hashMap.put("fail", fail);
        hashMap.put("pass", pass);
        return hashMap;
    }

    public HashMap getEnrollData() {
        HashMap<String, Integer> hashMap = new HashMap<>();
        List<EnrollVO> enrollVOList = enrollService.getEnrollVOList();
        int enroll = enrollVOList.size();
        int join = 0;
        for (EnrollVO enrollVO : enrollVOList) {
            if (CANCEL.equals(enrollVO.getEnrollStatus())) {
                enroll--;
            }
            if (JOIN.equals(enrollVO.getJoinStatus())) {
                join++;
            }
        }
        hashMap.put("enroll", enroll);
        hashMap.put("join", join);
        return hashMap;
    }

    public HashMap getVolunteerData() {
        HashMap<String, Integer> hashMap = new HashMap<>();
        List<Volunteer> volunteerList = volunteerService.getVolunteerList();
        int man = 0;
        int woman = 0;
        for (Volunteer volunteer : volunteerList) {
            if (MAN.equals(volunteer.getSex())) {
                man++;
            }
            if (WOMAN.equals(volunteer.getSex())) {
                woman++;
            }
        }
        hashMap.put("man", man);
        hashMap.put("woman", woman);
        return hashMap;
    }
}
