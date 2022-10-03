package com.model.admin.controller;

import com.model.admin.constatns.ApiConstants;
import com.model.admin.dto.ApiResponseDto;
import com.model.admin.dto.UserDto;
import com.model.admin.exception.ManagedException;
import com.model.admin.exception.ManagedExceptionCode;
import com.model.admin.service.UserService;
import com.model.admin.service.ValidatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

/**
 * packageName : com.model.admin.controller
 * className : UserController
 * user : jwlee
 * date : 2022/10/02
 */
@RestController
@RequestMapping(path = "/admin/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final ValidatorService validatorService;

    /**
     * 관리자 조회
     * @param request
     * @param token
     * @return
     */
    @GetMapping(path = "/findUser")
    public ResponseEntity findUser (HttpServletRequest request,
                                    @RequestParam(value = "token", defaultValue = "")final String token){

        //token 값 조회
        log.info("token|{}",token);
        String[] tokenInfo = token.split("_");
        if(tokenInfo.length != 3){
            throw new ManagedException(ManagedExceptionCode.InvalidToken, ApiConstants.INVALID_TOKEN);
        }

        if(!StringUtils.hasLength(tokenInfo[1])){
            throw new ManagedException(ManagedExceptionCode.InvalidAdminNo, ApiConstants.INVALID_ADMIN_NO);
        }

        int adminNo = Integer.parseInt(tokenInfo[1]);

        UserDto.ResUserDto resUserDto = userService.findUser(adminNo);

        return new ResponseEntity(ApiResponseDto.makeResponse(resUserDto), HttpStatus.OK);
    }

    /**
     * 비밀번호 수정
     * @param request
     * @param dto
     * @return
     */
    @PostMapping(path = "/update/password")
    public ResponseEntity updatePassword (HttpServletRequest request,
                                          @RequestBody @Valid UserDto.ReqUserPasswordDto dto){

        int result = this.userService.updatePassword(dto);

        if(result > 0){
            return new ResponseEntity(ApiResponseDto.makeSuccessResponse(), HttpStatus.OK);
        }else{
            return new ResponseEntity(ApiResponseDto.makeFailResponse(), HttpStatus.OK);
        }
    }

    @PostMapping(path = "/update/user")
    public ResponseEntity updateUser (HttpServletRequest request,
                                      @RequestBody @Valid UserDto.ReqUserDto dto,
                                      BindingResult errors){

        Optional<ResponseEntity> responseEntityOptional =  validatorService.validateParameter(errors);

        if(responseEntityOptional.isPresent()){
            return responseEntityOptional.get();
        }

        int result = userService.updateUser(dto);

        if(result > 0){
            return new ResponseEntity(ApiResponseDto.makeSuccessResponse(), HttpStatus.OK);
        }else{
            return new ResponseEntity(ApiResponseDto.makeFailResponse(), HttpStatus.OK);
        }
    }

}
