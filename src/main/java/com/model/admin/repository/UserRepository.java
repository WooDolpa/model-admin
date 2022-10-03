package com.model.admin.repository;

import com.model.common.model.AdminModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * packageName : com.model.admin.repository
 * className : UserRepository
 * user : jwlee
 * date : 2022/10/02
 */
@Repository
@Mapper
public interface UserRepository {

    @Select("<script>                                                                                   "
            +"select /* UserRepository.findUser */                                                      "
            +"* from admin                                                                              "
            +"<where>                                                                                   "
            +"<if test=\"adminNo != null\">                                                             "
            +"admin_no = #{adminNo}                                                                     "
            +"</if>                                                                                     "
            +"</where>                                                                                  "
            +"</script>                                                                                 "
    )
    AdminModel findUser (Map<String, Object> map);

    @Update("<script>                                                                                   "
            +"update admin /* UserRepository.updateUser */                                              "
            +"<trim prefix=\"SET\" suffixOverrides=\",\">                                               "
            +"<if test=\"id != null \">  id = #{id},  </if>                                             "
            +"<if test=\"name != null \">  name = #{name},  </if>                                       "
            +"<if test=\"email != null \">  email = #{email},  </if>                                    "
            +"<if test=\"password != null \">  password = #{password},  </if>                           "
            +"upd_datetime  = now()                                                                     "
            +"</trim>                                                                                   "
            +"<where>                                                                                   "
            +"<if test=\"adminNo != null\">                                                             "
            +"admin_no = #{adminNo}                                                                     "
            +"</if>                                                                                     "
            +"</where>                                                                                  "
            +"</script>                                                                                 "
    )
    int updateUser (Map<String, Object> map);

}
