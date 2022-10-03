package com.model.admin.controller;

import com.model.admin.constatns.ApiConstants;
import com.model.admin.dto.ApiResponseDto;
import com.model.admin.dto.MainDto;
import com.model.admin.dto.UserDto;
import com.model.admin.exception.ManagedException;
import com.model.admin.exception.ManagedExceptionCode;
import com.model.admin.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * packageName : com.model.admin.controller
 * className : MainController
 * user : jwlee
 * date : 2022/10/02
 */
@RestController
@RequestMapping(path = "/admin/api/v1/main")
@RequiredArgsConstructor
@Slf4j
public class MainController {

    private final CompanyService companyService;
    private final NavigationService navigationService;
    private final SliderService sliderService;
    private final ValidatorService validatorService;
    private final ContentsService contentsService;

    /**
     * 회사 정보 조회
     * @param request
     * @return
     */
    @GetMapping(path = "/getCompany")
    public ResponseEntity getCompany (HttpServletRequest request){

        List<MainDto.CompanyDto> list = companyService.findCompany();

        return new ResponseEntity(ApiResponseDto.makeResponse(list), HttpStatus.OK);
    }

    /**
     * 회사 정보 수정
     *
     * @param request
     * @param cpNo
     * @param cpName
     * @param imgFile
     * @return
     */
    @PostMapping(path = "updateCompany", produces = "application/json")
    public ResponseEntity updateCompany (HttpServletRequest request,
                                         @RequestParam(value = "cpNo") final Integer cpNo,
                                         @RequestParam(value = "cpName") final String cpName,
                                         @RequestParam(value = "company_img", required = false) MultipartFile imgFile){

        int result = companyService.updateCompany(cpNo, cpName, imgFile);

        if(result > 0){
            return new ResponseEntity(ApiResponseDto.makeSuccessResponse(), HttpStatus.OK);
        }else{
            return new ResponseEntity(ApiResponseDto.makeFailResponse(), HttpStatus.OK);
        }
    }

    /**
     * 네비게이션 조회
     * @param request
     * @return
     */
    @GetMapping(path = "getNavigationList")
    public ResponseEntity getNavigationList (HttpServletRequest request,
                                             @RequestParam(value = "dataStatus", required = false) final String dataStatus){

        List<MainDto.NavigationDto> list = navigationService.findNavigationList(dataStatus);

        return new ResponseEntity(ApiResponseDto.makeResponse(list), HttpStatus.OK);
    }

    /**
     * 네비게이션 수정
     * @param request
     * @param dto
     * @return
     */
    @PostMapping(path = "updateNavigation")
    public ResponseEntity updateNavigation (HttpServletRequest request,
                                            @RequestBody @Valid MainDto.NavigationDto dto){

        int result = navigationService.updateNavigation(dto);

        if(result > 0){
            return new ResponseEntity(ApiResponseDto.makeSuccessResponse(), HttpStatus.OK);
        }else{
            return new ResponseEntity(ApiResponseDto.makeFailResponse(), HttpStatus.OK);
        }
    }

    /**
     * 슬라이드 리스트 조회
     * @param request
     * @param dataStatus
     * @param keyword
     * @param offset
     * @param size
     * @return
     */
    @GetMapping(path = "/slider/list")
    public ResponseEntity sliderList (HttpServletRequest request,
                                      @RequestParam(value = "dataStatus") final String dataStatus,
                                      @RequestParam(value = "keyword") final String keyword,
                                      @RequestParam(value = "index", defaultValue = "1") final Integer offset,
                                      @RequestParam(value = "size", defaultValue = "15") final Integer size){

        Map<String, Object> resMap = new HashMap<>();

        int totalCount = sliderService.findSliderListTotalCount(dataStatus, keyword);
        resMap.put("totalCount", totalCount);

        if(totalCount > 0){
            List<MainDto.SliderDto> sliderList = sliderService.findSliderList(dataStatus, keyword, offset, size);
            resMap.put("list", sliderList);
        }

        return new ResponseEntity(ApiResponseDto.makeResponse(resMap), HttpStatus.OK);
    }

    /**
     * 슬라이드 등록
     * @param request
     * @param title
     * @param content
     * @param dataStatus
     * @param imgFile
     * @return
     */
    @PostMapping(path = "/slider/add", produces = "application/json")
    public ResponseEntity insertSlider (HttpServletRequest request,
                                        @RequestParam(value = "title") final String title,
                                        @RequestParam(value = "content") final String content,
                                        @RequestParam(value = "dataStatus") final String dataStatus,
                                        @RequestParam(value = "slider_img", required = false) MultipartFile imgFile){

        // 유효성 검사 체크
        if(!StringUtils.hasLength(title)){
            throw new ManagedException(ManagedExceptionCode.InvalidSliderTitle, ApiConstants.INVALID_SLIDER_TITLE);
        }

        if (!StringUtils.hasLength(content)){
            throw new ManagedException(ManagedExceptionCode.InvalidSliderContent, ApiConstants.INVALID_SLIDER_CONTENT);
        }

        if(!StringUtils.hasLength(dataStatus)){
            throw new ManagedException(ManagedExceptionCode.InvalidSliderDataStatus, ApiConstants.INVALID_SLIDER_DATASTATUS);
        }

        if(imgFile == null){
            throw new ManagedException(ManagedExceptionCode.InvalidSliderImage, ApiConstants.INVALID_SLIDER_IMAGE);
        }

        int result = sliderService.insertSlider(title, content, dataStatus, imgFile);

        if(result > 0){
            return new ResponseEntity(ApiResponseDto.makeSuccessResponse(), HttpStatus.OK);
        }else{
            return new ResponseEntity(ApiResponseDto.makeFailResponse(), HttpStatus.OK);
        }
    }

