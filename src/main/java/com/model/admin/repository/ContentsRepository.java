package com.model.admin.repository;

import com.model.common.model.ContentsModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * packageName : com.model.admin.repository
 * className : ContentRepository
 * user : jwlee
 * date : 2022/10/02
 */
@Repository
@Mapper
public interface ContentsRepository {

    @Select("<script>                                                                                                   "
            +"select /* ContentsRepository.findContents */                                                              "
            +"* from contents                                                                                           "
            +"</script>                                                                                                 "
    )
    ContentsModel findContents ();

    @Update("<script>                                                                                                   "
            +"update contents /* ContentsRepository.updateContents */                                                   "
            +"<trim prefix=\"SET\" suffixOverrides=\",\">                                                               "
            +"<if test=\"ctTxt != null \">        ct_txt = #{ctTxt},    </if>                                           "
            +"upd_datetime = now()                                                                                      "
            +"</trim>                                                                                                   "
            +"<where>                                                                                                   "
            +"<if test=\"ctNo != null\">                                                                                "
            +"ct_no = #{ctNo}                                                                                           "
            +"</if>                                                                                                     "
            +"</where>                                                                                                  "
            +"</script>                                                                                                 "
    )
    int updateContents (Map<String, Object> map);

}
