package com.model.admin.service;

import com.model.admin.dto.MainDto;
import com.model.admin.repository.NavigationRepository;
import com.model.common.model.NavigationModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * packageName : com.model.admin.service
 * className : NavigationService
 * user : jwlee
 * date : 2022/10/02
 */
@Service
@RequiredArgsConstructor
public class NavigationService {

    private final NavigationRepository navigationRepository;

    /**
     * 네비게이션 조회
     * @return
     */
    public List<MainDto.NavigationDto> findNavigationList(final String dataStatus){

        List<MainDto.NavigationDto> list = new ArrayList<>();

        List<String> orderByList = new ArrayList<>();
        orderByList.add(new StringBuilder().append("sort").append(" asc").toString());

        Map<String, Object> map = new HashMap<>();
        map.put("dataStatus", dataStatus);
        map.put("orderByList",orderByList);

        Optional<List<NavigationModel>> modelListOptional = Optional.ofNullable(navigationRepository.findNavigationList(map));

        if(modelListOptional.isPresent()){

            List<NavigationModel> modelList = modelListOptional.get();
            for(NavigationModel model : modelList){

                MainDto.NavigationDto dto = new MainDto.NavigationDto();
                dto.setNaviNo(model.getNavNo());
                dto.setTitle(model.getTitle());
                dto.setLink(model.getLink());
                dto.setSort(model.getSort());
                dto.setDataStatus(model.getDataStatus().toStr());
                dto.setRegDatetime(model.getRegDatetime());
                dto.setUpdDatetime(model.getUpdDatetime());

                list.add(dto);
            }
        }

        return list;
    }

    /**
     * 네비게이션 정보 수정
     * @param dto
     * @return
     */
    @Transactional
    public int updateNavigation (MainDto.NavigationDto dto){

        Map<String, Object> map = new HashMap<>();
        map.put("navNo", dto.getNaviNo());
        map.put("title", dto.getTitle());
        map.put("dataStatus", dto.getDataStatus());

        return navigationRepository.updateNavigation(map);
    }

}
