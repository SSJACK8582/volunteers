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
import sky.jack.volunteers.entity.Permission;
import sky.jack.volunteers.mapper.PermissionMapper;
import sky.jack.volunteers.tool.ExcelListener;
import sky.jack.volunteers.tool.Pagination;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
public class PermissionService {
    @Autowired
    PermissionMapper permissionMapper;

    public int insertPermission(Permission permission) {
        return permissionMapper.insert(permission);
    }

    public int deletePermission(Long[] permissionIds) {
        List<Long> permissionIdList = Arrays.asList(permissionIds);
        return permissionMapper.deleteBatchIds(permissionIdList);
    }

    public int updatePermission(Permission permission) {
        return permissionMapper.updateById(permission);
    }

    public Permission getPermission(Long permissionId) {
        return permissionMapper.selectById(permissionId);
    }

    public List<Permission> getPermissionList() {
        return permissionMapper.selectList(null);
    }

    public Page<Permission> getPermissionPage(Pagination pagination, Permission permission) {
        Page<Permission> page = new Page<>(pagination.getCurrent(), pagination.getSize());
        OrderItem orderItem = new OrderItem(pagination.getColumn(), pagination.isAsc());
        page.addOrder(orderItem);
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        if (permission != null) {
            if (permission.getPermName() != null && !"".equals(permission.getPermName())) {
                queryWrapper.like("perm_name", permission.getPermName());
            }
            if (permission.getPermCode() != null && !"".equals(permission.getPermCode())) {
                queryWrapper.like("perm_code", permission.getPermCode());
            }
        }
        return permissionMapper.selectPage(page, queryWrapper);
    }

    public int importPermission(MultipartFile file) {
        int i = 0;
        try {
            ExcelListener listener = new ExcelListener();
            EasyExcel.read(file.getInputStream(), Permission.class, listener).sheet().doRead();
            List<Object> list = listener.getList();
            for (Object o : list) {
                i += insertPermission((Permission) o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }
}
