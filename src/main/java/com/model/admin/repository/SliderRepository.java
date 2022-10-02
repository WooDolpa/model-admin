package com.model.admin.repository;

import com.model.common.model.SliderModel;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * packageName : com.model.admin.repository
 * className : SliderRepository
 * user : jwlee
 * date : 2022/10/02
 */
@Repository
@Mapper
public interface SliderRepository {

    @Select("<script>                                                                                   "
            +"select /* SliderRepository.findSliderListTotalCount */                                    "
            +"count(1) from slider                                                                      "
            +"<where>                                                                                   "
            +"1=1                                                                                       "
            +"<if test=\"dataStatus != null and dataStatus != '' \">                                    "
            +"and data_status = #{dataStatus}                                                           "
            +"</if>                                                                                     "
            +"<if test=\"keyword != null and keyword != '' \">                                          "
            +"and (title like concat('%',#{keyword},'%') or content like concat('%',#{keyword},'%'))    "
            +"</if>                                                                                     "
            +"</where>                                                                                  "
            +"</script>                                                                                 "
    )
    int findSliderListTotalCount(Map<String, Object> map);

    @Select("<script>                                                                                   "
            +"select /* SliderRepository.findSliderList */                                              "
            +"* from slider                                                                             "
            +"<where>                                                                                   "
            +"1=1                                                                                       "
            +"<if test=\"dataStatus != null and dataStatus != '' \">                                    "
            +"and data_status = #{dataStatus}                                                           "
            +"</if>                                                                                     "
            +"<if test=\"keyword != null and keyword != '' \">                                          "
            +"and (title like concat('%',#{keyword},'%') or content like concat('%',#{keyword},'%'))    "
            +"</if>                                                                                     "
            +"<if test=\"deleteSort != null \">                                                         "
            +"and sort <![CDATA[ > ]]> #{deleteSort}                                                    "
            +"</if>                                                                                     "
            +"</where>                                                                                  "
            +"<if test=\"orderByList != null \">                                                        "
            +"order by                                                                                  "
            +"<foreach collection=\"orderByList\" item=\"item\" index=\"index\" separator=\",\">        "
            +"${item}                                                                                   "
            +"</foreach>                                                                                "
            +"</if>                                                                                     "
            +"<if test=\"offset != null and size != null\">                                             "
            +"limit #{offset},#{size}                                                                   "
            +"</if>                                                                                     "
            +"</script>                                                                                 "
    )
    List<SliderModel> findSliderList(Map<String, Object> map);

    /**
     * 슬라이드 조회
     * @param map
     * @return
     */
    @Select("<script>                                                                                   "
            +"select /* SliderRepository.findSlider */                                                  "
            +"* from slider                                                                             "
            +"<where>                                                                                   "
            +"<if test=\"sliderNo != null \">                                                           "
            +"slider_no = #{sliderNo}                                                                   "
            +"</if>                                                                                     "
            +"</where>                                                                                  "
            +"</script>                                                                                 "
    )
    SliderModel findSlider (Map<String, Object> map);

    /**
     * 마지막 등록된 이미지 넘버
     * @return
     */
    @Select("<script>                                                                                   "
            +"select /* SliderRepository.findMaxImgNum */                                               "
            +"ifnull(max(img_num),0)                                                                    "
            +"from slider                                                                               "
            +"</script>                                                                                 "
    )
    int findMaxImgNum ();

    /**
     * 마지막 순번 조회
     * @return
     */
    @Select("<script>                                                                                   "
            +"select /* SliderRepository.findMaxSort */                                                 "
            +"ifnull(max(sort),0)                                                                       "
            +"from slider                                                                               "
            +"</script>                                                                                 "
    )
    int findMaxSort ();

    /**
     * 슬라이드 등록
     * @param map
     * @return
     */
    @Insert("<script>                                                                                   "
            +"insert /* SliderRepository.insertSlider */ into slider                                    "
            +"(title, content, img_url, sort, img_num, data_status, reg_datetime)                       "
            +"values(#{title}, #{content}, #{imgUrl}, #{sort}, #{imgNum}, #{dataStatus}, now())         "
            +"</script>                                                                                 "
    )
    int insertSlider (Map<String, Object> map);

    /**
     * 슬라이드 수정
     * @param map
     * @return
     */
    @Update("<script>                                                                                   "
            +"update slider /* SliderRepository.updateSlider */                                         "
            +"<trim prefix=\"SET\" suffixOverrides=\",\">                                               "
            +"<if test=\"title != null \">  title = #{title},  </if>                                    "
            +"<if test=\"content != null \">  content = #{content},  </if>                              "
            +"<if test=\"imgUrl != null \">  img_url = #{imgUrl},  </if>                                "
            +"<if test=\"sort != null \">  sort = #{sort},  </if>                                       "
            +"<if test=\"imgNum != null \">  img_num = #{imgNum},  </if>                                "
            +"<if test=\"dataStatus != null \">  data_status = #{dataStatus},  </if>                    "
            +" upd_datetime = now()                                                                     "
            +"</trim>                                                                                   "
            +"<where>                                                                                   "
            +"<if test=\"sliderNo != null \">                                                           "
            +"slider_no = #{sliderNo}                                                                   "
            +"</if>                                                                                     "
            +"</where>                                                                                  "
            +"</script>                                                                                 "
    )
    int updateSlider (Map<String, Object> map);

    /**
     * 슬라이드 삭제
     * @param map
     * @return
     */
    @Delete("<script>                                                                                   "
            +"delete /* SliderRepository.deleteSlider */                                                "
            +"from slider                                                                               "
            +"<where>                                                                                   "
            +"<if test=\"sliderNo != null\">                                                            "
            +"slider_no = #{sliderNo}                                                                   "
            +"</if>                                                                                     "
            +"</where>                                                                                  "
            +"</script>                                                                                 "
    )
    int deleteSlider (Map<String, Object> map);

}
