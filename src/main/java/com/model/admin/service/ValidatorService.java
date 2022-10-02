package com.model.admin.service;

import com.model.admin.constatns.ApiConstants;
import com.model.admin.dto.ApiResponseDto;
import com.model.admin.exception.ManagedException;
import com.model.admin.exception.ManagedExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Optional;

/**
 * packageName : com.model.admin.service
 * className : ValidatorService
 * user : jwlee
 * date : 2022/10/02
 */
@Service
@Slf4j
public class ValidatorService {

    /**
     * Controller 통해 전달되는 @RequestBody 파라미터의 유효성 검사 후 에러 발생시 BindingResult에 실패 내용을 포함되고 실패 내용을 응답으로 전달.
     * @param errors
     * @return
     */
    public Optional<ResponseEntity> validateParameter(BindingResult errors) {

        if(errors.hasErrors()){

            ManagedException me  = null;

            for(FieldError error : errors.getFieldErrors()){

                switch (error.getField()){
                    case ApiConstants.INVALID_FIELD_ID:
                        me = new ManagedException(ManagedExceptionCode.InvalidId, ApiConstants.INVALID_ID);
                        break;
                    case ApiConstants.INVALID_FIELD_PASSWORD:
                        me = new ManagedException(ManagedExceptionCode.InvalidPassword, ApiConstants.INVALID_PASSWORD);
                        break;
                    case ApiConstants.INVALID_FIELD_NAME:
                        me = new ManagedException(ManagedExceptionCode.InvalidName, ApiConstants.INVALID_NAME);
                        break;
                    case ApiConstants.INVALID_FIELD_EMAIL:
                        me = new ManagedException(ManagedExceptionCode.InvalidEmail, ApiConstants.INVALID_EMAIL);
                        break;
                    case ApiConstants.INVALID_FIELD_SLIDER_NO:
                        me = new ManagedException(ManagedExceptionCode.InvalidSliderNo, ApiConstants.INVALID_SLIDER_NO);
                        break;
                    case ApiConstants.INVALID_FIELD_CT_NO:
                        me = new ManagedException(ManagedExceptionCode.InvalidCtNo, ApiConstants.INVALID_CT_NO);
                        break;
                    case  ApiConstants.INVALID_FIELD_CT_TXT:
                        me = new ManagedException(ManagedExceptionCode.InvalidCtTxt, ApiConstants.INVALID_CT_TXT);
                        break;
                    case  ApiConstants.INVALID_FIELD_TXT_NO:
                        me = new ManagedException(ManagedExceptionCode.InvalidTxtNo, ApiConstants.INVALID__TXT_NO);
                        break;
                    case  ApiConstants.INVALID_FIELD_TXT_CONTENTS:
                        me = new ManagedException(ManagedExceptionCode.InvalidTxtContents, ApiConstants.INVALID_TXT_CONTENTS);
                        break;
                    default:
                        break;
                }
            }

            if(me == null){
                log.warn("validateParameter|unknown field");
                me = new ManagedException(ManagedExceptionCode.ServerError, ApiConstants.SERVER_ERROR);
            }

            return Optional.of(new ResponseEntity(ApiResponseDto.makeErrorResponse(me), HttpStatus.BAD_REQUEST));
        }

        return Optional.empty();
    }

}
