package roomescape.theme.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import roomescape.common.exception.ErrorInformation;

@Getter
@AllArgsConstructor
public enum ThemeErrorInformation implements ErrorInformation {

    ID_IS_NULL(HttpStatus.BAD_REQUEST, "THEME_001", "테마 ID는 필수입니다."),
    NAME_IS_NULL(HttpStatus.BAD_REQUEST, "THEME_002", "테마 이름은 필수입니다."),
    DESCRIPTION_IS_NULL(HttpStatus.BAD_REQUEST, "THEME_003", "테마 설명은 필수입니다."),
    THUMBNAIL_URL_IS_NULL(HttpStatus.BAD_REQUEST, "THEME_004", "테마 썸네일 URL은 필수입니다."),

    THEME_NOT_FOUND(HttpStatus.BAD_REQUEST, "THEME_005", "등록되지 않은 테마입니다."),
    THEME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "THEME_006", "이미 등록된 테마입니다."),
    THEME_STATUS_UPDATE_FAILED(HttpStatus.BAD_REQUEST, "THEME_007", "테마 활성화/비활성화 상태 변경에 실패했습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
