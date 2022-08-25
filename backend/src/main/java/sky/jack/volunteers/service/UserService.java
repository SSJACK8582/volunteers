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
import sky.jack.volunteers.entity.User;
import sky.jack.volunteers.VO.UserVO;
import sky.jack.volunteers.mapper.UserMapper;
import sky.jack.volunteers.tool.ChangeBody;
import sky.jack.volunteers.tool.ExcelListener;
import sky.jack.volunteers.tool.Pagination;

import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
public class UserService {
    private static final String DEFAULT_PASSWORD = "123456";
    @Autowired
    UserMapper userMapper;

    public int insertUser(User user) {
        String passwordMd5 = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(passwordMd5);
        return userMapper.insert(user);
    }

    public int deleteUser(Long[] userIds) {
        List<Long> userIdList = Arrays.asList(userIds);
        return userMapper.deleteBatchIds(userIdList);
    }

    public int updateUser(User user) {
        return userMapper.updateById(user);
    }

    public int updateUserInfo(User user) {
        Long userId = getUserInfo().getUserId();
        user.setUserId(userId);
        return userMapper.updateById(user);
    }

    public int resetPassword(Long userId) {
        String passwordMd5 = DigestUtils.md5DigestAsHex(DEFAULT_PASSWORD.getBytes());
        User user = new User();
        user.setUserId(userId);
        user.setPassword(passwordMd5);
        return userMapper.updateById(user);
    }

    public int changePassword(ChangeBody changeBody) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        String passwordMd5 = DigestUtils.md5DigestAsHex(changeBody.getOldPassword().getBytes());
        String userName = StpUtil.getLoginIdAsString();
        queryWrapper.eq("user_name", userName);
        User user = userMapper.selectOne(queryWrapper);
        if (changeBody.getNewPassword().equals(changeBody.getRePassword()) && user != null && passwordMd5.equals(user.getPassword())) {
            user.setPassword(DigestUtils.md5DigestAsHex(changeBody.getNewPassword().getBytes()));
            return userMapper.updateById(user);
        } else {
            return 0;
        }
    }

    public User getUser(Long userId) {
        return userMapper.selectById(userId);
    }

    public User getUserInfo() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        String userName = StpUtil.getLoginIdAsString();
        queryWrapper.eq("user_name", userName);
        return userMapper.selectOne(queryWrapper);
    }

    public List<User> getUserList() {
        return userMapper.selectList(null);
    }

    public Page<UserVO> getUserPage(Pagination pagination, User user) {
        Page<User> page = new Page<>(pagination.getCurrent(), pagination.getSize());
        OrderItem orderItem = new OrderItem(pagination.getColumn(), pagination.isAsc());
        page.addOrder(orderItem);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (user != null) {
            if (user.getUserName() != null && !"".equals(user.getUserName())) {
                queryWrapper.like("user_name", user.getUserName());
            }
            if (user.getRealName() != null && !"".equals(user.getRealName())) {
                queryWrapper.like("real_name", user.getRealName());
            }
            if (user.getPhoneNumber() != null && !"".equals(user.getPhoneNumber())) {
                queryWrapper.like("phone_number", user.getPhoneNumber());
            }
            if (user.getUserType() != null && !"".equals(user.getUserType())) {
                queryWrapper.eq("user_type", user.getUserType());
            }
        }
        Page<User> userPage = userMapper.selectPage(page, queryWrapper);
        Page<UserVO> userVOPage = new Page<>();
        BeanUtils.copyProperties(userPage, userVOPage);
        List<UserVO> list = new ArrayList<>();
        List<User> records = userPage.getRecords();
        for (User u : records) {
            UserVO userVO = new UserVO(u);
            list.add(userVO);
        }
        userVOPage.setRecords(list);
        return userVOPage;
    }

    public int importUser(MultipartFile file) {
        int i = 0;
        try {
            ExcelListener listener = new ExcelListener();
            EasyExcel.read(file.getInputStream(), User.class, listener).sheet().doRead();
            List<Object> list = listener.getList();
            for (Object o : list) {
                i += insertUser((User) o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }
}
