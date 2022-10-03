package com.model.admin.repository;

import com.model.common.model.CategoryItemModel;
import com.model.common.model.CategoryModel;
import com.model.common.model.GalleryTxtModel;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * packageName : com.model.admin.repository
 * className : SubTwoRepository
 * user : jwlee
 * date : 2022/10/02
 */
@Repository
@Mapper
public interface SubTwoRepository {

    /**
     * 마지막 등록된 이미지 넘버
     * @return
     */
    @Select("<script>                                                                                   "
            +"select /* SubTwoRepository.findMaxImgNum */                                               "
            +"ifnull(max(img_number),0)                                                                 "
            +"from category_item                                                                        "
            +"</script>                                                                                 "
    )
    int findMaxImgNum ();

    @Select("<script>                                                                                   "
            +"select /* SubTwoRepository.findMaxRank */                                                 "
            +"ifnull(max(rank),0)                                                                       "
            +"from category_item                                                                        "
            +"</script>                                                                                 "
    )
    int findMaxRank ();

    @Insert("<script>                                                                                                   "
            +"insert into category_item /* SubTwoRepository.insertItem */                                               "
            +"(                                                                                                         "
            +"category_id, title, description, img, url, rank, img_number, reg_datetime                                 "
            +")values(                                                                                                  "
            +"#{categoryId}, #{title}, #{description}, #{img}, #{url}, #{rank}, #{imgNumber}, now()                     "
            +")                                                                                                         "
            +"</script>                                                                                                 "
    )
    int insertItem (Map<String, Object> map);

    @Update("<script>                                                                                                   "
            +"update category_item /* SubTwoRepository.updateItem*/                                                     "
            +"<trim prefix=\"SET\" suffixOverrides=\",\">                                                               "
            +"<if test=\"categoryId != null \">          category_id = #{categoryId},      </if>                        "
            +"<if test=\"title != null \">               title = #{title},                 </if>                        "
            +"<if test=\"description != null \">         description = #{description},     </if>                        "
            +"<if test=\"img != null \">                 img = #{img},                     </if>                        "
            +"<if test=\"url != null \">                 url = #{url},                     </if>                        "
            +"<if test=\"rank != null \">                rank = #{rank},                   </if>                        "
            +"<if test=\"imgNumber != null \">           img_number = #{imgNumber},        </if>                        "
            +"upd_datetime = now()                                                                                      "
            +"</trim>                                                                                                   "
            +"<where>                                                                                                   "
            +"<if test=\"id != null\">                                                                                  "
            +"id = #{id}                                                                                                "
            +"</if>                                                                                                     "
            +"</where>                                                                                                  "
            +"</script>                                                                                                 "
    )
    int updateItem (Map<String, Object> map);

    @Delete("<script>                                                                                                   "
            +"delete from category_item /* SubTwoRepository.deleteItem */                                               "
            +"<where>                                                                                                   "
            +"<if test=\"id != null\">                                                                                  "
            +"id = #{id}                                                                                                "
            +"</if>                                                                                                     "
            +"</where>                                                                                                  "
            +"</script>                                                                                                 "
    )
    int deleteItem (Map<String, Object> map);

    @Select("<script>                                                                                                   "
            +"select /* SubTwoRepository.findItemListTotalCount */                                                      "
            +"count(1) from category_item                                                                               "
            +"<where>                                                                                                   "
            +"  <if test=\"categoryId != null and categoryId != '' \">                                                  "
            +"      category_id = #{categoryId}                                                                         "
            +"  </if>                                                                                                   "
            +"</where>                                                                                                  "
            +"</script>                                                                                                 "
    )
    int findItemListTotalCount(Map<String, Object> map);

    @Select("<script>                                                                                                   "
            +"select /* SubTwoRepository.findItemList */                                                                "
            +"* from category_item                                                                                      "
            +"<where>                                                                                                   "
            +"  <if test=\"categoryId != null and categoryId != '' \">                                                  "
            +"      category_id = #{categoryId}                                                                         "
            +"  </if>                                                                                                   "
            +"</where>                                                                                                  "
            +"<if test=\"orderByList != null \">                                                                        "
            +"order by                                                                                                  "
            +"<foreach collection=\"orderByList\" item=\"item\" index=\"index\" separator=\",\">                        "
            +"${item}                                                                                                   "
            +"</foreach>                                                                                                "
            +"</if>                                                                                                     "
            +"</script>                                                                                                 "
    )
    List<CategoryItemModel> findItemList(Map<String, Object> map);

