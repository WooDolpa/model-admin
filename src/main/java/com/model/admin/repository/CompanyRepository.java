package com.model.admin.repository;

import com.model.common.model.CompanyModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * packageName : com.model.admin.repository
 * className : CompanyRepository
 * user : jwlee
 * date : 2022/10/02
 */
@Repository
@Mapper
public interface CompanyRepository {

    /**
     * 회사 정보 조회
     * @return
     */
    @Select("<script>                                                                                   "
            +"select /* CompanyRepository.getCompany */                                                 "
            +"* from company                                                                            "
            +"</script>                                                                                 "
    )
    CompanyModel findCompany ();

    /**
     * 회사 정보 수정
     * @param map
     * @return
     */
    @Update("<script>                                                                                   "
            +"update company /* CompanyRepository.updateCompany */                                      "
            +"<trim prefix=\"SET\" suffixOverrides=\",\">                                               "
            +"<if test=\"cpName != null and cpName != '' \">        cp_name = #{cpName},     </if>      "
            +"<if test=\"cpImgUrl != null and cpImgUrl != '' \">    cp_imgurl = #{cpImgUrl}, </if>      "
            +"upd_datetime = now()                                                                      "
            +"</trim>                                                                                   "
            +"<where>                                                                                   "
            +"<if test=\"cpNo != null \">                                                               "
            +"cp_no = #{cpNo}                                                                           "
            +"</if>                                                                                     "
            +"</where>                                                                                  "
            +"</script>                                                                                 "
    )
    int updateCompany (Map<String, Object> map);

}
