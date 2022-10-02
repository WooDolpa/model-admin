package com.model.admin.service;

import com.model.admin.repository.AuthRepository;
import com.model.common.model.AdminModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * packageName : com.model.admin.service
 * className : AuthService
 * user : jwlee
 * date : 2022/10/02
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthRepository authRepository;

    /**
     * 관리자 정보 찾기
     *
     * @param id
     * @return
     */
    public Optional<AdminModel> findAdmin(String id) {

        Map<String, Object> map = new HashMap<>();
        map.put("id", id);

        return Optional.ofNullable(authRepository.findAdmin(map));
    }

}
