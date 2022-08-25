package sky.jack.volunteers.service;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import sky.jack.volunteers.entity.Activity;
import sky.jack.volunteers.entity.Enroll;
import sky.jack.volunteers.VO.EnrollVO;
import sky.jack.volunteers.entity.Message;
import sky.jack.volunteers.entity.Volunteer;
import sky.jack.volunteers.mapper.EnrollMapper;
import sky.jack.volunteers.mapper.EnrollVOMapper;
import sky.jack.volunteers.tool.Pagination;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
public class EnrollService {
    private static final String WAIT = "0";
    private static final String JOIN = "1";
    private static final String FAIL = "1";
    private static final String PASS = "2";
    private static final String START = "3";
    private static final String CANCEL = "3";
    @Autowired
    EnrollMapper enrollMapper;
    @Autowired
    EnrollVOMapper enrollVOMapper;
    @Autowired
    UserService userService;
    @Autowired
    VolunteerService volunteerService;
    @Autowired
    ActivityService activityService;
    @Autowired
    MessageService messageService;

    public int insertEnroll(Enroll enroll) {
        Long activityId = enroll.getActivityId();
        Long volunteerId = volunteerService.getVolunteerInfo().getVolunteerId();
        Activity activity = activityService.getActivity(activityId);
        enroll.setVolunteerId(volunteerId);
        if (activity != null && START.equals(activity.getActStatus())) {
            QueryWrapper<Enroll> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("activity_id", activityId);
            queryWrapper.ne("enroll_status", CANCEL);
            List<Enroll> enrollList = enrollMapper.selectList(queryWrapper);
            for (Enroll e : enrollList) {
                if (volunteerId.equals(e.getVolunteerId())) {
                    return 0;
                }
            }
            List<Enroll> enrolls = getEnrollList(volunteerId);
            for (Enroll e : enrolls) {
                Activity enrollActivity = activityService.getActivity(e.getActivityId());
                Long ast = activity.getActStartTime().getTime();
                Long aet = activity.getActEndTime().getTime();
                Long east = enrollActivity.getActStartTime().getTime();
                Long eaet = enrollActivity.getActEndTime().getTime();
                if (ast <= eaet && ast >= east || aet <= eaet && aet >= east) {
                    return 0;
                }
            }
            if (enrollList.size() < activity.getPeopleNum()) {
                return enrollMapper.insert(enroll);
            }
        }
        return 0;
    }

    public int deleteEnroll(Long[] enrollIds) {
        List<Long> enrollIdList = Arrays.asList(enrollIds);
        return enrollMapper.deleteBatchIds(enrollIdList);
    }

    public int updateEnroll(Enroll enroll) {
        return enrollMapper.updateById(enroll);
    }

    public int cancelEnroll(Long activityId) {
        Long volunteerId = volunteerService.getVolunteerInfo().getVolunteerId();
        Enroll enroll = new Enroll();
        enroll.setEnrollStatus(CANCEL);
        UpdateWrapper<Enroll> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("activity_id", activityId);
        updateWrapper.eq("volunteer_id", volunteerId);
        updateWrapper.eq("enroll_status", WAIT);
        return enrollMapper.update(enroll, updateWrapper);
    }

    public int verifyEnroll(Long[] enrollIds, String status) {
        List<Long> enrollIdList = Arrays.asList(enrollIds);
        List<EnrollVO> enrollVOList = getEnrollVOList(enrollIds);
        Enroll enroll = new Enroll();
        enroll.setEnrollStatus(status);
        Message message = new Message();
        if (FAIL.equals(status)) {
            message.setTitle("审核失败");
            for (EnrollVO enrollVO : enrollVOList) {
                message.setContent(enrollVO.getTitle() + "报名审核失败");
                message.setVolunteerId(enrollVO.getVolunteerId());
                messageService.insertMessage(message);
            }
        } else if (PASS.equals(status)) {
            message.setTitle("审核通过");
            for (EnrollVO enrollVO : enrollVOList) {
                message.setContent(enrollVO.getTitle() + "报名审核通过");
                message.setVolunteerId(enrollVO.getVolunteerId());
                messageService.insertMessage(message);
            }
        }
        UpdateWrapper<Enroll> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("enroll_id", enrollIdList);
        return enrollMapper.update(enroll, updateWrapper);
    }

    public int signEnroll(Long activityId) {
        Long volunteerId = volunteerService.getVolunteerInfo().getVolunteerId();
        Enroll enroll = new Enroll();
        enroll.setJoinStatus(JOIN);
        UpdateWrapper<Enroll> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("activity_id", activityId);
        updateWrapper.eq("volunteer_id", volunteerId);
        updateWrapper.eq("enroll_status", PASS);
        updateWrapper.ne("join_status", JOIN);
        enrollMapper.update(enroll, updateWrapper);
        Activity activity = activityService.getActivity(activityId);
        Long time = activity.getActEndTime().getTime() - activity.getActStartTime().getTime();
        Double times = Double.parseDouble(String.valueOf(time));
        Double workTime = times / 3600000;
        Volunteer volunteer = volunteerService.getVolunteerInfo();
        Double totalTime = workTime + volunteer.getWorkTime();
        volunteer.setWorkTime(totalTime);
        return volunteerService.updateVolunteer(volunteer);
    }

