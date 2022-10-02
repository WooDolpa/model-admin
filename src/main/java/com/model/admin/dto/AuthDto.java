package com.model.admin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.*;

/**
 * packageName : com.model.admin.dto
 * className : AuthDto
 * user : jwlee
 * date : 2022/10/02
 */
public class AuthDto {

    @Data
    @JsonInclude(NON_NULL)
    @NoArgsConstructor
    public static class LoginDto {
        @NotBlank
        private String id;
        @NotBlank
        private String password;
        @Builder
        public LoginDto(String id, String password) {
            this.id = id;
            this.password = password;
        }
    }

}
