package com.model.admin.service;

import com.model.admin.dto.MainDto;
import com.model.admin.repository.SliderRepository;
import com.model.admin.utils.ImageUtils;
import com.model.common.model.SliderModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * packageName : com.model.admin.service
 * className : SliderService
 * user : jwlee
 * date : 2022/10/02
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SliderService {

    private final SliderRepository sliderRepository;
    private final ImageUtils imageUtils;

    @Value("${img.slider.directory.uri}")
    private String sliderDirectoryUri;
    @Value("${img.slider.uri}")
    private String sliderUri;

    /**
     * 슬라이드 등록
     * @param title
     * @param content
     * @param dataStatus
     * @param imgFile
     * @return
     */
    @Transactional
    public int insertSlider (final String title, final String content, final String dataStatus, final MultipartFile imgFile){

        int result = 0;

        try {

            // 마지막 이미지 순번 가져오기
            int maxImgNum = sliderRepository.findMaxImgNum();
            maxImgNum++;

            // 마지막 순번 가져오기
            int maxSort = sliderRepository.findMaxSort();
            maxSort++;

            // 이미지 저장
            String imgName = imageUtils.saveImage(imgFile, maxImgNum, sliderDirectoryUri);

            // 이미지가 저장되었다고 가정하고 진행
            Map<String, Object> map = new HashMap<>();
            map.put("title", title);
            map.put("content", content);
            map.put("imgUrl", sliderUri+"/"+imgName);
            map.put("sort", maxSort);
            map.put("imgNum", maxImgNum);
            map.put("dataStatus", dataStatus);

            result = sliderRepository.insertSlider(map);

        }catch (Exception e){
            log.warn("insertSlider|Error|{}",e.getMessage());
        }finally {
            return result;
        }
    }

    /**
     * 슬라이드 수정
     * @param sliderNo
     * @param title
     * @param content
     * @param sort
     * @param dataStatus
     * @param imgFile
     * @return
     */
    @Transactional
    public int updateSlider (final int sliderNo, final String title, final String content, final String sort, final String dataStatus, final MultipartFile imgFile){

        int result = 0;

        try {

            // 1. 수정 대상 기존 정보 조회
            Map<String, Object> map = new HashMap<>();
            map.put("sliderNo", sliderNo);
            Optional<SliderModel> sliderModelOptional = Optional.ofNullable(sliderRepository.findSlider(map));

            if(sliderModelOptional.isPresent()){

                SliderModel sliderModel = sliderModelOptional.get();

                // 2. 이미지 변경 여부
                String imgName = null;
                int maxImgNum = 0;

                if(imgFile != null){
                    // max 값 조회
                    maxImgNum = sliderRepository.findMaxImgNum();
                    maxImgNum++;

                    // 기존이미지 삭제
                    boolean deleteImgStatus = imageUtils.deleteImage(sliderModel.getImgUrl(), sliderDirectoryUri);
                    if(deleteImgStatus){
                        imgName = imageUtils.saveImage(imgFile, maxImgNum, sliderDirectoryUri);
                    }
                }

                // 순번 데이터 확인

                int rank = Integer.parseInt(sort);

                if(rank == sliderModel.getSort()){

                    // 순위 변동 없음 -> 바로 수정 처리
                    map = new HashMap<>();
                    map.put("sliderNo", sliderNo);
                    map.put("title", title);
                    map.put("content", content);
                    if(imgName != null){
                        map.put("imgUrl", sliderUri+"/"+imgName);
                    }
                    map.put("sort", rank);
                    if(maxImgNum > 0){
                        map.put("imgNum", maxImgNum);
                    }
                    map.put("dataStatus", dataStatus);


                    result = sliderRepository.updateSlider(map);

                }else{

                    // 기존 등록된 순번이랑 같이 다름
                    // 2. 전체 리스트 가져오기
                    map = new HashMap<>();

                    List<String> orderByList = new ArrayList<>();
                    orderByList.add(new StringBuilder().append("sort").append(" asc").toString());
                    map.put("orderByList", orderByList);

                    Optional<List<SliderModel>> sliderModelListOptional = Optional.ofNullable(sliderRepository.findSliderList(map));

                    if(sliderModelListOptional.isPresent()){

                        List<SliderModel> sliderModelList = sliderModelListOptional.get();

                        if(sliderModel.getSort() > rank){

                            for(SliderModel model : sliderModelList){
                                // 바꾸려는 순번부터 하나씩 뒤로 밀리도록 수정
                                if(model.getSort() >= rank && model.getSort() < sliderModel.getSort()){

                                    Map<String, Object> upMap = new HashMap<>();
                                    upMap.put("sliderNo", model.getSliderNo());
                                    upMap.put("sort", (model.getSort()+1));
                                    sliderRepository.updateSlider(upMap);

                                }
                            }

                        }else{

                            // 기존 순번보다 낮은 순번일때  ex) 2 -> 9
                            for(SliderModel model : sliderModelList){

                                if(model.getSort() > sliderModel.getSort()  && model.getSort() <= rank){

                                    Map<String, Object> upMap = new HashMap<>();
                                    upMap.put("sliderNo", model.getSliderNo());
                                    upMap.put("sort", (model.getSort()-1));
                                    sliderRepository.updateSlider(upMap);

                                }
                            }
                        }
                    }

                    map = new HashMap<>();
                    map.put("sliderNo", sliderNo);
                    map.put("title", title);
                    map.put("content", content);
                    if(imgName != null){
                        map.put("imgUrl", sliderUri+"/"+imgName);
                    }
                    map.put("sort", rank);
                    if(maxImgNum > 0){
                        map.put("imgNum", maxImgNum);
                    }
                    map.put("dataStatus", dataStatus);

                    result = sliderRepository.updateSlider(map);

                }
            }

        }catch (Exception e){
            log.warn("updateSlider|Error|{}",e.getMessage());
        }finally {
            return result;
        }

    }

    /**
     * 슬라이드 삭제
     * @param dto
     * @return
     */
    @Transactional
    public int deleteSlider (final MainDto.DeleteSliderDto dto){

        int result = 0;

        Map<String, Object> map = new HashMap<>();
        map.put("sliderNo", dto.getSliderNo());

        Optional<SliderModel> sliderModelOptional = Optional.ofNullable(sliderRepository.findSlider(map));

        if(sliderModelOptional.isPresent()){

            SliderModel sliderModel = sliderModelOptional.get();

            int sort = sliderModel.getSort();
            boolean deleteImgStatus = imageUtils.deleteImage(sliderModel.getImgUrl(), sliderDirectoryUri);

            if(deleteImgStatus){
                result = sliderRepository.deleteSlider(map);

                if(result > 0){
                    // 하위 순번 업데이트
                    map = new HashMap<>();
                    map.put("deleteSort", sort);

                    Optional<List<SliderModel>> sliderModelListOptional = Optional.ofNullable(sliderRepository.findSliderList(map));
                    List<SliderModel> sliderModelList = sliderModelListOptional.get();

                    for(SliderModel model : sliderModelList){

                        int updSort = model.getSort();
                        updSort--;

                        Map<String, Object> updMap = new HashMap<>();
                        updMap.put("sliderNo", model.getSliderNo());
                        updMap.put("sort", updSort);

                        sliderRepository.updateSlider(updMap);
                    }
                }
            }
        }

        return result;
    }

    /**
     * 슬라이드 조회 합
     * @param dataStatus
     * @param keyword
     * @return
     */
    public int findSliderListTotalCount(final String dataStatus, final String keyword){

        Map<String, Object> map = new HashMap<>();
        map.put("dataStatus", dataStatus);
        map.put("keyword", keyword);

        return sliderRepository.findSliderListTotalCount(map);
    }

    /**
     * Slider list 조회
     * @param dataStatus
     * @param keyword
     * @param offset
     * @param size
     * @return
     */
    public List<MainDto.SliderDto> findSliderList (final String dataStatus, final String keyword, final int offset, final int size){

        List<MainDto.SliderDto> sliderDtoList = new ArrayList<>();

        Map<String, Object> map = new HashMap<>();
        map.put("dataStatus", dataStatus);
        map.put("keyword", keyword);
        map.put("offset", (offset-1)*size);
        map.put("size", size);

        List<String> orderByList = new ArrayList<>();
        orderByList.add(new StringBuilder().append("sort").append(" asc").toString());
        map.put("orderByList", orderByList);

        Optional<List<SliderModel>> sliderModelListOptional = Optional.ofNullable(sliderRepository.findSliderList(map));

        if(sliderModelListOptional.isPresent()){

            List<SliderModel> sliderModelList = sliderModelListOptional.get();
            for (SliderModel sliderModel : sliderModelList){

                MainDto.SliderDto sliderDto = new MainDto.SliderDto();

                sliderDto.setSliderNo(sliderModel.getSliderNo());
                sliderDto.setTitle(sliderModel.getTitle());
                sliderDto.setContent(sliderModel.getContent());
                sliderDto.setImgUrl(sliderModel.getImgUrl());
                sliderDto.setSort(sliderModel.getSort());
                sliderDto.setDataStatus(sliderModel.getDataStatus().toStr());
                sliderDto.setRegDatetime(sliderModel.getRegDatetime());
                sliderDto.setUpdDatetime(sliderModel.getUpdDatetime());

                sliderDtoList.add(sliderDto);
            }

        }

        return sliderDtoList;
    }

}
