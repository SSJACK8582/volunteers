package sky.jack.volunteers.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import sky.jack.volunteers.entity.Message;
import sky.jack.volunteers.mapper.MessageMapper;
import sky.jack.volunteers.tool.Pagination;


@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
public class MessageService {
    @Autowired
    MessageMapper messageMapper;
    @Autowired
    UserService userService;
    @Autowired
    VolunteerService volunteerService;

    public int insertMessage(Message message) {
        return messageMapper.insert(message);
    }

    public Page<Message> getMessagePage(Pagination pagination, Message message) {
        Long volunteerId = volunteerService.getVolunteerInfo().getVolunteerId();
        Page<Message> page = new Page<>(pagination.getCurrent(), pagination.getSize());
        OrderItem orderItem = new OrderItem(pagination.getColumn(), pagination.isAsc());
        page.addOrder(orderItem);
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("volunteer_id", volunteerId);
        if (message != null) {
            if (message.getTitle() != null && !"".equals(message.getTitle())) {
                queryWrapper.like("title", message.getTitle());
            }
            if (message.getContent() != null && !"".equals(message.getContent())) {
                queryWrapper.like("content", message.getContent());
            }
        }
        return messageMapper.selectPage(page, queryWrapper);
    }
}
