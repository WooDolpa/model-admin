package com.model.admin.security;

import com.model.admin.constatns.ApiConstants;
import com.model.admin.exception.ManagedException;
import com.model.admin.exception.ManagedExceptionCode;
import com.model.admin.repository.AuthRepository;
import com.model.common.model.AdminModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * packageName : com.model.admin.security
 * className : UserDetailsService
 * user : jwlee
 * date : 2022/10/02
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthRepository authRepository;

    @Override
    public UserDetails loadUserByUsername(String id){

        Map<String, Object> param = new HashMap<>();
        param.put("id", id);

        Optional<AdminModel> adminModelOptional = Optional.ofNullable(authRepository.findAdmin(param));
        if(adminModelOptional.isPresent()) {

            AdminModel adminModel = adminModelOptional.get();
            Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            return new User(adminModel.getId(), adminModel.getPassword(), grantedAuthorities);

        }else{
            throw new ManagedException(ManagedExceptionCode.NotExistAdminAccount, ApiConstants.NOT_EXIST_ADMIN_ACCOUNT);
        }

    }
}
