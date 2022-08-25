package sky.jack.volunteers.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import sky.jack.volunteers.entity.Activity;

import java.util.Date;

@Repository
public interface ActivityMapper extends BaseMapper<Activity> {
    @Update("UPDATE activity " +
            "SET act_status = " +
            "CASE " +
            "WHEN #{date} > enroll_start_time AND #{date} < enroll_end_time THEN '3' " +
            "WHEN #{date} > enroll_end_time AND #{date} < act_start_time THEN '4' " +
            "WHEN #{date} > act_end_time THEN '5' " +
            "ELSE '2' " +
            "END " +
            "WHERE act_status IN ( '2', '3', '4') AND del_flag = '0'")
    int checkActivity(Date date);
}
