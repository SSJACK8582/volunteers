package sky.jack.volunteers.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import sky.jack.volunteers.entity.UserPerm;

@Repository
public interface UserPermMapper extends BaseMapper<UserPerm> {
    @Insert("INSERT INTO user_perm ( user_id, permission_id ) " +
            "VALUES ( ( SELECT user_id FROM user WHERE user_name = #{userName} ), ( SELECT permission_id FROM permission WHERE perm_code = #{permCode} ) )")
    int insertByNameCode(@Param("userName") String userName, @Param("permCode") String permCode);

    @Update("UPDATE user_perm " +
            "SET user_id = ( SELECT user_id FROM USER WHERE user_name = #{userName} ), " +
            "permission_id = ( SELECT permission_id FROM permission WHERE perm_code = #{permCode} ) " +
            "WHERE user_perm_id = #{userPermId}")
    int updateByNameCode(@Param("userPermId") Long userPermId, @Param("userName") String userName, @Param("permCode") String permCode);
}
