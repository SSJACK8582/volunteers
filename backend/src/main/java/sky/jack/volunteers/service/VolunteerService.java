package sky.jack.volunteers.service;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;
import sky.jack.volunteers.entity.Enroll;
import sky.jack.volunteers.entity.Volunteer;
import sky.jack.volunteers.VO.VolunteerVO;
import sky.jack.volunteers.mapper.VolunteerMapper;
import sky.jack.volunteers.tool.ChangeBody;
import sky.jack.volunteers.tool.ExcelListener;
import sky.jack.volunteers.tool.Pagination;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
public class VolunteerService {
    private static final String DEFAULT_PASSWORD = "123456";
    @Autowired
    VolunteerMapper volunteerMapper;

    public int insertVolunteer(Volunteer volunteer) {
        String passwordMd5 = DigestUtils.md5DigestAsHex(volunteer.getPassword().getBytes());
        volunteer.setPassword(passwordMd5);
        return volunteerMapper.insert(volunteer);
    }

    public int deleteVolunteer(Long[] volunteerIds) {
        List<Long> volunteerIdList = Arrays.asList(volunteerIds);
        return volunteerMapper.deleteBatchIds(volunteerIdList);
    }

    public int updateVolunteer(Volunteer volunteer) {
        return volunteerMapper.updateById(volunteer);
    }

    public int updateVolunteerInfo(Volunteer volunteer) {
        Long volunteerId = getVolunteerInfo().getVolunteerId();
        volunteer.setVolunteerId(volunteerId);
        return volunteerMapper.updateById(volunteer);
    }

    public int resetPassword(Long volunteerId) {
        String passwordMd5 = DigestUtils.md5DigestAsHex(DEFAULT_PASSWORD.getBytes());
        Volunteer volunteer = new Volunteer();
        volunteer.setVolunteerId(volunteerId);
        volunteer.setPassword(passwordMd5);
        return volunteerMapper.updateById(volunteer);
    }

    public int changePassword(ChangeBody changeBody) {
        QueryWrapper<Volunteer> queryWrapper = new QueryWrapper<>();
        String passwordMd5 = DigestUtils.md5DigestAsHex(changeBody.getOldPassword().getBytes());
        String studentNo = StpUtil.getLoginIdAsString();
        queryWrapper.eq("student_no", studentNo);
        Volunteer volunteer = volunteerMapper.selectOne(queryWrapper);
        if (changeBody.getNewPassword().equals(changeBody.getRePassword()) && volunteer != null && passwordMd5.equals(volunteer.getPassword())) {
            volunteer.setPassword(DigestUtils.md5DigestAsHex(changeBody.getNewPassword().getBytes()));
            return volunteerMapper.updateById(volunteer);
        } else {
            return 0;
        }
    }

    public int updateVolunteerScore(Long volunteerId, List<Enroll> enrollList) {
        Volunteer volunteer = new Volunteer();
        volunteer.setVolunteerId(volunteerId);
        Double score = 0d;
        for (Enroll enroll : enrollList) {
            score += enroll.getScore();
        }
        Double average = score / enrollList.size();
        volunteer.setScore(average);
        return volunteerMapper.updateById(volunteer);
    }

    public Volunteer getVolunteer(Long volunteerId) {
        return volunteerMapper.selectById(volunteerId);
    }

    public Volunteer getVolunteerInfo() {
        QueryWrapper<Volunteer> queryWrapper = new QueryWrapper<>();
        String studentNo = StpUtil.getLoginIdAsString();
        queryWrapper.eq("student_no", studentNo);
        return volunteerMapper.selectOne(queryWrapper);
    }

    public List<Volunteer> getVolunteerList() {
        return volunteerMapper.selectList(null);
    }

    public Page<VolunteerVO> getVolunteerPage(Pagination pagination, Volunteer volunteer) {
        Page<Volunteer> page = new Page<>(pagination.getCurrent(), pagination.getSize());
        OrderItem orderItem = new OrderItem(pagination.getColumn(), pagination.isAsc());
        page.addOrder(orderItem);
        QueryWrapper<Volunteer> queryWrapper = new QueryWrapper<>();
        if (volunteer != null) {
            if (volunteer.getStudentNo() != null && !"".equals(volunteer.getStudentNo())) {
                queryWrapper.like("student_no", volunteer.getStudentNo());
            }
            if (volunteer.getRealName() != null && !"".equals(volunteer.getRealName())) {
                queryWrapper.like("real_name", volunteer.getRealName());
            }
            if (volunteer.getMajor() != null && !"".equals(volunteer.getMajor())) {
                queryWrapper.like("major", volunteer.getMajor());
            }
            if (volunteer.getPhoneNumber() != null && !"".equals(volunteer.getPhoneNumber())) {
                queryWrapper.like("phone_number", volunteer.getPhoneNumber());
            }
        }
        Page<Volunteer> volunteerPage = volunteerMapper.selectPage(page, queryWrapper);
        Page<VolunteerVO> volunteerVOPage = new Page<>();
        BeanUtils.copyProperties(volunteerPage, volunteerVOPage);
        List<VolunteerVO> list = new ArrayList<>();
        List<Volunteer> records = volunteerPage.getRecords();
        for (Volunteer v : records) {
            VolunteerVO volunteerVO = new VolunteerVO(v);
            list.add(volunteerVO);
        }
        volunteerVOPage.setRecords(list);
        return volunteerVOPage;
    }

    public int importVolunteer(MultipartFile file) {
        int i = 0;
        try {
            ExcelListener listener = new ExcelListener();
            EasyExcel.read(file.getInputStream(), Volunteer.class, listener).sheet().doRead();
            List<Object> list = listener.getList();
            for (Object o : list) {
                i += insertVolunteer((Volunteer) o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }
}
