package roomescape.support.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ThemeErrorCode implements ErrorCode {
    INVALID_THEME(HttpStatus.BAD_REQUEST,
        "테마는 필수입니다.", "테마 정보를 선택해 주세요."),
    THEME_NOT_EXIST(HttpStatus.NOT_FOUND,
        "존재하지 않는 테마 입니다.", "테마 목록 확인 후, 유효한 테마를 선택해 주세요"),
    THEME_IN_USE(HttpStatus.CONFLICT,
        "이미 예약이 존재하는 테마는 삭제할 수 없습니다.", "해당 테마에 연결된 예약들을 먼저 취소하거나 변경한 뒤 다시 삭제해 주세요."),
    INVALID_THEME_NAME(HttpStatus.BAD_REQUEST,
        "테마 제목은 비어 있을 수 없습니다.", "테마의 이름을 입력해 주세요"),
    INVALID_THEME_CONTENT(HttpStatus.BAD_REQUEST,
        "테마 설명은 비어 있을 수 없습니다.", "테마의 줄거리나 특징을 작성해 주세요."),
    INVALID_THEME_URL(HttpStatus.BAD_REQUEST,
        "테마 포스터 URL은 비어 있을 수 없습니다.", "테마 포스터 이미지의 유효한 URL 주소를 입력해 주세요."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
    private final String action;

    ThemeErrorCode(HttpStatus httpStatus, String message, String action) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.action = action;
    }

    @Override
    public String getCode() {
        return name();
    }
}
