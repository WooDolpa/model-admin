package com.model.admin.service;

import com.model.admin.constatns.ApiConstants;
import com.model.admin.exception.ManagedException;
import com.model.admin.exception.ManagedExceptionCode;
import com.model.admin.repository.AuthRepository;
import com.model.common.dto.MailDto;
import com.model.common.model.AdminModel;
import com.model.common.util.KeyGenerator;
import com.model.common.util.MailUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * packageName : com.model.admin.service
 * className : MailService
 * user : jwlee
 * date : 2022/10/02
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final AuthRepository authRepository;
    private final UserService userService;

    @Value("${smtp.mail.host}")
    private String host;
    @Value("${smtp.mail.port}")
    private int port;
    @Value("${smtp.mail.id}")
    private String id;
    @Value("${smtp.mail.password}")
    private String password;
    @Value("${smtp.mail.sender.address}")
    private String address;

    /**
     * 이메일 전송
     *
     */
    public void sendMail(){

        // 회사 정보 조회
        Map<String, Object> map = new HashMap<>();
        map.put("adminNo", ApiConstants.ADMIN_NO);
        Optional<AdminModel> adminModelOptional = Optional.ofNullable(authRepository.findAdmin(map));

        if(adminModelOptional.isPresent()){

            AdminModel adminModel = adminModelOptional.get();

            if(adminModel.getEmail() == null){
                throw new ManagedException(ManagedExceptionCode.InvalidEmail, ApiConstants.INVALID_EMAIL);
            }

            String tempPassword = KeyGenerator.createKey(10);

            // 비밀번호 변경
            int result = userService.updateUser(adminModel.getAdminNo(), tempPassword);

            if(result > 0){

                try {

                    MailDto.MailReqDto mailReqDto = new MailDto.MailReqDto();

                    mailReqDto.setHost(host);
                    mailReqDto.setPort(port);
                    mailReqDto.setId(id);
                    mailReqDto.setPassword(password);
                    mailReqDto.setFromAddress(address);
                    mailReqDto.setToAddress(adminModel.getEmail());
                    mailReqDto.setTitle(ApiConstants.MAIL_TEMP_PASSWORD_TITLE);
                    mailReqDto.setContent(MailUtils.getTempPasswordHtmlCode(tempPassword));

                    MailUtils.sendMail(mailReqDto);

                }catch (Exception e){
                    log.warn("MailService|sendMail|Error|{}", e.getMessage());
                    throw new ManagedException(ManagedExceptionCode.SendEmailError, ApiConstants.SEND_EMAIL_ERROR);
                }
            }


        }else{
            throw new ManagedException(ManagedExceptionCode.InvalidAdminNo, ApiConstants.INVALID_ADMIN_NO);
        }

    }

}
