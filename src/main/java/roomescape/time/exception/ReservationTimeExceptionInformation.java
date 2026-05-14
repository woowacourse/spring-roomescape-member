package roomescape.time.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import roomescape.common.exception.ExceptionInformation;

@Getter
@AllArgsConstructor
public enum ReservationTimeExceptionInformation implements ExceptionInformation {

    ID_IS_NULL(HttpStatus.BAD_REQUEST, "예약 시간 ID는 필수입니다."),
    START_AT_IS_NULL(HttpStatus.BAD_REQUEST, "예약 시작 시간은 필수입니다."),

    TIME_NOT_FOUND(HttpStatus.BAD_REQUEST, "등록되지 않은 예약 시간입니다."),
    TIME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 예약 시간입니다."),
    TIME_STATUS_UPDATE_FAILED(HttpStatus.BAD_REQUEST, "등록된 시간 활성화/비활성화 상태 변경에 실패했습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

}