    public int scoreEnroll(Long[] enrollIds, Double score) {
        List<Long> enrollIdList = Arrays.asList(enrollIds);
        Enroll enroll = new Enroll();
        enroll.setScore(score);
        UpdateWrapper<Enroll> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("enroll_id", enrollIdList);
        int i = enrollMapper.update(enroll, updateWrapper);
        QueryWrapper<Enroll> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("enroll_id", enrollIdList);
        List<Enroll> enrollList = enrollMapper.selectList(queryWrapper);
        for (Enroll e : enrollList) {
            List<Enroll> enrolls = getEnrollList(e.getVolunteerId());
            i = volunteerService.updateVolunteerScore(e.getVolunteerId(), enrolls);
        }
        return i;
    }

    public int commentEnroll(Long activityId, String comment) {
        Long volunteerId = volunteerService.getVolunteerInfo().getVolunteerId();
        Enroll enroll = new Enroll();
        Date date = new Date();
        enroll.setComment(comment);
        enroll.setCommentTime(date);
        UpdateWrapper<Enroll> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("activity_id", activityId);
        updateWrapper.eq("volunteer_id", volunteerId);
        updateWrapper.eq("enroll_status", PASS);
        updateWrapper.eq("join_status", JOIN);
        updateWrapper.isNull("comment");
        return enrollMapper.update(enroll, updateWrapper);
    }

    public EnrollVO getEnrollVO(Long enrollId) {
        return enrollVOMapper.selectById(enrollId);
    }

    public List<Enroll> getEnrollList(Long volunteerId) {
        QueryWrapper<Enroll> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("volunteer_id", volunteerId);
        queryWrapper.eq("enroll_status", PASS);
        return enrollMapper.selectList(queryWrapper);
    }

    public List<EnrollVO> getEnrollVOList(Long[] enrollIds) {
        List<Long> enrollIdList = Arrays.asList(enrollIds);
        QueryWrapper<EnrollVO> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("enroll_id", enrollIdList);
        return enrollVOMapper.selectList(queryWrapper);
    }

    public List<EnrollVO> getEnrollVOList() {
        return enrollVOMapper.selectList(null);
    }

    public Page<EnrollVO> getEnrollVOPage(Pagination pagination, EnrollVO enrollVO) {
        List<String> roleList = StpUtil.getRoleList();
        Page<EnrollVO> page = new Page<>(pagination.getCurrent(), pagination.getSize());
        OrderItem orderItem = new OrderItem(pagination.getColumn(), pagination.isAsc());
        page.addOrder(orderItem);
        QueryWrapper<EnrollVO> queryWrapper = new QueryWrapper<>();
        if (roleList.contains("teacher")) {
            Long userId = userService.getUserInfo().getUserId();
            queryWrapper.eq("user_id", userId);
        }
        if (roleList.contains("volunteer")) {
            Long volunteerId = volunteerService.getVolunteerInfo().getVolunteerId();
            queryWrapper.eq("volunteer_id", volunteerId);
        }
        if (enrollVO != null) {
            if (enrollVO.getTitle() != null && !"".equals(enrollVO.getTitle())) {
                queryWrapper.like("title", enrollVO.getTitle());
            }
            if (enrollVO.getDemand() != null && !"".equals(enrollVO.getDemand())) {
                queryWrapper.like("demand", enrollVO.getDemand());
            }
            if (enrollVO.getRealName() != null && !"".equals(enrollVO.getRealName())) {
                queryWrapper.like("real_name", enrollVO.getRealName());
            }
            if (enrollVO.getMajor() != null && !"".equals(enrollVO.getMajor())) {
                queryWrapper.like("major", enrollVO.getMajor());
            }
            if (enrollVO.getEnrollStatus() != null && !"".equals(enrollVO.getEnrollStatus())) {
                queryWrapper.eq("enroll_status", enrollVO.getEnrollStatus());
            }
            if (enrollVO.getJoinStatus() != null && !"".equals(enrollVO.getJoinStatus())) {
                queryWrapper.eq("join_status", enrollVO.getJoinStatus());
            }
        }
        return enrollVOMapper.selectPage(page, queryWrapper);
    }

    public Page<EnrollVO> getEnrollVOScorePage(Pagination pagination, EnrollVO enrollVO) {
        enrollVO.setEnrollStatus(PASS);
        enrollVO.setJoinStatus(JOIN);
        return getEnrollVOPage(pagination, enrollVO);
    }

    public Page<EnrollVO> getCommentPage(Pagination pagination, Long activityId) {
        Page<EnrollVO> page = new Page<>(pagination.getCurrent(), pagination.getSize());
        OrderItem orderItem = new OrderItem(pagination.getColumn(), pagination.isAsc());
        page.addOrder(orderItem);
        QueryWrapper<EnrollVO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("activity_id", activityId);
        queryWrapper.eq("enroll_status", PASS);
        queryWrapper.eq("join_status", JOIN);
        queryWrapper.isNotNull("comment");
        return enrollVOMapper.selectPage(page, queryWrapper);
    }
}
