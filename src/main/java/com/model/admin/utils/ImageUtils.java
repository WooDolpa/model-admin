package com.model.admin.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * packageName : com.model.admin.utils
 * className : ImageUtils
 * user : jwlee
 * date : 2022/10/02
 */
@Component
@Slf4j
public class ImageUtils {

    /**
     * 이미지 로컬 저장
     *
     * @param multipartFile
     * @param uri
     * @return
     */
    public String saveImage (final MultipartFile multipartFile, final int number, final String uri){

        String filePath = null;
        String fileName = null;

        try {

            fileName = multipartFile.getName()+"_"+number+".png";
            filePath = uri+"/"+fileName;

            File file = new File(filePath);
            multipartFile.transferTo(file);

        }catch (Exception e){
            log.warn("saveImage|Error|{}",e.getMessage());
        }finally {
            return fileName;
        }

    }

    public String changeImage (final MultipartFile multipartFile, final int number, final String url){

        String fileName = null;

        try {

            fileName = multipartFile.getName()+"_"+number+".png";
            String filePath = url+"/"+fileName;

            log.info("ImageUtils|changeImage|filePath:{}|fileName:{}",filePath, fileName);

            File file = new File(filePath);

            if(file.exists()){
                if(file.delete()){

                    multipartFile.transferTo(file);

                }else{
                    log.info("ImageUtils|changeImage|파일 삭제 실패");
                }
            }else{
                log.info("ImageUtils|changeImage|파일 존재하지 않음");
            }

        }catch (Exception e){
            log.warn("ImageUtils|changeImage|Error|{}",e.getMessage());
        }finally {
            return fileName;
        }

    }

    public boolean deleteImage (final String imgPath, final String url){

        boolean deleteStatus = false;

        try {

            String[] imgPaths = imgPath.split("/");
            String fileName = imgPaths[imgPaths.length-1];
            String filePath = url+"/"+fileName;

            log.info("ImageUtils|deleteImage|fileName:{}|filePath:{}",fileName, filePath);

            File file = new File(filePath);

            if(file.exists()){
                if(file.delete()){
                    deleteStatus = true;
                }else{
                    log.info("ImageUtils|deleteImage|File|Delete|Fail");
                }
            }else{
                log.info("ImageUtils|deleteImage|File|NoExist");
            }

        }catch (Exception e){
            log.warn("ImageUtils|deleteImage|Error|{}",e.getMessage());
        }finally {
            return deleteStatus;
        }

    }

}
