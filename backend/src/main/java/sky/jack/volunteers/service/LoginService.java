package sky.jack.volunteers.service;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import sky.jack.volunteers.entity.User;
import sky.jack.volunteers.entity.Volunteer;
import sky.jack.volunteers.mapper.UserMapper;
import sky.jack.volunteers.mapper.VolunteerMapper;
import sky.jack.volunteers.tool.IpUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class LoginService {
    private static final String NORMAL = "0";
    @Autowired
    UserMapper userMapper;
    @Autowired
    VolunteerMapper volunteerMapper;

    public String userLogin(String username, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        String passwordMd5 = DigestUtils.md5DigestAsHex(password.getBytes());
        queryWrapper.eq("user_name", username);
        queryWrapper.eq("user_status", NORMAL);
        User user = userMapper.selectOne(queryWrapper);
        if (user != null && passwordMd5.equals(user.getPassword())) {
            StpUtil.login(user.getUserName());
            SaTokenInfo saTokenInfo = StpUtil.getTokenInfo();
            String token = saTokenInfo.getTokenValue();
            int i = recordUserLogin(user.getUserId());
            return token;
        } else {
            return null;
        }
    }

    public String volunteerLogin(String studentno, String password) {
        QueryWrapper<Volunteer> queryWrapper = new QueryWrapper<>();
        String passwordMd5 = DigestUtils.md5DigestAsHex(password.getBytes());
        queryWrapper.eq("student_no", studentno);
        queryWrapper.eq("vol_status", NORMAL);
        Volunteer volunteer = volunteerMapper.selectOne(queryWrapper);
        if (volunteer != null && passwordMd5.equals(volunteer.getPassword())) {
            StpUtil.login(volunteer.getStudentNo());
            SaTokenInfo saTokenInfo = StpUtil.getTokenInfo();
            String token = saTokenInfo.getTokenValue();
            int i = recordVolunteerLogin(volunteer.getVolunteerId());
            return token;
        } else {
            return null;
        }
    }

    private int recordUserLogin(Long userId) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String ip = IpUtil.getIpAddress(request);
        User user = new User();
        Date nowDate = new Date();
        user.setUserId(userId);
        user.setLoginIp(ip);
        user.setLoginDate(nowDate);
        return userMapper.updateById(user);
    }

    private int recordVolunteerLogin(Long volunteerId) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String ip = IpUtil.getIpAddress(request);
        Volunteer volunteer = new Volunteer();
        Date nowDate = new Date();
        volunteer.setVolunteerId(volunteerId);
        volunteer.setLoginIp(ip);
        volunteer.setLoginDate(nowDate);
        return volunteerMapper.updateById(volunteer);
    }
}