    @Select("<script>                                                                                                   "
            +"select /* SubTwoRepository.findItem */                                                                    "
            +"* from category_item                                                                                      "
            +"<where>                                                                                                   "
            +"<if test=\"id != null\">                                                                                  "
            +"id = #{id}                                                                                                "
            +"</if>                                                                                                     "
            +"</where>                                                                                                  "
            +"</script>                                                                                                 "
    )
    CategoryItemModel findItem(Map<String, Object> map);

    @Select("<script>                                                                                                   "
            +"select /* SubTwoRepository.findItemText */                                                                "
            +"* from gallery_txt                                                                                        "
            +"</script>                                                                                                 "
    )
    GalleryTxtModel findItemText ();

    @Select("<script>                                                                                                   "
            +"select /* SubTwoRepository.findItemTextTotalCount */                                                      "
            +"count(1) from gallery_txt                                                                                 "
            +"</script>                                                                                                 "
    )
    int findItemTextTotalCount();

    @Update("<script>                                                                                                   "
            +"update gallery_txt /* SubTwoRepository.updateItemText */                                                  "
            +"<trim prefix=\"SET\" suffixOverrides=\",\">                                                               "
            +"<if test=\"txtContents != null \">               txt_contents = #{txtContents},                </if>      "
            +"upd_datetime = now()                                                                                      "
            +"</trim>                                                                                                   "
            +"<where>                                                                                                   "
            +"<if test=\"txtNo != null\">                                                                               "
            +"txt_no = #{txtNo}                                                                                         "
            +"</if>                                                                                                     "
            +"</where>                                                                                                  "
            +"</script>                                                                                                 "
    )
    int updateItemText(Map<String, Object> map);

    @Select("<script>                                                                               "
            +"select /* SubTwoRepository.findTypeListTotalCount */                                  "
            +"count(1) from category                                                                "
            +"<where>                                                                               "
            +"<if test=\"keyword != null and keyword != '' \">                                      "
            +"name like concat('%',#{keyword},'%')                                                  "
            +"</if>                                                                                 "
            +"</where>                                                                              "
            +"</script>                                                                             "
    )
    int findTypeListTotalCount (Map<String, Object> map);

    @Select("<script>                                                                               "
            +"select /* SubTwoRepository.findTypeList */                                            "
            +"* from category                                                                       "
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
    List<CategoryModel> findTypeList (Map<String, Object> map);


    @Select("<script>                                                                               "
            +"select /* SubTwoRepository.findType */                                                "
            +"* from category                                                                       "
            +"<where>                                                                               "
            +"<if test=\" id > 0 \">                                                                "
            +"id = #{id}                                                                            "
            +"</if>                                                                                 "
            +"</where>                                                                              "
            +"</script>                                                                             "
    )
    CategoryModel findType (Map<String, Object> map);


    @Update("<script>                                                                               "
            +"update category /* SubTwoRepository.updateType */                                     "
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


    @Select("<script>                                                                               "
            +"select /* SubTwoRepository.findGroupListTotalCount */                                 "
            +"count(1) from category                                                                "
            +"</script>                                                                             "
    )
    int findGroupListTotalCount (Map<String, Object> map);

    @Select("<script>                                                                               "
            +"select /* SubTwoRepository.findGroupList */                                           "
            +"* from category                                                                       "
            +"<if test=\"orderByList != null \">                                                    "
            +"order by                                                                              "
            +"<foreach collection=\"orderByList\" item=\"item\" index=\"index\" separator=\",\">    "
            +"${item}                                                                               "
            +"</foreach>                                                                            "
            +"</if>                                                                                 "
            +"</script>                                                                             "
    )
    List<CategoryModel> findGroupList (Map<String, Object> map);

}
