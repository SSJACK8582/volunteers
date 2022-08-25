package sky.jack.volunteers.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import sky.jack.volunteers.entity.Message;

@Repository
public interface MessageMapper extends BaseMapper<Message> {
}
