package com.model.admin.service;

import com.model.admin.dto.MainDto;
import com.model.admin.repository.CompanyRepository;
import com.model.admin.utils.ImageUtils;
import com.model.common.model.CompanyModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * packageName : com.model.admin.service
 * className : CompanyService
 * user : jwlee
 * date : 2022/10/02
 */
@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final ImageUtils imageUtils;

    @Value("${img.company.directory.uri}")
    private String companyDirectoryUri;
    @Value("${img.company.uri}")
    private String companyUri;

    /**
     * 회사 정보 조회
     * @return
     */
    public List<MainDto.CompanyDto> findCompany (){

        List<MainDto.CompanyDto> list = new ArrayList<>();
        Optional<CompanyModel> companyModelOptional = Optional.ofNullable(companyRepository.findCompany());

        if(companyModelOptional.isPresent()){

            CompanyModel companyModel = companyModelOptional.get();
            MainDto.CompanyDto dto = new MainDto.CompanyDto();
            dto.setCpNo(companyModel.getCpNo());
            dto.setCpName(companyModel.getCpName());
            dto.setCpImgUrl(companyModel.getCpImgUrl());

            list.add(dto);
        }

        return list;
    }

    /**
     * 회사 정보 수정
     * @param cpNo
     * @param cpName
     * @return
     */
    @Transactional
    public int updateCompany (final int cpNo, final String cpName, final MultipartFile imgFile){

        // 1. 기존 정보 조회
        Optional<CompanyModel> companyModelOptional = Optional.ofNullable(companyRepository.findCompany());

        if(companyModelOptional.isPresent()){

            CompanyModel companyModel = companyModelOptional.get();

            String imgPath = null;
            int maxImgNum = 0;

            if(imgFile != null){
                // 2. 기존 이미지 삭제
                boolean deleteStatus = imageUtils.deleteImage(companyModel.getCpImgUrl(), companyDirectoryUri);
                // 3. 정상적으로 삭제가 되면 이미지 등록
                if(deleteStatus){
                    imgPath = imageUtils.saveImage(imgFile, maxImgNum, companyDirectoryUri);
                }
            }

            Map<String, Object> map = new HashMap<>();
            map.put("cpNo", cpNo);
            map.put("cpName", cpName);
            if(imgPath != null){
                map.put("cpImgUrl", companyUri+"/"+imgPath);
            }

            return companyRepository.updateCompany(map);

        }else{
            return 0;
        }
    }

}
