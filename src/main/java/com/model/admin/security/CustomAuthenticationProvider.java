package com.model.admin.security;

import com.model.admin.constatns.ApiConstants;
import com.model.admin.exception.ManagedException;
import com.model.admin.exception.ManagedExceptionCode;
import com.model.admin.service.AuthService;
import com.model.common.model.AdminModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * packageName : com.model.admin.security
 * className : UserAuthenticationProvider
 * user : jwlee
 * date : 2022/10/02
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {

    protected MessageSourceAccessor messageSourceAccessor = SpringSecurityMessageSource.getAccessor();
    private final AuthService authService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        log.info("CustomAuthenticationProvider authenticate called...");
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        String loginId = authentication.getName();
        String password = ((String)authentication.getCredentials()).trim();

        Optional<AdminModel> adminModel = authService.findAdmin(loginId);

        if(!adminModel.isPresent()){
            // 존재하지 않은 정보
            throw new ManagedException(ManagedExceptionCode.NotExistAdminAccount, ApiConstants.NOT_EXIST_ADMIN_ACCOUNT);
        }

        //비밀번호 일치 여부
        boolean isMatched = bCryptPasswordEncoder.matches(password, adminModel.get().getPassword());

        if(!isMatched){
            //비밀번호가 일치하지 않음
            throw new ManagedException(ManagedExceptionCode.NotMatchedPassword, ApiConstants.NOT_MATCHED_PASSWORD);
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        Collection<? extends GrantedAuthority> authorities = null;
        List<HashMap<String, Object>> roleList = null;
        ArrayList<String> roles = null;

        try {
            roles = new ArrayList<String>();

            authorities = getAuthorities(loginId, roles);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if( authorities == null ){
            throw new BadCredentialsException(messageSourceAccessor.getMessage(
                    "SimpleGrantedAuthority.noAuthority",
                    new Object[] { authentication.getName() }, "Username {0} not found"));
        }


        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginId, authentication.getCredentials(), authorities);

        log.debug("Provider Token|[{}]", authenticationToken);

        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }

    public Collection<? extends GrantedAuthority> getAuthorities(String userId, ArrayList<String> roles) throws Exception {
        List<GrantedAuthority> authList = getGrantedAuthorities(getRoles(userId, roles));

        return authList;
    }

    public static List<GrantedAuthority> getGrantedAuthorities(List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }

        return authorities;
    }

    public List<String> getRoles(String userId, ArrayList<String> roles) throws Exception {
//        roles.add("ROLE_USER");
        return roles;
    }

}
