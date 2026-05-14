package roomescape.reservation.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import roomescape.common.exception.ErrorInformation;

@Getter
@AllArgsConstructor
public enum ReservaitonErrorInformation implements ErrorInformation {

    RESERVATION_ID_IS_NULL(HttpStatus.BAD_REQUEST, "RES_001", "예약 ID는 필수입니다."),
    RESERVATION_NAME_IS_NULL(HttpStatus.BAD_REQUEST, "RES_002", "예약자 이름은 필수입니다."),
    RESERVATION_DATE_IS_NULL(HttpStatus.BAD_REQUEST, "RES_003", "예약 날짜는 필수입니다."),
    RESERVATION_TIME_IS_NULL(HttpStatus.BAD_REQUEST, "RES_004", "예약 시간은 필수입니다."),
    RESERVATION_THEME_IS_NULL(HttpStatus.BAD_REQUEST, "RES_005", "테마는 필수입니다."),

    RESERVATION_NOT_FOUND(HttpStatus.BAD_REQUEST, "RES_006", "등록되지 않은 예약입니다."),
    RESERVATION_ALREADY_BOOKED(HttpStatus.BAD_REQUEST, "RES_007", "해당 날짜/시간/테마는 이미 예약되었습니다."),
    RESERVATION_ALREADY_CANCELED(HttpStatus.BAD_REQUEST, "RES_008", "이미 취소된 예약입니다."),
    RESERVATION_NOT_OWNER(HttpStatus.BAD_REQUEST, "RES_009", "본인의 예약만 취소할 수 있습니다."),
    RESERVATION_ALREADY_PAST(HttpStatus.BAD_REQUEST, "RES_010", "이미 지난 예약입니다."),
    RESERVATION_PAST_DATETIME_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "RES_011", "과거 날짜/시간으로는 예약할 수 없습니다."),
    RESERVATION_NEW_SCHEDULE_PAST_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "RES_012", "이미 지난 날짜/시간을 예약할 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
