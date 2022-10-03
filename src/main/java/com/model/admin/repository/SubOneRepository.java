package com.model.admin.repository;

import com.model.common.model.AwardModel;
import com.model.common.model.AwardTypeModel;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * packageName : com.model.admin.repository
 * className : SubOneRepository
 * user : jwlee
 * date : 2022/10/02
 */
@Repository
@Mapper
public interface SubOneRepository {


    /**
     * sub1 에 등록된 전체 데이터 수
     * @param map
     * @return
     */
    @Select("<script>                                                                               "
            +"select /* SubOneRepository.findItemListTotalCount */                                  "
            +"count(1) from awards as a                                                             "
            +"inner join awards_type as b on (b.id = a.award_type)                                  "
            +"<where>                                                                               "
            +" 1=1                                                                                  "
            +"<if test=\"keyword != null and keyword != '' \">                                      "
            +" and a.award_name like concat('%',#{keyword},'%')                                     "
            +"</if>                                                                                 "
            +"<if test=\"group != null and group != '' \">                                          "
            +" and a.award_type = #{group}                                                          "
            +"</if>                                                                                 "
            +"</where>                                                                              "
            +"</script>                                                                             "
    )
    int findItemListTotalCount (Map<String, Object> map);

    /**
     * sub1 에 등록된 데이터 조회
     * @param map
     * @return
     */
    @Select("<script>                                                                               "
            +"select /* SubOneRepository.findItemList */                                            "
            +"a.*,                                                                                  "
            +"b.name                                                                                "
            +"from awards as a                                                                      "
            +"inner join awards_type as b on (b.id = a.award_type)                                  "
            +"<where>                                                                               "
            +" 1=1                                                                                  "
            +"<if test=\"keyword != null and keyword != '' \">                                      "
            +" and a.award_name like concat('%',#{keyword},'%')                                     "
            +"</if>                                                                                 "
            +"<if test=\"group != null and group != '' \">                                          "
            +" and a.award_type = #{group}                                                          "
            +"</if>                                                                                 "
            // 삭제 후 순번 정렬 하기위한 조건
            +"<if test=\"deleteRank != null \">                                                     "
            +" and a.rank <![CDATA[ > ]]> #{deleteRank}                                             "
            +"</if>                                                                                 "
            +"</where>                                                                              "
            +"<if test=\"orderByList != null \">                                                    "
            +"order by                                                                              "
            +"<foreach collection=\"orderByList\" item=\"item\" index=\"index\" separator=\",\">    "
            +"${item}                                                                               "
            +"</foreach>                                                                            "
            +"</if>                                                                                 "
            +"<if test=\"offset != null and size != null\">                                         "
            +"limit #{offset},#{size}                                                               "
            +"</if>                                                                                 "
            +"</script>                                                                             "
    )
    List<AwardModel> findItemList (Map<String, Object> map);

    @Select("<script>                                                                               "
            +"select /* SubOneRepository.findItemGroupList */                                       "
            +"* from awards_type                                                                    "
            +"<if test=\"orderByList != null \">                                                    "
            +"order by                                                                              "
            +"<foreach collection=\"orderByList\" item=\"item\" index=\"index\" separator=\",\">    "
            +"${item}                                                                               "
            +"</foreach>                                                                            "
            +"</if>                                                                                 "
            +"</script>                                                                             "
    )
    List<AwardTypeModel> findItemGroupList (Map<String, Object> map);

    @Select("<script>                                                   "
            +"select /* SubOneRepository.findItem */                    "
            +"* from awards                                             "
            +"<where>                                                   "
            +"<if test=\" awardNo > 0 \">                               "
            +"award_no = #{awardNo}                                     "
            +"</if>                                                     "
            +"</where>                                                  "
            +"</script>                                                 "
    )
    AwardModel findItem (Map<String, Object> map);

    /**
     * 마지막 순번 조회
     * @return
     */
    @Select("<script>                                                                               "
            +"select /* SubOneRepository.findMaxRank */                                             "
            +"ifnull(max(rank), 0) from awards                                                      "
            +"</script>                                                                             "
    )
    int findMaxRank ();

    /**
     * sub1 데이터 등록
     * @param map
     * @return
     */
    @Insert("<script>                                                                               "
            +"insert into awards /* SubOneRepository.insertItem */                                  "
            +"(                                                                                     "
            +"award_name, award_type, url, rank, data_status, reg_datetime                          "
            +")values(                                                                              "
            +"#{awardName}, #{awardType}, #{url}, #{rank}, #{dataStatus}, now()                     "
            +")                                                                                     "
            +"</script>                                                                             "
    )
    int insertItem (Map<String, Object> map);

