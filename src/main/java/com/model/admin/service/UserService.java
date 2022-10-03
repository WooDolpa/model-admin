package com.model.admin.service;

import com.model.admin.constatns.ApiConstants;
import com.model.admin.dto.UserDto;
import com.model.admin.exception.ManagedException;
import com.model.admin.exception.ManagedExceptionCode;
import com.model.admin.repository.UserRepository;
import com.model.common.model.AdminModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * packageName : com.model.admin.service
 * className : UserService
 * user : jwlee
 * date : 2022/10/02
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    /**
     * 관리자 조회
     * @param adminNo
     * @return
     */
    public UserDto.ResUserDto findUser (final int adminNo){

        UserDto.ResUserDto resUserDto = new UserDto.ResUserDto();

        Map<String, Object> map = new HashMap<>();
        map.put("adminNo", adminNo);

        Optional<AdminModel> adminModelOptional = Optional.ofNullable(userRepository.findUser(map));

        if(adminModelOptional.isPresent()){

            AdminModel adminModel = adminModelOptional.get();

            resUserDto.setAdminNo(adminModel.getAdminNo());
            resUserDto.setId(adminModel.getId());
            resUserDto.setName(adminModel.getName());
            resUserDto.setPassword(adminModel.getPassword());
            resUserDto.setEmail(adminModel.getEmail());
        }

        return resUserDto;
    }

    /**
     * 비밀번호 수정
     * @param dto
     * @return
     */
    @Transactional
    public int updatePassword (final UserDto.ReqUserPasswordDto dto){

        int count = 0;

        Map<String,Object> map = new HashMap<>();
        map.put("adminNo", dto.getAdminNo());

        // 1. 관리자 정보 조회
        Optional<AdminModel> adminModelOptional = Optional.ofNullable(userRepository.findUser(map));

        if(adminModelOptional.isPresent()){

            AdminModel adminModel = adminModelOptional.get();

            // 2. 현재 비밀번호 매치
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

            boolean isMatched = bCryptPasswordEncoder.matches(dto.getPassword(), adminModel.getPassword());

            if(!isMatched){
                //비밀번호가 일치하지 않음
                throw new ManagedException(ManagedExceptionCode.NotMatchedPassword, ApiConstants.NOT_MATCHED_PASSWORD);
            }

            // 3. 관리자 정보 수정
            String newPassword = bCryptPasswordEncoder.encode(dto.getNewPassword());
            map.put("password", newPassword);

            try {
                count = userRepository.updateUser(map);
            }catch (Exception e){
                log.warn("error Message|{}",e.getMessage());
            }finally {
            }
        }

        return count;
    }

    /**
     * 회원정보 변경
     *
     * @param dto
     * @return
     */
    @Transactional
    public int updateUser(UserDto.ReqUserDto dto){

        Map<String, Object> map = new HashMap<>();
        map.put("adminNo", dto.getAdminNo());
        map.put("id", dto.getId());
        map.put("name", dto.getName());
        map.put("email", dto.getEmail());

        return userRepository.updateUser(map);
    }

    /**
     * 임시 비밀번호 변경
     *
     * @param adminNo
     * @param password
     * @return
     */
    @Transactional
    public int updateUser(final int adminNo, final String password){

        // 비밀번호 암호화
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String bCryptPassword = bCryptPasswordEncoder.encode(password);

        Map<String, Object> map = new HashMap<>();
        map.put("adminNo", adminNo);
        map.put("password", bCryptPassword);

        return userRepository.updateUser(map);
    }

}
