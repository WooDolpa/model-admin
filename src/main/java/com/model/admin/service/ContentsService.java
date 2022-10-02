package com.model.admin.service;

import com.model.admin.dto.MainDto;
import com.model.admin.dto.UserDto;
import com.model.admin.repository.ContentsRepository;
import com.model.common.model.ContentsModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * packageName : com.model.admin.service
 * className : ContentService
 * user : jwlee
 * date : 2022/10/02
 */
@Service
@RequiredArgsConstructor
public class ContentsService {

    private final ContentsRepository contentsRepository;

    /**
     * 컨텐츠 수정
     *
     * @param dto
     * @return
     */
    @Transactional
    public int updateContents (final MainDto.UpdateContentsDto dto){

        Map<String, Object> map = new HashMap<>();
        map.put("ctNo", dto.getCtNo());
        map.put("ctTxt", dto.getCtTxt());

        return contentsRepository.updateContents(map);
    }

    /**
     * 컨텐츠 조회
     *
     * @return
     */
    public List<UserDto.ResContentsDto> findContents() {

        List<UserDto.ResContentsDto> list = new ArrayList<>();

        Optional<ContentsModel> contentsModelOptional = Optional.ofNullable(contentsRepository.findContents());

        if(contentsModelOptional.isPresent()){

            ContentsModel contentModel = contentsModelOptional.get();
            UserDto.ResContentsDto dto = new UserDto.ResContentsDto();

            dto.setCtNo(contentModel.getCtNo());
            dto.setCtTxt(contentModel.getCtTxt());
            dto.setRegDatetime(contentModel.getRegDatetime());
            dto.setUpdDatetime(contentModel.getRegDatetime());

            list.add(dto);
        }

        return  list;
    }

}
