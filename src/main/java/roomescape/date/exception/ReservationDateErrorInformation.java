package roomescape.date.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import roomescape.common.exception.ErrorInformation;

@Getter
@AllArgsConstructor
public enum ReservationDateErrorInformation implements ErrorInformation {

    ID_IS_NULL(HttpStatus.BAD_REQUEST, "RES_DATE_001", "예약날짜 ID는 필수입니다."),
    DATE_IS_NULL(HttpStatus.BAD_REQUEST, "RES_DATE_002", "예약 날짜는 필수입니다."),
    PAST_DATE_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "RES_DATE_003", "과거 날짜는 등록할 수 없습니다."),

    DATE_NOT_FOUND(HttpStatus.BAD_REQUEST, "RES_DATE_004", "등록되지 않은 예약날짜입니다."),
    DATE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "RES_DATE_005", "이미 등록된 날짜 입니다."),
    DATE_STATUS_UPDATE_FAILED(HttpStatus.BAD_REQUEST, "RES_DATE_006", "등록된 날짜의 활성화/비활성화 상태 변경에 실패했습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
