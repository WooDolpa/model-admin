package com.model.admin.service;

import com.model.admin.constatns.ApiConstants;
import com.model.admin.dto.SubDto;
import com.model.admin.exception.ManagedException;
import com.model.admin.exception.ManagedExceptionCode;
import com.model.admin.repository.SubTwoRepository;
import com.model.admin.utils.ImageUtils;
import com.model.common.model.CategoryItemModel;
import com.model.common.model.CategoryModel;
import com.model.common.model.GalleryTxtModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * packageName : com.model.admin.service
 * className : SubTwoService
 * user : jwlee
 * date : 2022/10/02
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SubTwoService {

    private final SubTwoRepository subTwoRepository;
    private final ImageUtils imageUtils;

    @Value("${img.item.directory.uri}")
    private String itemDirectoryUri;
    @Value("${img.item.uri}")
    private String itemUri;

    /**
     * Item 등록
     *
     * @param title
     * @param description
     * @param url
     * @param imgFile
     * @return
     */
    @Transactional
    public int insertItem(int groupId, final String title, final String description, final String url, final MultipartFile imgFile){

        int result = 0;

        try {

            int maxImgNum = subTwoRepository.findMaxImgNum();
            maxImgNum++;

            // 마지막 순번
            int maxRank = subTwoRepository.findMaxRank();
            maxRank++;

            String fileName = imageUtils.saveImage(imgFile, maxImgNum, itemDirectoryUri);

            if(fileName != null){

                String imgPath = itemUri+"/"+fileName;

                Map<String, Object> map = new HashMap<>();
                map.put("categoryId", groupId);
                map.put("title", title);
                map.put("description",description);
                map.put("img", imgPath);
                map.put("imgNumber", maxImgNum);
                map.put("rank", maxRank);
                map.put("url", url);

                result = subTwoRepository.insertItem(map);
            }

        }catch (Exception e){
            log.warn("SubTwoService|item|insert|error|{}", e.getMessage());
        }

        return result;
    }

    /**
     * Item 업데이트
     *
     * @param id
     * @param title
     * @param description
     * @param url
     * @param imgFile
     * @return
     */
    @Transactional
    public int updateItem (final int id, String groupId, final String title, final String description, final String url, final int rank, final MultipartFile imgFile){

        int result = 0;

//         순번 사이즈 체크
        int totalCount = findItemListTotalCount("");
        if(rank > totalCount){
            throw new ManagedException(ManagedExceptionCode.InvalidRank, ApiConstants.INVALID_RANK);
        }

        try {

            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("categoryId", groupId);

            Optional<CategoryItemModel> categoryItemModelOptional =  Optional.ofNullable(subTwoRepository.findItem(map));

            if(categoryItemModelOptional.isPresent()){

                CategoryItemModel categoryItemModel = categoryItemModelOptional.get();

                if(rank == categoryItemModel.getRank()){

                    // 변경 이미지 체크
                    String fileName = null;
                    int maxImgNum = 0;

                    if(imgFile != null){

                        boolean deleteImageStatus = imageUtils.deleteImage(categoryItemModel.getImg(), itemDirectoryUri);

                        if(deleteImageStatus){

                            maxImgNum = subTwoRepository.findMaxImgNum();
                            maxImgNum++;
                            fileName = imageUtils.saveImage(imgFile, maxImgNum, itemDirectoryUri);
                        }
                    }

                    map.put("title", title);
                    map.put("description",description);
                    if(fileName != null){
                        map.put("img", itemUri+"/"+fileName);
                    }
                    map.put("url", url);
                    map.put("rank", rank);
                    if(maxImgNum > 0){
                        map.put("imgNumber", maxImgNum);
                    }
                    result = subTwoRepository.updateItem(map);

                }else{

                    Map<String, Object> searchMap = new HashMap<>();

                    List<String> orderByList = new ArrayList<>();
                    orderByList.add(new StringBuilder().append("rank").append(" asc").toString());

                    searchMap.put("orderByList", orderByList);

                    Optional<List<CategoryItemModel>> categoryItemModelListOptional = Optional.ofNullable(subTwoRepository.findItemList(searchMap));

                    if(categoryItemModelListOptional.isPresent()){

                        List<CategoryItemModel> categoryItemModelList = categoryItemModelListOptional.get();

                        if(rank < categoryItemModel.getRank()){

                            for(CategoryItemModel model : categoryItemModelList){
                                if(model.getRank() >= rank && model.getRank() < categoryItemModel.getRank()){
                                    Map<String, Object> upMap = new HashMap<>();
                                    upMap.put("id", model.getId());
                                    upMap.put("rank", (model.getRank()+1));
                                    subTwoRepository.updateItem(upMap);
                                }
                            }

                        }else{

                            for(CategoryItemModel model : categoryItemModelList){
                                if(model.getRank() > categoryItemModel.getRank() && model.getRank() <= rank){
                                    Map<String, Object> upMap = new HashMap<>();
                                    upMap.put("id", model.getId());
                                    upMap.put("rank", (model.getRank()-1));
                                    subTwoRepository.updateItem(upMap);
                                }
                            }

                        }

                        // 변경 이미지 체크
                        String fileName = null;
                        int maxImgNum = 0;

                        if(imgFile != null){

                            boolean deleteImageStatus = imageUtils.deleteImage(categoryItemModel.getImg(), itemDirectoryUri);

                            if(deleteImageStatus){

                                maxImgNum = subTwoRepository.findMaxImgNum();
                                maxImgNum++;
                                fileName = imageUtils.saveImage(imgFile, maxImgNum, itemDirectoryUri);
                            }
                        }

                        map.put("title", title);
                        map.put("description",description);
                        if(fileName != null){
                            map.put("img", itemUri+"/"+fileName);
                        }
                        map.put("url", url);
                        map.put("rank", rank);
                        if(maxImgNum > 0){
                            map.put("imgNumber", maxImgNum);
                        }
                        result = subTwoRepository.updateItem(map);
                    }
                }

            }

        }catch (Exception e){
            log.warn("SubTwoService|item|update|error|{}", e.getMessage());
        }

        return result;
    }

    /**
     * Item 삭제
     *
     * @param galleryNo
     * @return
     */
    public int deleteItem (final int galleryNo){


        int count = 0;
        Map<String, Object> map = new HashMap<>();
        map.put("id", galleryNo);

        Optional<CategoryItemModel> categoryItemModelOptional =  Optional.ofNullable(subTwoRepository.findItem(map));

        if(categoryItemModelOptional.isPresent()){

            CategoryItemModel categoryItemModel = categoryItemModelOptional.get();
            // 이미지 파일 삭제
            imageUtils.deleteImage(categoryItemModel.getImg(), itemDirectoryUri);
            count = subTwoRepository.deleteItem(map);
        }

        return count;
    }

    /**
     * Item 조회
     *
     * @return
     */
    public int findItemListTotalCount(String groupId){
        Map<String, Object> map = new HashMap<>();
        map.put("categoryId", groupId);
        return subTwoRepository.findItemListTotalCount(map);
    }

    public List<List<SubDto.SubTwoDto>> findItemList(String groupId) {

        List<List<SubDto.SubTwoDto>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();

        List<String> orderByList = new ArrayList<>();
        orderByList.add(new StringBuilder().append("rank").append(" asc").toString());

        map.put("orderByList", orderByList);
        map.put("categoryId", groupId);

        Optional<List<CategoryItemModel>> galleryModelListOptional = Optional.ofNullable(subTwoRepository.findItemList(map));

        if(galleryModelListOptional.isPresent()){

            List<CategoryItemModel> categoryItemModelList = galleryModelListOptional.get();

            List<SubDto.SubTwoDto> dtoList = new ArrayList<>();

            for(CategoryItemModel model : categoryItemModelList){

                SubDto.SubTwoDto dto = new SubDto.SubTwoDto();
                dto.setId(model.getId());
                dto.setGroupId(model.getCategoryId());
                dto.setTitle(model.getTitle());
                dto.setDescription(model.getDescription());
                dto.setImg(model.getImg());
                dto.setUrl(model.getUrl());
                dto.setRank(model.getRank());
                dto.setRegDatetime(model.getRegDatetime());
                dto.setUpdDatetime(model.getUpdDatetime());

                dtoList.add(dto);

                if(dtoList.size() == ApiConstants.ITEM_MAX_SIZE){
                    list.add(dtoList);
                    dtoList = new ArrayList<>();
                }

            }

            if(dtoList.size() > 0){
                list.add(dtoList);
            }

        }

        return list;
    }

    /**
     * Item(버튼) 텍스트 데이터 수
     *
     * @return
     */
    public int findItemTextTotalCount() {
        return subTwoRepository.findItemTextTotalCount();
    }

    /**
     * Item(버튼) 텍스트 조회
     *
     * @return
     */
    public List<SubDto.ResSubTwoTextDto> findItemText (){

        List<SubDto.ResSubTwoTextDto> list = new ArrayList<>();

        Optional<GalleryTxtModel> galleryTxtModelOptional = Optional.ofNullable(subTwoRepository.findItemText());

        if(galleryTxtModelOptional.isPresent()){

            GalleryTxtModel galleryTxtModel = galleryTxtModelOptional.get();
            SubDto.ResSubTwoTextDto dto = new SubDto.ResSubTwoTextDto();

            dto.setTxtNo(galleryTxtModel.getTxtNo());
            dto.setTxtContents(galleryTxtModel.getTxtContents());

            list.add(dto);
        }

        return list;
    }

    /**
     * Item(버튼) 텍스트 수정
     *
     * @param dto
     * @return
     */
    public int updateItemText (final SubDto.SubTwoTextDto dto){

        Map<String, Object> map = new HashMap<>();

        map.put("txtNo", dto.getTxtNo());
        map.put("txtContents", dto.getTxtContents());

        return subTwoRepository.updateItemText(map);
    }

    public int findTypeListTotalCount (String keyword) {
        Map<String, Object> map = new HashMap<>();
        map.put("keyword", keyword);

        return subTwoRepository.findTypeListTotalCount(map);
    }

    public List<SubDto.SubTwoTypeDto> findTypeList (String type, String keyword, int page, int size) {

        List<SubDto.SubTwoTypeDto> list = new ArrayList<>();
        List<String> orderByList = new ArrayList<>();

        if(ApiConstants.SEARCH_ORDER_BY_RANK.equals(type)){
            orderByList.add(new StringBuilder().append("rank").append(" desc").toString());
        }else if(ApiConstants.SEARCH_ORDER_BY_REG.equals(type)){
            orderByList.add(new StringBuilder().append("reg_datetime").append(" desc").toString());
        }else{
            orderByList.add(new StringBuilder().append("rank").append(" asc").toString());
        }

        Map<String, Object> map = new HashMap<>();
        map.put("keyword", keyword);
        map.put("offset", (page * size));
        map.put("size", size);
        map.put("orderByList", orderByList);

        Optional<List<CategoryModel>> categoryModelListOptional = Optional.ofNullable(subTwoRepository.findTypeList(map));

        if(categoryModelListOptional.isPresent()){

            List<CategoryModel> categoryModelList = categoryModelListOptional.get();

            for (CategoryModel categoryModel : categoryModelList) {
                SubDto.SubTwoTypeDto dto = new SubDto.SubTwoTypeDto();
                dto.setId(categoryModel.getId());
                dto.setName(categoryModel.getName());
                dto.setRank(categoryModel.getRank());
                dto.setDataStatus(categoryModel.getDataStatus().toStr());
                list.add(dto);
            }

        }

        return list;
    }


    @Transactional
    public void updateType (SubDto.SubTwoTypeUpdReqDto dto){

        if(dto.getRank() == null){
            throw new ManagedException(ManagedExceptionCode.InvalidRank, ApiConstants.INVALID_RANK);
        }

        // 수정 대상 항목 조회
        Map<String, Object> map = new HashMap<>();
        map.put("id", dto.getId());
        Optional<CategoryModel> categoryModelOptional = Optional.ofNullable(subTwoRepository.findType(map));

        if(categoryModelOptional.isPresent()){

            CategoryModel categoryModel = categoryModelOptional.get();
            // 변경하려는 순위
            int rank = dto.getRank();
            map = new HashMap<>();

            if(rank == categoryModel.getRank()){
                // 순번이 같음
                map.put("id", dto.getId());
                map.put("name", dto.getName());
                map.put("rank", rank);
                map.put("dataStatus", dto.getDataStatus());

                subTwoRepository.updateType(map);

            }else{
                // 기존 순번 보다 크거나 작음
                // 전체 리스트 가져오기
                List<String> orderByList = new ArrayList<>();
                orderByList.add(new StringBuilder().append("rank").append(" asc").toString());

                Map<String, Object> searchMap = new HashMap<>();
                searchMap.put("orderByList", orderByList);
                Optional<List<CategoryModel>> categoryModelListOptional  = Optional.of(subTwoRepository.findTypeList(searchMap));

                List<CategoryModel> categoryModelList = categoryModelListOptional.get();

                // 변경하려는 순위 유효성 검사
                if(rank > categoryModelList.size()){
                    throw new ManagedException(ManagedExceptionCode.InvalidRank, ApiConstants.INVALID_RANK);
                }

                if(categoryModel.getRank() > rank){
                    // 기존 순번보다 높은 순번일때  ex) 9 -> 2
                    for(CategoryModel model : categoryModelList){
                        // 바꾸려는 순번부터 하나씩 뒤로 밀리도록 수정
                        if(model.getRank() >= rank && model.getRank() < categoryModel.getRank()){
                            Map<String, Object> upMap = new HashMap<>();
                            upMap.put("id", model.getId());
                            upMap.put("rank", (model.getRank()+1));
                            subTwoRepository.updateType(upMap);
                        }
                    }
                }else{
                    // 기존 순번보다 낮은 순번일때  ex) 2 -> 9
                    for(CategoryModel model : categoryModelList){

                        if(model.getRank() > categoryModel.getRank()  && model.getRank() <= rank){

                            Map<String, Object> upMap = new HashMap<>();
                            upMap.put("id", model.getId());
                            upMap.put("rank", (model.getRank()-1));
                            subTwoRepository.updateType(upMap);

                        }
                    }
                }

                // 수정
                map.put("id", dto.getId());
                map.put("name", dto.getName());
                map.put("rank", rank);
                map.put("dataStatus", dto.getDataStatus());

                subTwoRepository.updateType(map);
            }

        }else{
            throw new ManagedException(ManagedExceptionCode.UpdateError, ApiConstants.UPDATE_ERROR);
        }
    }

    public int findGroupListTotalCount () {
        return subTwoRepository.findGroupListTotalCount(new HashMap<>());
    }

    public List<SubDto.SubTwoGroupDto> findGroupList () {

        Map<String, Object> map = new HashMap<>();
        List<SubDto.SubTwoGroupDto> list = new ArrayList<>();

        List<String> orderByList = new ArrayList<>();
        orderByList.add(new StringBuilder().append("rank").append(" asc").toString());

        map.put("orderByList", orderByList);

        Optional<List<CategoryModel>> categoryModelListOptional = Optional.ofNullable(subTwoRepository.findGroupList(map));

        if (categoryModelListOptional.isPresent()) {
            List<CategoryModel> categoryModelList = categoryModelListOptional.get();
            for (CategoryModel model : categoryModelList){
                SubDto.SubTwoGroupDto dto = new SubDto.SubTwoGroupDto();
                dto.setId(model.getId());
                dto.setName(model.getName());
                dto.setRank(model.getRank());
                dto.setDataStatus(model.getDataStatus().toStr());
                list.add(dto);
            }
        }

        return list;
    }
}
