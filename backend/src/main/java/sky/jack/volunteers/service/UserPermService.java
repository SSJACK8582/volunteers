package sky.jack.volunteers.service;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sky.jack.volunteers.entity.User;
import sky.jack.volunteers.entity.UserPerm;
import sky.jack.volunteers.VO.UserPermVO;
import sky.jack.volunteers.mapper.UserMapper;
import sky.jack.volunteers.mapper.UserPermMapper;
import sky.jack.volunteers.mapper.UserPermVOMapper;
import sky.jack.volunteers.tool.ExcelListener;
import sky.jack.volunteers.tool.Pagination;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
public class UserPermService {
    private static final String ADMIN = "0";
    private static final String OPERATOR = "1";
    private static final String TEACHER = "2";
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserPermMapper userPermMapper;
    @Autowired
    UserPermVOMapper userPermVOMapper;

    public int insertUserPerm(UserPerm userPerm) {
        return userPermMapper.insert(userPerm);
    }

    public int insertUserPerm(UserPermVO userPermVO) {
        return userPermMapper.insertByNameCode(userPermVO.getUserName(), userPermVO.getPermCode());
    }

    public int deleteUserPerm(Long[] userPermIds) {
        List<Long> userPermIdList = Arrays.asList(userPermIds);
        return userPermMapper.deleteBatchIds(userPermIdList);
    }

    public int updateUserPerm(UserPerm userPerm) {
        return userPermMapper.updateById(userPerm);
    }

    public int updateUserPerm(UserPermVO userPermVO) {
        return userPermMapper.updateByNameCode(userPermVO.getUserPermId(), userPermVO.getUserName(), userPermVO.getPermCode());
    }

    public UserPermVO getUserPermVO(Long userPermId) {
        return userPermVOMapper.selectById(userPermId);
    }

    public List<String> getUserPermList(Object userName) {
        QueryWrapper<UserPermVO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", userName);
        List<UserPermVO> userPermVOList = userPermVOMapper.selectList(queryWrapper);
        List<String> list = new ArrayList<>();
        for (UserPermVO userPermVO : userPermVOList) {
            list.add(userPermVO.getPermCode());
        }
        return list;
    }

    public List<String> getUserRoleList(Object userName) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", userName);
        User user = userMapper.selectOne(queryWrapper);
        List<String> list = new ArrayList<>();
        if (user == null) {
            list.add("volunteer");
        } else if (ADMIN.equals(user.getUserType())) {
            list.add("admin");
        } else if (OPERATOR.equals(user.getUserType())) {
            list.add("operator");
        } else if (TEACHER.equals(user.getUserType())) {
            list.add("teacher");
        }
        return list;
    }

    public List<UserPermVO> getUserPermVOList() {
        return userPermVOMapper.selectList(null);
    }

    public Page<UserPermVO> getUserPermVOPage(Pagination pagination, UserPermVO userPermVO) {
        Page<UserPermVO> page = new Page<>(pagination.getCurrent(), pagination.getSize());
        OrderItem orderItem = new OrderItem(pagination.getColumn(), pagination.isAsc());
        page.addOrder(orderItem);
        QueryWrapper<UserPermVO> queryWrapper = new QueryWrapper<>();
        if (userPermVO != null) {
            if (userPermVO.getUserName() != null && !"".equals(userPermVO.getUserName())) {
                queryWrapper.like("user_name", userPermVO.getUserName());
            }
            if (userPermVO.getUserType() != null && !"".equals(userPermVO.getUserType())) {
                queryWrapper.like("user_type", userPermVO.getUserType());
            }
            if (userPermVO.getPermName() != null && !"".equals(userPermVO.getPermName())) {
                queryWrapper.like("perm_name", userPermVO.getPermName());
            }
            if (userPermVO.getPermCode() != null && !"".equals(userPermVO.getPermCode())) {
                queryWrapper.like("perm_code", userPermVO.getPermCode());
            }
        }
        return userPermVOMapper.selectPage(page, queryWrapper);
    }

    public int importUserPerm(MultipartFile file) {
        int i = 0;
        try {
            ExcelListener listener = new ExcelListener();
            EasyExcel.read(file.getInputStream(), UserPermVO.class, listener).sheet().doRead();
            List<Object> list = listener.getList();
            for (Object o : list) {
                i += insertUserPerm((UserPermVO) o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }
}
