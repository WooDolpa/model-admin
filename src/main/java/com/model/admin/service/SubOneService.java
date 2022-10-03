package com.model.admin.service;

import com.model.admin.constatns.ApiConstants;
import com.model.admin.dto.SubDto;
import com.model.admin.exception.ManagedException;
import com.model.admin.exception.ManagedExceptionCode;
import com.model.admin.repository.SubOneRepository;
import com.model.common.enums.DataStatus;
import com.model.common.model.AwardModel;
import com.model.common.model.AwardTypeModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * packageName : com.model.admin.service
 * className : SubOneService
 * user : jwlee
 * date : 2022/10/02
 */
@Service
@RequiredArgsConstructor
public class SubOneService {

    private final SubOneRepository subOneRepository;

    /**
     * sub1 데이터 등록
     * @param dto
     * @return
     */
    @Transactional
    public int insertItem (SubDto.SubOneRegReqDto dto){

        int maxRank = subOneRepository.findMaxRank();
        maxRank++;

        Map<String, Object> map = new HashMap<>();
        map.put("awardName", dto.getAwardName());
        map.put("awardType", dto.getAwardType());
        map.put("url", dto.getUrl());
        map.put("rank", maxRank);
        map.put("dataStatus", DataStatus.Active.toStr());

        return subOneRepository.insertItem(map);
    }

    /**
     * sub1 수정
     * @param dto
     * @return
     */
    @Transactional
    public void updateItem (SubDto.SubOneUpdReqDto dto){

        if(!StringUtils.hasLength(dto.getRank())){
            throw new ManagedException(ManagedExceptionCode.InvalidRank, ApiConstants.INVALID_RANK);
        }

        // 수정 대상 항목 조회
        Map<String, Object> map = new HashMap<>();
        map.put("awardNo", dto.getAwardNo());
        Optional<AwardModel> awardModelOptional = Optional.ofNullable(subOneRepository.findItem(map));

        if(awardModelOptional.isPresent()){

            AwardModel awardModel = awardModelOptional.get();
            // 변경하려는 순위
            int rank = Integer.parseInt(dto.getRank());
            map = new HashMap<>();

            if(rank == awardModel.getRank()){
                // 순번이 같음
                map.put("awardNo", dto.getAwardNo());
                map.put("awardName", dto.getAwardName());
                map.put("awardType", dto.getAwardType());
                map.put("url", dto.getUrl());
                map.put("rank", rank);
                map.put("dataStatus", dto.getDataStatus());

                subOneRepository.updateItem(map);
            }else{
                // 기존 순번 보다 크거나 작음
                // 전체 리스트 가져오기
                List<String> orderByList = new ArrayList<>();
                orderByList.add(new StringBuilder().append("a.rank").append(" asc").toString());

                Map<String, Object> searchMap = new HashMap<>();
                searchMap.put("orderByList", orderByList);
                Optional<List<AwardModel>> awardModelListOptional = Optional.of(subOneRepository.findItemList(searchMap));

                List<AwardModel> awardModelList = awardModelListOptional.get();

                // 변경하려는 순위 유효성 검사
                if(rank > awardModelList.size()){
                    throw new ManagedException(ManagedExceptionCode.InvalidRank, ApiConstants.INVALID_RANK);
                }

                if(awardModel.getRank() > rank){
                    // 기존 순번보다 높은 순번일때  ex) 9 -> 2
                    for(AwardModel model : awardModelList){
                        // 바꾸려는 순번부터 하나씩 뒤로 밀리도록 수정
                        if(model.getRank() >= rank && model.getRank() < awardModel.getRank()){
                            Map<String, Object> upMap = new HashMap<>();
                            upMap.put("awardNo", model.getAwardNo());
                            upMap.put("rank", (model.getRank()+1));
                            subOneRepository.updateItem(upMap);
                        }
                    }
                }else{
                    // 기존 순번보다 낮은 순번일때  ex) 2 -> 9
                    for(AwardModel model : awardModelList){

                        if(model.getRank() > awardModel.getRank()  && model.getRank() <= rank){

                            Map<String, Object> upMap = new HashMap<>();
                            upMap.put("awardNo", model.getAwardNo());
                            upMap.put("rank", (model.getRank()-1));
                            subOneRepository.updateItem(upMap);

                        }
                    }
                }

                // 수정
                map.put("awardNo", dto.getAwardNo());
                map.put("awardName", dto.getAwardName());
                map.put("awardType", dto.getAwardType());
                map.put("url", dto.getUrl());
                map.put("rank", rank);
                map.put("dataStatus", dto.getDataStatus());

                subOneRepository.updateItem(map);
            }

        }else{
            throw new ManagedException(ManagedExceptionCode.UpdateError, ApiConstants.UPDATE_ERROR);
        }
    }

