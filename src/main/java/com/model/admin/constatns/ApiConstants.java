package com.model.admin.constatns;

/**
 * packageName : com.model.admin.constatns
 * className : ApiConstants
 * user : jwlee
 * date : 2022/10/02
 */
public class ApiConstants {

    public static final String AUTHORIZATION = "authorization";

    // Token Status
    public static final String TOKEN_STATUS = "Token-Status";

    // Exception Message
    public final static String INVALID_TOKEN                    =   "유효하지 않은 토큰입니다.";
    public final static String EXPIRED_TOKEN                    =   "만료된 토큰입니다.";
    public final static String ACCESS_DENIED                    =   "접근 권한이 없습니다.";
    public final static String SERVER_ERROR                     =   "서버 에러";
    public final static String INVALID_PARAMETER                =   "유효하지 않은 파라미터";
    public final static String NOT_EXIST_ADMIN_ACCOUNT          =   "존재하지 않은 계정";
    public final static String NOT_MATCHED_PASSWORD             =   "비밀번호 오류";
    public final static String UPDATE_ERROR                     =   "업데이트 오류";
    public final static String INVALID_RANK                     =   "순번 파라미터 오류";
    public final static String INVALID_ADMIN_NO                 =   "관리자 번호 오류";
    public final static String INVALID_PASSWORD_MATCH           =   "비밀번호 일치 오류";
    public final static String INVALID_ID                       =   "아이디 파라미터 오류";
    public final static String INVALID_PASSWORD                 =   "비밀번호 파라미터 오류";
    public final static String INVALID_NAME                     =   "이름 파라미터 오류";
    public final static String INVALID_EMAIL                    =   "이메일 파라미터 오류";
    public final static String INVALID_SLIDER_TITLE             =   "제목 파라미터 오류";
    public final static String INVALID_SLIDER_CONTENT           =   "내용 파라미터 오류";
    public final static String INVALID_SLIDER_DATASTATUS        =   "상태 파라미터 오류";
    public final static String INVALID_SLIDER_IMAGE             =   "이미지 파라미터 오류";
    public final static String INVALID_SLIDER_NO                =   "슬라이드 번호 파라미터 오류";
    public final static String INVALID_RANK_SIZE                =   "허용되지 않은 순번";
    public final static String INVALID_TITLE                    =   "제목 파라미터 오류";
    public final static String INVALID_DESCRIPTION              =   "설명 파라미터 오류";
    public final static String INVALID_URL                      =   "연결 주소 파라미터 오류";
    public final static String INVALID_IMAGE                    =   "이미지 파라미터 오류";
    public final static String INVALID_GALLERY_NO               =   "프로젝트 번호 파라미터 오류";
    public final static String SEND_EMAIL_ERROR                 =   "이메일 전송 오류";
    public final static String INVALID_CT_NO                    =   "컨텐츠 넘버 파라미터 오류";
    public final static String INVALID_CT_TXT                   =   "컨텐츠 내용 파라미터 오류";
    public final static String INVALID__TXT_NO                  =   "Item 텍스트 넘버 파라미터 오류";
    public final static String INVALID_TXT_CONTENTS             =   "Item 텍스트 파라미터 오류";

    // Response Code
    public static final String RESPONSE_SUCCESS_CODE    =   "0";
    public static final String RESPONSE_FAIL_CODE       =   "-9999";

    // Response Message
    public static final String RESPONSE_SUCCESS_MSG     =   "success";
    public static final String RESPONSE_FAIL_MSG        =   "fail";

    // Sub1 search Type
    public static final String SEARCH_ORDER_BY_RANK     =   "1";
    public static final String SEARCH_ORDER_BY_REG      =   "2";

    // Sub2 item size
    public static final int ITEM_MAX_SIZE               =   3;

    // validation
    public static final String INVALID_FIELD_ID             =   "id";
    public static final String INVALID_FIELD_PASSWORD       =   "password";
    public static final String INVALID_FIELD_NAME           =   "name";
    public static final String INVALID_FIELD_EMAIL          =   "email";
    public static final String INVALID_FIELD_SLIDER_NO      =   "sliderNo";
    public static final String INVALID_FIELD_CT_NO          =   "ctNo";
    public static final String INVALID_FIELD_CT_TXT         =   "ctTxt";
    public static final String INVALID_FIELD_TXT_NO         =   "txtNo";
    public static final String INVALID_FIELD_TXT_CONTENTS   =   "txtContents";

    public static final int ADMIN_NO                    =   1;

    // send email title
    public final static String MAIL_TEMP_PASSWORD_TITLE =   "임시 비밀번호 발송";
}
