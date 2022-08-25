package sky.jack.volunteers.tool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sky.jack.volunteers.mapper.ActivityMapper;

import java.util.Date;

@Component
public class Task {
    @Autowired
    ActivityMapper activityMapper;

    @Scheduled(cron = "0 */1 * * * *")
    public void checkActivity() {
        int i = activityMapper.checkActivity(new Date());
    }
}
