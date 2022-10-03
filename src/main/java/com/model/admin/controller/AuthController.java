package com.model.admin.controller;

import com.model.admin.constatns.ApiConstants;
import com.model.admin.dto.ApiResponseDto;
import com.model.admin.dto.AuthDto;
import com.model.admin.exception.ManagedException;
import com.model.admin.exception.ManagedExceptionCode;
import com.model.admin.service.AuthService;
import com.model.admin.service.MailService;
import com.model.admin.service.ValidatorService;
import com.model.common.model.AdminModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

/**
 * packageName : com.model.admin.controller
 * className : AuthController
 * user : jwlee
 * date : 2022/10/02
 */
@RestController
@RequestMapping(path = "/admin/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final ValidatorService validatorService;
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final MailService mailService;

    @PostMapping(path = "/login")
    public ResponseEntity login(HttpServletRequest request,
                                @RequestBody @Valid AuthDto.LoginDto dto,
                                BindingResult errors) {

        Optional<ResponseEntity> responseEntityOptional = validatorService.validateParameter(errors);

        //파라미터 유효성 실패
        if(responseEntityOptional.isPresent()){
            return responseEntityOptional.get();
        }

        Map<String, Object> resultMap = new HashMap<>();

        // 계정 체크
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(dto.getId().trim(),dto.getPassword().trim());
        Authentication authentication = authenticationManager.authenticate(token);

        // 계정 정보 조회
        Optional<AdminModel> adminModelOptional = authService.findAdmin(dto.getId());

        // 계정이 존재하면 토큰 생성
        if(adminModelOptional.isPresent()){

            AdminModel adminModel = adminModelOptional.get();

            //Token 생성
            String admin_token = null;

            Random random = new Random();
            StringBuffer sbf = new StringBuffer();

            for(int i=0; i<8; i++){
                if(random.nextBoolean()){
                    sbf.append((char)(int)(random.nextInt(26)+97));
                }else{
                    sbf.append(random.nextInt(10));
                }
            }

            admin_token = "ADMIN_"+adminModel.getAdminNo()+"_"+sbf.toString();

            resultMap.put("token", admin_token);
            resultMap.put("name", adminModel.getName());
        }

        return new ResponseEntity(ApiResponseDto.makeResponse(resultMap), HttpStatus.OK);

    }

    /**
     * 로그아웃
     * @param request
     * @return
     */
    @DeleteMapping(path = "/logout")
    public ResponseEntity logout(HttpServletRequest request){

        String token = null;
        token = request.getHeader(ApiConstants.AUTHORIZATION);

        if(StringUtils.isEmpty(token)){
            throw new ManagedException(ManagedExceptionCode.ExpiredToken,ApiConstants.EXPIRED_TOKEN);
        }

        return new ResponseEntity(ApiResponseDto.makeSuccessResponse(),HttpStatus.OK);
    }

    /**
     * 임시 비밀번호 이메일 전송
     *
     * @param request
     * @return
     */
    @GetMapping(path = "/temp/password")
    public ResponseEntity tempPassword(HttpServletRequest request){

        mailService.sendMail();

        return new ResponseEntity(ApiResponseDto.makeSuccessResponse(), HttpStatus.OK);
    }

}
