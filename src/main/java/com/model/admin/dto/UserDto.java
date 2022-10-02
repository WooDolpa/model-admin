package com.model.admin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * packageName : com.model.admin.dto
 * className : UserDto
 * user : jwlee
 * date : 2022/10/02
 */
public class UserDto {

    @Data
    public static class ResUserDto {

        private int adminNo;                    // 관리자 번호
        private String id;                      // 아이디
        private String password;                // 패스워드
        private String name;                    // 이름
        private String email;                   // 이메일
        private String regDatetime;             // 등록일
        private String updDatetime;             // 수정일

    }

    @Data
    @JsonInclude(NON_NULL)
    public static class ReqUserPasswordDto {

        @NotNull
        private Integer adminNo;
        @NotBlank
        private String password;
        @NotBlank
        private String newPassword;
        @NotBlank
        private String newPasswordConfirm;
    }

    @Data
    @JsonInclude(NON_NULL)
    public static class ReqUserDto {

        @NotNull
        private Integer adminNo;
        @NotBlank
        private String id;
        @NotBlank
        private String name;
        @NotBlank
        private String email;
    }

    @Data
    @JsonInclude(NON_NULL)
    public static class ResContentsDto {

        @NotNull
        private Integer ctNo;
        @NotNull
        private String ctTxt;
        @NotNull
        private String regDatetime;
        @NotNull
        private String updDatetime;

    }

}
