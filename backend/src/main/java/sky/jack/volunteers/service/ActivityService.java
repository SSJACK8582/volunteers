package sky.jack.volunteers.service;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sky.jack.volunteers.entity.Activity;
import sky.jack.volunteers.mapper.ActivityMapper;
import sky.jack.volunteers.tool.ExcelListener;
import sky.jack.volunteers.tool.Pagination;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
public class ActivityService {
    private static final String WAIT = "0";
    private static final String FAIL = "1";
    @Autowired
    ActivityMapper activityMapper;
    @Autowired
    UserService userService;

    public int insertActivity(Activity activity) {
        Long userId = userService.getUserInfo().getUserId();
        activity.setUserId(userId);
        return activityMapper.insert(activity);
    }

    public int deleteActivity(Long[] activityIds) {
        List<Long> activityIdList = Arrays.asList(activityIds);
        return activityMapper.deleteBatchIds(activityIdList);
    }

    public int updateActivity(Activity activity) {
        return activityMapper.updateById(activity);
    }

    public int verifyActivity(Long[] activityIds, String status) {
        List<Long> activityIdList = Arrays.asList(activityIds);
        Activity activity = new Activity();
        activity.setActStatus(status);
        UpdateWrapper<Activity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("activity_id", activityIdList);
        return activityMapper.update(activity, updateWrapper);
    }

    public Activity getActivity(Long activityId) {
        return activityMapper.selectById(activityId);
    }

    public Activity selectActivity(Long activityId) {
        QueryWrapper<Activity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("activity_id", activityId);
        queryWrapper.notIn("act_status", Arrays.asList(WAIT, FAIL));
        return activityMapper.selectOne(queryWrapper);
    }

    public List<Activity> getActivityList() {
        return activityMapper.selectList(null);
    }

    public Page<Activity> getActivityPage(Pagination pagination, Activity activity) {
        List<String> roleList = StpUtil.getRoleList();
        Page<Activity> page = new Page<>(pagination.getCurrent(), pagination.getSize());
        OrderItem orderItem = new OrderItem(pagination.getColumn(), pagination.isAsc());
        page.addOrder(orderItem);
        QueryWrapper<Activity> queryWrapper = new QueryWrapper<>();
        if (roleList.isEmpty() || roleList.contains("volunteer")) {
            queryWrapper.notIn("act_status", Arrays.asList(WAIT, FAIL));
        }
        if (activity != null) {
            if (activity.getTitle() != null && !"".equals(activity.getTitle())) {
                queryWrapper.like("title", activity.getTitle());
            }
            if (activity.getDemand() != null && !"".equals(activity.getDemand())) {
                queryWrapper.like("demand", activity.getDemand());
            }
            if (activity.getAddress() != null && !"".equals(activity.getAddress())) {
                queryWrapper.like("address", activity.getAddress());
            }
            if (activity.getActType() != null && !"".equals(activity.getActType())) {
                queryWrapper.eq("act_type", activity.getActType());
            }
        }
        return activityMapper.selectPage(page, queryWrapper);
    }

    public int importActivity(MultipartFile file) {
        int i = 0;
        try {
            ExcelListener listener = new ExcelListener();
            EasyExcel.read(file.getInputStream(), Activity.class, listener).sheet().doRead();
            List<Object> list = listener.getList();
            for (Object o : list) {
                i += insertActivity((Activity) o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }
}
