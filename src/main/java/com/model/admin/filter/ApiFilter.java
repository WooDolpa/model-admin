package com.model.admin.filter;

import com.model.admin.constatns.ApiConstants;
import com.model.common.enums.TokenStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * packageName : com.model.admin.filter
 * className : ApiFilter
 * user : jwlee
 * date : 2022/10/02
 */
@Slf4j
public class ApiFilter implements Filter {

    private static final String NO_NEED_TOKEN_API =
            new StringBuilder()
                    .append("/admin/api/v1/auth/login").append("|")
                    .append("/admin/api/v1/auth/temp/password").append("|")
                    .toString();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        boolean isNoNeedAccessTokenApi = false;
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        CustomHttpServletRequest customHttpServletRequest = new CustomHttpServletRequest(httpRequest);
        String authorization = "";

        if(isNoNeedAccessTokenApi(httpRequest)){
            isNoNeedAccessTokenApi = true;     //Token 값이 필요없는 API
        }

        if(httpRequest.getHeader(ApiConstants.AUTHORIZATION) != null){
            authorization = httpRequest.getHeader(ApiConstants.AUTHORIZATION);      //Token 값 가져오기
        }

        checkValidateToken(isNoNeedAccessTokenApi, authorization, customHttpServletRequest);
        filterChain.doFilter(customHttpServletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    /**
     * Token 값이 필요없는 API 인지 판별
     * @param httpRequest
     * @return
     */
    private boolean isNoNeedAccessTokenApi(HttpServletRequest httpRequest){
        if(httpRequest.getRequestURI().matches(NO_NEED_TOKEN_API)) {
            return true;
        }
        return false;
    }

    /**
     * Token 확인 후 Http Header 에 결과값을 저장
     *
     * @param isNoNeedAccessTokenApi
     * @param authorization
     * @param customHttpServletRequest
     */
    private void checkValidateToken(boolean isNoNeedAccessTokenApi, String authorization, CustomHttpServletRequest customHttpServletRequest) {

        if(!StringUtils.hasLength(authorization) && authorization.length() > 0){
            customHttpServletRequest.putHeader(ApiConstants.AUTHORIZATION, authorization);
            customHttpServletRequest.putHeader(ApiConstants.TOKEN_STATUS, TokenStatus.Valid.name());
        }else{
            //Token 값이 존재하지 않음
            if(isNoNeedAccessTokenApi){
                customHttpServletRequest.putHeader(ApiConstants.TOKEN_STATUS, TokenStatus.Valid.name());
            }else{
                customHttpServletRequest.putHeader(ApiConstants.TOKEN_STATUS, TokenStatus.Invalid.name());
            }
        }

    }
}
