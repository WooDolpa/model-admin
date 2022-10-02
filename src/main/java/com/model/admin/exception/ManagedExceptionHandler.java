package com.model.admin.exception;

import com.model.admin.constatns.ApiConstants;
import com.model.admin.dto.ApiResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

/**
 * packageName : com.model.admin.exception
 * className : ManagedExceptionHandler
 * user : jwlee
 * date : 2022/10/02
 */
@ControllerAdvice(annotations = {RestController.class})
@Slf4j
public class ManagedExceptionHandler {

    /**
     * It manages all exceptions accordingly
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class )
    public ResponseEntity managedExceptionHandler (Exception e){

        ManagedException me;
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        log.info("managedExceptionHandler|called|{}",e.getMessage());

        if(e instanceof ManagedException){
            me = (ManagedException) e;
        }else if(e instanceof HttpMessageNotReadableException){
            me = new ManagedException(ManagedExceptionCode.InvalidParameter, ApiConstants.INVALID_PARAMETER);
        }else {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            me = new ManagedException(ManagedExceptionCode.ServerError, ApiConstants.SERVER_ERROR); // code : 500 || msg : 서버에러
        }

        String result = ApiResponseDto.makeErrorResponse(me);

        return new ResponseEntity(result, httpStatus);
    }

}
