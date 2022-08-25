package sky.jack.volunteers.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sky.jack.volunteers.entity.Message;
import sky.jack.volunteers.service.MessageService;
import sky.jack.volunteers.tool.AjaxResult;
import sky.jack.volunteers.tool.Pagination;

@CrossOrigin
@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @SaCheckRole("volunteer")
    @PostMapping("/list")
    private AjaxResult getMessageList(Pagination pagination, @RequestBody Message message) {
        Page<Message> messagePage = messageService.getMessagePage(pagination, message);
        if (messagePage != null) {
            return AjaxResult.success(messagePage);
        } else {
            return AjaxResult.error();
        }
    }
}
