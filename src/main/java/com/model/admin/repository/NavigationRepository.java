package com.model.admin.repository;

import com.model.common.model.NavigationModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * packageName : com.model.admin.repository
 * className : NavigationRepository
 * user : jwlee
 * date : 2022/10/02
 */
@Repository
@Mapper
public interface NavigationRepository {

    /**
     * 네비게이션 조회
     * @return
     */
    @Select("<script>                                                                                                           "
            +"select /* NavigationRepository.findNavigationList */                                                              "
            +"* from navigation                                                                                                 "
            +"<where>                                                                                                           "
            +"<if test=\"dataStatus != null and dataStatus != '' \">                                                            "
            +"data_status = #{dataStatus}                                                                                       "
            +"</if>                                                                                                             "
            +"</where>                                                                                                          "
            +"<if test=\"orderByList != null \">                                                                                "
            +"order by                                                                                                          "
            +"<foreach collection=\"orderByList\" item=\"item\" index=\"index\" separator=\",\">                                "
            +"${item}                                                                                                           "
            +"</foreach>                                                                                                        "
            +"</if>                                                                                                             "
            +"</script>                                                                                                         "
    )
    List<NavigationModel> findNavigationList (Map<String, Object> map);

    @Update("<script>                                                                                                           "
            +"update navigation /* NavigationRepository.updateNavigation */                                                     "
            +"<trim prefix=\"SET\" suffixOverrides=\",\">                                                                       "
            +"<if test=\"title != null and title != '' \">                  title = #{title},    </if>                          "
            +"<if test=\"dataStatus != null and dataStatus != '' \">        data_status = #{dataStatus},    </if>               "
            +"upd_datetime = now()                                                                                              "
            +"</trim>                                                                                                           "
            +"<where>                                                                                                           "
            +"<if test=\"navNo != null\">                                                                                       "
            +"nav_no = #{navNo}                                                                                                 "
            +"</if>                                                                                                             "
            +"</where>                                                                                                          "
            +"</script>                                                                                                         "
    )
    int updateNavigation (Map<String, Object> map);

}