    /**
     * 슬라이드 수정
     * @param request
     * @param sliderNo
     * @param title
     * @param content
     * @param sort
     * @param dataStatus
     * @param imgFile
     * @return
     */
    @PostMapping(path = "/slider/update", produces = "application/json")
    public ResponseEntity updateSlider (HttpServletRequest request,
                                        @RequestParam(value = "sliderNo") final Integer sliderNo,
                                        @RequestParam(value = "title") final String title,
                                        @RequestParam(value = "content") final String content,
                                        @RequestParam(value = "sort") final String sort,
                                        @RequestParam(value = "dataStatus") final String dataStatus,
                                        @RequestParam(value = "slider_img", required = false) MultipartFile imgFile){


        // 유효성 검사 체크
        if(sliderNo == null || sliderNo == 0){
            throw new ManagedException(ManagedExceptionCode.InvalidSliderNo, ApiConstants.INVALID_SLIDER_NO);
        }

        if(!StringUtils.hasLength(title)){
            throw new ManagedException(ManagedExceptionCode.InvalidSliderTitle, ApiConstants.INVALID_SLIDER_TITLE);
        }

        if(!StringUtils.hasLength(content)){
            throw new ManagedException(ManagedExceptionCode.InvalidSliderContent, ApiConstants.INVALID_SLIDER_CONTENT);
        }

        if(!StringUtils.hasLength(sort)){
            throw new ManagedException(ManagedExceptionCode.InvalidRank, ApiConstants.INVALID_RANK);
        }

        if(!StringUtils.hasLength(dataStatus)){
            throw new ManagedException(ManagedExceptionCode.InvalidSliderDataStatus, ApiConstants.INVALID_SLIDER_DATASTATUS);
        }

        int totalCount = sliderService.findSliderListTotalCount(null, null);
        int rank = Integer.parseInt(sort);

        if(rank > totalCount){
            throw new ManagedException(ManagedExceptionCode.InvalidRankSize, ApiConstants.INVALID_RANK_SIZE);
        }

        int result = sliderService.updateSlider(sliderNo, title, content, sort, dataStatus, imgFile);

        if(result > 0){
            return new ResponseEntity(ApiResponseDto.makeSuccessResponse(), HttpStatus.OK);
        }else{
            return new ResponseEntity(ApiResponseDto.makeFailResponse(), HttpStatus.OK);
        }
    }

    /**
     * 슬라이드 삭제
     * @param request
     * @param dto
     * @param errors
     * @return
     */
    @PostMapping(path = "/slider/delete")
    public ResponseEntity deleteSlider (HttpServletRequest request,
                                        @RequestBody @Valid MainDto.DeleteSliderDto dto,
                                        BindingResult errors){

        Optional<ResponseEntity> responseEntityOptional = this.validatorService.validateParameter(errors);

        //파라미터 유효성 실패
        if(responseEntityOptional.isPresent()){
            return responseEntityOptional.get();
        }

        int result = sliderService.deleteSlider(dto);

        if(result > 0){
            return new ResponseEntity(ApiResponseDto.makeSuccessResponse(), HttpStatus.OK);
        }else{
            return new ResponseEntity(ApiResponseDto.makeFailResponse(), HttpStatus.OK);
        }
    }

    /**
     * 컨텐츠 조회
     *
     * @param request
     * @return
     */
    @GetMapping(path = "/contents")
    public ResponseEntity findContents (HttpServletRequest request){

        Map<String, Object> resMap = new HashMap<>();
        List<UserDto.ResContentsDto> list = contentsService.findContents();

        resMap.put("totalCount", list.size());
        resMap.put("data", list);

        return new ResponseEntity(ApiResponseDto.makeResponse(resMap), HttpStatus.OK);
    }

    /**
     * 컨텐츠 수정
     *
     * @param request
     * @param dto
     * @param errors
     * @return
     */
    @PostMapping(path = "update/contents")
    public ResponseEntity updateContents (HttpServletRequest request,
                                          @RequestBody @Valid MainDto.UpdateContentsDto dto,
                                          BindingResult errors){

        Optional<ResponseEntity> responseEntityOptional = this.validatorService.validateParameter(errors);

        //파라미터 유효성 실패
        if(responseEntityOptional.isPresent()){
            return responseEntityOptional.get();
        }

        int result = contentsService.updateContents(dto);

        if(result > 0){
            return new ResponseEntity(ApiResponseDto.makeSuccessResponse(), HttpStatus.OK);
        }else{
            return new ResponseEntity(ApiResponseDto.makeFailResponse(), HttpStatus.OK);
        }
    }
    
}
