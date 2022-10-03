package com.model.admin.controller;

import com.model.admin.dto.ApiResponseDto;
import com.model.admin.dto.SubDto;
import com.model.admin.service.SubOneService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * packageName : com.model.admin.controller
 * className : SubOneController
 * user : jwlee
 * date : 2022/10/02
 */
@RestController
@RequestMapping(path = "/admin/api/v1/sub1")
@RequiredArgsConstructor
@Slf4j
public class SubOneController {

    private final SubOneService subOneService;

    /**
     * sub1 조회
     * @param request
     * @param type
     * @param keyword
     * @param page
     * @param size
     * @return
     */
    @GetMapping(path = "item/list")
    public ResponseEntity findItemList (HttpServletRequest request,
                                        @RequestParam(value = "type") final String type,
                                        @RequestParam(value = "group") final String group,
                                        @RequestParam(value = "keyword") final String keyword,
                                        @RequestParam(value = "page", defaultValue = "0") final Integer page,
                                        @RequestParam(value = "size", defaultValue = "10") final Integer size){

        Map<String, Object> resMap = new HashMap<>();
        int totalCount = subOneService.findItemListTotalCount(keyword, group);

        if(totalCount > 0){

            List<SubDto.SubOneItemDto> dtoList = subOneService.findItemList(type, keyword, group, page, size);

            resMap.put("totalCount", totalCount);
            resMap.put("list", dtoList);
        }else {
            resMap.put("totalCount", totalCount);
        }

        return new ResponseEntity(ApiResponseDto.makeResponse(resMap), HttpStatus.OK);
    }

    @GetMapping(path = "/group/list")
    public ResponseEntity findGroupList(HttpServletRequest request) {

        Map<String, Object> map = new HashMap<>();
        List<SubDto.SubOneTypeDto> list =  subOneService.findItemGroupList();
        map.put("totalCount", list.size());
        if(list.size() > 0) {
            map.put("list", list);
        }

        return new ResponseEntity(ApiResponseDto.makeResponse(map), HttpStatus.OK);
    }

    /**
     * sub1 등록
     * @param request
     * @param dto
     * @return
     */
    @PostMapping(path = "item/reg")
    public ResponseEntity itemReg (HttpServletRequest request,
                                   @RequestBody @Valid SubDto.SubOneRegReqDto dto){

        int result = subOneService.insertItem(dto);

        if(result > 0){
            return new ResponseEntity(ApiResponseDto.makeSuccessResponse(), HttpStatus.OK);
        }else{
            return new ResponseEntity(ApiResponseDto.makeFailResponse(), HttpStatus.OK);
        }
    }

    /**
     * sub1 수정
     * @param request
     * @param dto
     * @return
     */
    @PostMapping(path = "item/update")
    public ResponseEntity itemUpdate (HttpServletRequest request,
                                      @RequestBody @Valid SubDto.SubOneUpdReqDto dto){

        subOneService.updateItem(dto);

        return new ResponseEntity(ApiResponseDto.makeSuccessResponse(), HttpStatus.OK);
    }

    /**
     *sub1 삭제
     * @param request
     * @param dto
     * @return
     */
    @PostMapping(path = "item/delete")
    public ResponseEntity itemDelete (HttpServletRequest request,
                                      @RequestBody @Valid SubDto.SubOneDelReqDto dto){

        subOneService.deleteItem(dto);

        return new ResponseEntity(ApiResponseDto.makeSuccessResponse(), HttpStatus.OK);
    }

    /**
     * Sub1 type list
     *
     * @param request
     * @param type
     * @param keyword
     * @param page
     * @param size
     * @return
     */
    @GetMapping(path = "type/list")
    public ResponseEntity findTypeList (HttpServletRequest request,
                                        @RequestParam(value = "type") final String type,
                                        @RequestParam(value = "keyword") final String keyword,
                                        @RequestParam(value = "page", defaultValue = "0") final Integer page,
                                        @RequestParam(value = "size", defaultValue = "10") final Integer size){

        Map<String, Object> resMap = new HashMap<>();
        int totalCount = subOneService.findTypeListTotalCount(keyword);

        if(totalCount > 0){

            List<SubDto.SubOneTypeDto> list = subOneService.findTypeList(type, keyword, page, size);

            resMap.put("totalCount", totalCount);
            resMap.put("list", list);
        }else {
            resMap.put("totalCount", totalCount);
        }

        return new ResponseEntity(ApiResponseDto.makeResponse(resMap), HttpStatus.OK);
    }


    /**
     * sub1 type 수정
     * @param request
     * @param dto
     * @return
     */
    @PostMapping(path = "type/update")
    public ResponseEntity typeUpdate (HttpServletRequest request,
                                      @RequestBody @Valid SubDto.SubOneTypeUpdReqDto dto){

        subOneService.updateType(dto);
        return new ResponseEntity(ApiResponseDto.makeSuccessResponse(), HttpStatus.OK);
    }

}
