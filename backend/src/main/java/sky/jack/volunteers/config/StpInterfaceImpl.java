package sky.jack.volunteers.config;

import cn.dev33.satoken.stp.StpInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sky.jack.volunteers.service.UserPermService;

import java.util.List;

@Component
public class StpInterfaceImpl implements StpInterface {
    @Autowired
    private UserPermService userPermService;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return userPermService.getUserPermList(loginId);
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return userPermService.getUserRoleList(loginId);
    }
}
