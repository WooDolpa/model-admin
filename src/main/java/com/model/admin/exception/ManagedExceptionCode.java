package com.model.admin.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * packageName : com.model.admin.exception
 * className : ManagedExceptionCode
 * user : jwlee
 * date : 2022/10/02
 */
public enum ManagedExceptionCode {

    InvalidToken                (-1),    // 유효하지 않은 토큰
    ExpiredToken                (-2),    // 만료된 토큰
    AccessDenied                (-3),    // 접근 권한 없음
    ServerError                 (-4),    // 서버 에러
    InvalidParameter            (-5),    // 파라미터 오류
    NotExistAdminAccount        (-6),    // 존재하지 않은 계정
    NotMatchedPassword          (-7),    // 비밀번호 일치 오류
    UpdateError                 (-8),    // 업데이트 오류
    InvalidRank                 (-9),    // 순번 파라미터 오류
    InvalidAdminNo              (-10),   // 관리자 번호 오류
    InvalidPasswordMatch        (-11),   // 비밀번호가 일치하지 않습니다.
    InvalidId                   (-12),   // 아이디 파라미터 오류
    InvalidPassword             (-13),   // 비밀번호 파라미터 오류
    InvalidName                 (-14),   // 이름 파라미터 오류
    InvalidEmail                (-15),   // 이메일 파라미터 오류
    InvalidSliderTitle          (-16),   // 제목 파라미터 오류
    InvalidSliderContent        (-17),   // 내용 파라미터 오류
    InvalidSliderDataStatus     (-18),   // 상태 파라미터 오류
    InvalidSliderImage          (-19),   // 이미지 파라미터 오류
    InvalidSliderNo             (-20),   // 슬라이드 번호 파라미터 오류
    InvalidRankSize             (-21),   // 허용되지 않은 순번
    InvalidTitle                (-22),   // 제목 파라미터 오류
    InvalidDescription          (-23),   // 설명 파라미터 오류
    InvalidUrl                  (-24),   // url 파라미터 오류
    InvalidImage                (-25),   // 이미지 파라미터 오류
    InvalidGalleryNo            (-26),   // 프로젝트 번호 파라미터 오류
    SendEmailError              (-27),   // 이메일 전송 오류
    InvalidCtNo                 (-28),   // 컨텐츠 넘버 파라미터 오류
    InvalidCtTxt                (-29),   // 컨텐츠 내용 파라미터 오류
    InvalidTxtNo                (-30),   // Item 텍스트 넘버 파라미터 오류
    InvalidTxtContents          (-31),   // Item 텍스트 파라미터 오류
    ;

    private int errorCode;

    private ManagedExceptionCode(int errorCode){this.errorCode = errorCode;}

    public int getErrorCode() {return this.errorCode;}

    public int toInt() {return errorCode;}

    private static final Map<Integer, ManagedExceptionCode> lookup = new HashMap<>();

    static {
        for(ManagedExceptionCode rt : ManagedExceptionCode.values()){
            lookup.put(new Integer(rt.errorCode),rt);
        }
    }

    public static ManagedExceptionCode get(int typeInt){ return lookup.get(typeInt); }

}
