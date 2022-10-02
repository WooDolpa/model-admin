package com.model.admin.aspect;

import com.model.admin.constatns.ApiConstants;
import com.model.admin.exception.ManagedException;
import com.model.admin.exception.ManagedExceptionCode;
import com.model.common.enums.TokenStatus;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * packageName : com.model.admin.aspect
 * className : ControllerAspect
 * user : jwlee
 * date : 2022/10/02
 */
@Aspect
@Component
@Slf4j
public class ControllerAspect {

    @Around("execution(public * com.model.admin.controller.*.*(..))")
    public Object arroundAdviceMethod(ProceedingJoinPoint joinPoint) throws Throwable {

        Object result = null;
        //파라미터 배열
        Object[] parameterValues;
        StringBuilder parameterValueStr = new StringBuilder();

        try {

            // 상태 문자열
            String tokenStatusStr = null;

            // 토큰 상태
            TokenStatus tokenStatus = TokenStatus.Invalid;

            // api 연결 여부
            boolean hasRights = false;

            parameterValues = joinPoint.getArgs();
            if(parameterValues != null && parameterValues.length > 0){
                Arrays.stream(parameterValues).forEach(p -> parameterValueStr.append(p).append(","));
            }

            log.info("Request|{}|{}|{}", joinPoint.getTarget().getClass().getCanonicalName(), joinPoint.getSignature().getName(), parameterValueStr.toString());

            for(int i=0; i<parameterValues.length; i++){
                // 파라미터 중에서 HttpServletRequest 객체를 찾는다.
                if(parameterValues[i] instanceof HttpServletRequest){
                    log.info("Token|[{}]|[{}]",((HttpServletRequest)parameterValues[i]).getHeader(ApiConstants.AUTHORIZATION), ((HttpServletRequest)parameterValues[i]).getHeader(ApiConstants.TOKEN_STATUS));
                    if(((HttpServletRequest)parameterValues[i]).getHeader(ApiConstants.AUTHORIZATION) != null){
                        if(!((HttpServletRequest)parameterValues[i]).getHeader(ApiConstants.AUTHORIZATION).equals("")){
                            // 현재 Token값이 존재하므로 Life Cycle Reset
                            //Map<String,Object> adminMap = this.loginService.resetToken(((HttpServletRequest)parameterValues[i]).getHeader(HttpHeaderConstants.AUTHORIZATION));
                        }
                    }
                    tokenStatusStr = ((HttpServletRequest)parameterValues[i]).getHeader(ApiConstants.TOKEN_STATUS);
                    break;
                }
            }

            if(!StringUtils.isEmpty(tokenStatusStr)){
                tokenStatus = TokenStatus.valueOf(tokenStatusStr);
            }

            //Token 상태에 따른 결과 메시지 처리
            switch (tokenStatus){
                case Invalid:
                    throw new ManagedException(ManagedExceptionCode.InvalidToken, ApiConstants.INVALID_TOKEN);
                case Expired:
                    throw new ManagedException(ManagedExceptionCode.ExpiredToken, ApiConstants.EXPIRED_TOKEN);
            }

            if(TokenStatus.Valid.equals(tokenStatus)){
                hasRights = true;
            }

            if(hasRights){
                result = joinPoint.proceed();
            }else{
                throw new ManagedException(ManagedExceptionCode.AccessDenied, ApiConstants.ACCESS_DENIED);
            }

        }catch (Throwable throwable){
            throw throwable;
        }finally {
            log.info("Response|{}|{}|{}|{}", joinPoint.getTarget().getClass().getCanonicalName(), joinPoint.getSignature().getName(), parameterValueStr.toString(), result);
            log.info("ControllerAspect|aroundAdviceMethod|end");
        }

        return result;
    }

}