    /**
     * Sub1 삭제
     * @param dto
     */
    @Transactional
    public void deleteItem(SubDto.SubOneDelReqDto dto){

        int rank = 0;

        Map<String, Object> map = new HashMap<>();
        map.put("awardNo", dto.getAwardNo());

        Optional<AwardModel> awardModelOptional = Optional.ofNullable(subOneRepository.findItem(map));
        if(awardModelOptional.isPresent()){

            AwardModel awardModel = awardModelOptional.get();
            rank = awardModel.getRank();

            int result = subOneRepository.deleteItem(map);

            if(result > 0){
                if(rank > 0){
                    map = new HashMap<>();
                    map.put("deleteRank", rank);
                    Optional<List<AwardModel>> awardModelListOptional = Optional.ofNullable(subOneRepository.findItemList(map));
                    if(awardModelListOptional.isPresent()){
                        List<AwardModel> awardModelList = awardModelListOptional.get();
                        for(AwardModel model : awardModelList){
                            // 한칸씩 위로 올리기
                            int updRank = model.getRank();
                            updRank--;

                            Map<String, Object> updMap = new HashMap<>();
                            updMap.put("awardNo", model.getAwardNo());
                            updMap.put("rank", updRank);

                            subOneRepository.updateItem(updMap);
                        }
                    }
                }
            }
        }
    }

    /**
     * Sub1 에 해당되는 전체 데이터 수
     * @param keyword
     * @return
     */
    public int findItemListTotalCount (String keyword, String group){

        Map<String, Object> map = new HashMap<>();
        map.put("keyword", keyword);
        map.put("group", group);

        return subOneRepository.findItemListTotalCount(map);
    }

    /**
     * Sub1 데이터 조회
     * @param type
     * @param keyword
     * @param page
     * @param size
     * @return
     */
    public List<SubDto.SubOneItemDto> findItemList (String type, String keyword, String group, int page, int size){

        List<SubDto.SubOneItemDto> list = new ArrayList<>();

        List<String> orderByList = new ArrayList<>();

        if(ApiConstants.SEARCH_ORDER_BY_RANK.equals(type)){
            orderByList.add(new StringBuilder().append("a.rank").append(" desc").toString());
        }else if(ApiConstants.SEARCH_ORDER_BY_REG.equals(type)){
            orderByList.add(new StringBuilder().append("a.reg_datetime").append(" desc").toString());
        }else{
            orderByList.add(new StringBuilder().append("a.rank").append(" asc").toString());
        }

        Map<String, Object> map = new HashMap<>();
        map.put("keyword", keyword);
        map.put("group", group);
        map.put("offset", (page * size));
        map.put("size", size);
        map.put("orderByList", orderByList);

        Optional<List<AwardModel>> awardModelListOptional = Optional.ofNullable(subOneRepository.findItemList(map));

        if(awardModelListOptional.isPresent()){

            List<AwardModel> awardModelList = awardModelListOptional.get();

            for (AwardModel awardModel : awardModelList){

                SubDto.SubOneItemDto dto = new SubDto.SubOneItemDto();
                dto.setAwardNo(awardModel.getAwardNo());
                dto.setAwardName(awardModel.getAwardName());
                dto.setUrl(awardModel.getUrl());
                dto.setRank(awardModel.getRank());
                dto.setDataStatus(awardModel.getDataStatus().toStr());
                dto.setRegDatetime(awardModel.getRegDatetime());
                dto.setAwardType(awardModel.getAwardType());
                dto.setAwardTypeName(awardModel.getName());

                list.add(dto);
            }
        }

        return list;
    }

    public List<SubDto.SubOneTypeDto> findItemGroupList () {

        List<SubDto.SubOneTypeDto> list = new ArrayList<>();

        List<String> orderByList = new ArrayList<>();
        orderByList.add(new StringBuilder().append("rank").append(" asc").toString());

        Map<String, Object> map = new HashMap<>();
        map.put("orderByList", orderByList);

        Optional<List<AwardTypeModel>> awardTypeModelListOptional = Optional.ofNullable(subOneRepository.findItemGroupList(map));
        if(awardTypeModelListOptional.isPresent()) {

            List<AwardTypeModel> awardTypeModelList = awardTypeModelListOptional.get();
            for (AwardTypeModel model : awardTypeModelList) {

                SubDto.SubOneTypeDto dto = new SubDto.SubOneTypeDto();
                dto.setId(model.getId());
                dto.setName(model.getName());
                dto.setRank(model.getRank());
                dto.setDataStatus(model.getDataStatus().toStr());

                list.add(dto);
            }
        }

        return list;
    }