    /**
     * sub1 수정
     * @param map
     * @return
     */
    @Update("<script>                                                                               "
            +"update awards /* SubOneRepository.updateItem */                                       "
            +"<trim prefix=\"SET\" suffixOverrides=\",\">                                           "
            +"<if test=\"awardName != null \">  award_name = #{awardName},  </if>                   "
            +"<if test=\"awardType != null \">  award_type = #{awardType},  </if>                   "
            +"<if test=\"url != null \">        url = #{url},  </if>                                "
            +"<if test=\"rank != null \">       rank = #{rank},     </if>                           "
            +"<if test=\"dataStatus != null\">  data_status = #{dataStatus}, </if>                  "
            +"upd_datetime  = now()                                                                 "
            +"</trim>                                                                               "
            +"<where>                                                                               "
            +"<if test=\"awardNo != null \">                                                        "
            +"award_no = #{awardNo}                                                                 "
            +"</if>                                                                                 "
            +"</where>                                                                              "
            +"</script>                                                                             "
    )
    int updateItem (Map<String, Object> map);

    @Delete("<script>                                                                               "
            +"delete /* SubOneRepository.deleteItem */                                              "
            +"from awards                                                                           "
            +"<where>                                                                               "
            +"<if test=\"awardNo != null \">                                                        "
            +"award_no = #{awardNo}                                                                 "
            +"</if>                                                                                 "
            +"</where>                                                                              "
            +"</script>                                                                             "
    )
    int deleteItem (Map<String, Object> map);


    /**
     * sub1 type 전체 데이터 수
     * @param map
     * @return
     */
    @Select("<script>                                                                               "
            +"select /* SubOneRepository.findTypeListTotalCount */                                  "
            +"count(1) from awards_type                                                             "
            +"<where>                                                                               "
            +"<if test=\"keyword != null and keyword != '' \">                                      "
            +"name like concat('%',#{keyword},'%')                                                  "
            +"</if>                                                                                 "
            +"</where>                                                                              "
            +"</script>                                                                             "
    )
    int findTypeListTotalCount (Map<String, Object> map);

    /**
     * sub1 type list
     * @param map
     * @return
     */
    @Select("<script>                                                                               "
            +"select /* SubOneRepository.findTypeList */                                            "
            +"* from awards_type                                                                    "
            +"<where>                                                                               "
            +"<if test=\"keyword != null and keyword != '' \">                                      "
            +"name like concat('%',#{keyword},'%')                                                  "
            +"</if>                                                                                 "
            +"</where>                                                                              "
            +"<if test=\"orderByList != null \">                                                    "
            +"order by                                                                              "
            +"<foreach collection=\"orderByList\" item=\"item\" index=\"index\" separator=\",\">    "
            +"${item}                                                                               "
            +"</foreach>                                                                            "
            +"</if>                                                                                 "
            +"<if test=\"offset != null and size != null\">                                         "
            +"limit #{offset},#{size}                                                               "
            +"</if>                                                                                 "
            +"</script>                                                                             "
    )
    List<AwardTypeModel> findTypeList (Map<String, Object> map);

    @Select("<script>                                                   "
            +"select /* SubOneRepository.findType */                    "
            +"* from awards_type                                        "
            +"<where>                                                   "
            +"<if test=\" id > 0 \">                                    "
            +"id = #{id}                                                "
            +"</if>                                                     "
            +"</where>                                                  "
            +"</script>                                                 "
    )
    AwardTypeModel findType (Map<String, Object> map);

    @Update("<script>                                                                               "
            +"update awards_type /* SubOneRepository.updateType */                                  "
            +"<trim prefix=\"SET\" suffixOverrides=\",\">                                           "
            +"<if test=\"name != null \">  name = #{name},  </if>                                   "
            +"<if test=\"rank != null \">       rank = #{rank},     </if>                           "
            +"<if test=\"dataStatus != null\">  data_status = #{dataStatus}, </if>                  "
            +"upd_datetime  = now()                                                                 "
            +"</trim>                                                                               "
            +"<where>                                                                               "
            +"<if test=\"id != null \">                                                             "
            +"id = #{id}                                                                            "
            +"</if>                                                                                 "
            +"</where>                                                                              "
            +"</script>                                                                             "
    )
    int updateType (Map<String, Object> map);
}
