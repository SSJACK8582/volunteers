package sky.jack.volunteers.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import sky.jack.volunteers.entity.User;

@Repository
public interface UserMapper extends BaseMapper<User> {
}