    /**
     * Sub1 Type TotalCount
     * @param keyword
     * @return
     */
    public int findTypeListTotalCount (final String keyword){

        Map<String, Object> map = new HashMap<>();
        map.put("keyword", keyword);

        return subOneRepository.findTypeListTotalCount(map);
    }

    /**
     * Sub1 Type list
     *
     * @param type
     * @param keyword
     * @param page
     * @param size
     * @return
     */
    public List<SubDto.SubOneTypeDto> findTypeList (String type, String keyword, int page, int size) {

        List<SubDto.SubOneTypeDto> list = new ArrayList<>();
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

        Optional<List<AwardTypeModel>> awardTypeModelListOptional = Optional.ofNullable(subOneRepository.findTypeList(map));

        if(awardTypeModelListOptional.isPresent()) {

            List<AwardTypeModel> awardTypeModelList = awardTypeModelListOptional.get();

            for (AwardTypeModel model : awardTypeModelList) {
                SubDto.SubOneTypeDto dto = new SubDto.SubOneTypeDto();
                dto.setId(model.getId());
                dto.setName(model.getName());
                dto.setRank(model.getRank());
                dto.setDataStatus(model.getDataStatus().toStr());
                list.add(dto);
            }

        }

        return list;
    }

    /**
     * Type 정보 수정
     *
     * @param dto
     */
    @Transactional
    public void updateType (SubDto.SubOneTypeUpdReqDto dto){

        if(dto.getRank() == null){
            throw new ManagedException(ManagedExceptionCode.InvalidRank, ApiConstants.INVALID_RANK);
        }

        // 수정 대상 항목 조회
        Map<String, Object> map = new HashMap<>();
        map.put("id", dto.getId());
        Optional<AwardTypeModel> awardTypeModelOptional = Optional.ofNullable(subOneRepository.findType(map));

        if(awardTypeModelOptional.isPresent()){

            AwardTypeModel awardTypeModel = awardTypeModelOptional.get();
            // 변경하려는 순위
            int rank = dto.getRank();
            map = new HashMap<>();

            if(rank == awardTypeModel.getRank()){
                // 순번이 같음
                map.put("id", dto.getId());
                map.put("name", dto.getName());
                map.put("rank", rank);
                map.put("dataStatus", dto.getDataStatus());

                subOneRepository.updateType(map);

            }else{
                // 기존 순번 보다 크거나 작음
                // 전체 리스트 가져오기
                List<String> orderByList = new ArrayList<>();
                orderByList.add(new StringBuilder().append("rank").append(" asc").toString());

                Map<String, Object> searchMap = new HashMap<>();
                searchMap.put("orderByList", orderByList);
                Optional<List<AwardTypeModel>> awardTypeModelListOptional  = Optional.of(subOneRepository.findTypeList(searchMap));

                List<AwardTypeModel> awardTypeModelList = awardTypeModelListOptional.get();

                // 변경하려는 순위 유효성 검사
                if(rank > awardTypeModelList.size()){
                    throw new ManagedException(ManagedExceptionCode.InvalidRank, ApiConstants.INVALID_RANK);
                }

                if(awardTypeModel.getRank() > rank){
                    // 기존 순번보다 높은 순번일때  ex) 9 -> 2
                    for(AwardTypeModel model : awardTypeModelList){
                        // 바꾸려는 순번부터 하나씩 뒤로 밀리도록 수정
                        if(model.getRank() >= rank && model.getRank() < awardTypeModel.getRank()){
                            Map<String, Object> upMap = new HashMap<>();
                            upMap.put("id", model.getId());
                            upMap.put("rank", (model.getRank()+1));
                            subOneRepository.updateType(upMap);
                        }
                    }
                }else{
                    // 기존 순번보다 낮은 순번일때  ex) 2 -> 9
                    for(AwardTypeModel model : awardTypeModelList){

                        if(model.getRank() > awardTypeModel.getRank()  && model.getRank() <= rank){

                            Map<String, Object> upMap = new HashMap<>();
                            upMap.put("id", model.getId());
                            upMap.put("rank", (model.getRank()-1));
                            subOneRepository.updateType(upMap);

                        }
                    }
                }

                // 수정
                map.put("id", dto.getId());
                map.put("name", dto.getName());
                map.put("rank", rank);
                map.put("dataStatus", dto.getDataStatus());

                subOneRepository.updateType(map);
            }

        }else{
            throw new ManagedException(ManagedExceptionCode.UpdateError, ApiConstants.UPDATE_ERROR);
        }
    }

}
