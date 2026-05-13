package roomescape.exception;

import static roomescape.service.ThemeService.MAX_RANKING_PERIOD;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    NOT_FOUND_RESERVATION(HttpStatus.NOT_FOUND, "[ERROR] 해당 ID의 예약을 찾을 수 없습니다."),
    DUPLICATED_RESERVATION(HttpStatus.CONFLICT, "[ERROR] 해당 시간에 예약이 이미 존재합니다. 예약 가능한 시간으로 다시 시도해 주세요."),
    PAST_TIME_RESERVATION(HttpStatus.UNPROCESSABLE_ENTITY, "[ERROR] 지나간 시간에는 예약할 수 없습니다. 예약 시간을 변경해 주세요."),

    NOT_FOUND_RESERVATION_TIME(HttpStatus.NOT_FOUND, "[ERROR] 해당 ID의 예약 시간을 찾을 수 없습니다."),
    REFERENCED_TIME(HttpStatus.CONFLICT, "[ERROR] 현재 해당 예약 시간을 사용하는 예약이 존재합니다. 연관된 예약을 삭제한 후 다시 시도해 주세요."),

    NOT_FOUND_THEME(HttpStatus.NOT_FOUND, "[ERROR] 해당 ID의 테마를 찾을 수 없습니다."),
    REFERENCED_THEME(HttpStatus.CONFLICT, "[ERROR] 현재 해당 테마를 사용하는 예약이 존재합니다. 연관된 예약을 삭제한 후 다시 시도해 주세요."),
    FUTURE_RANKING_PERIOD(HttpStatus.UNPROCESSABLE_ENTITY, "[ERROR] 조회 기간에 미래 날짜가 존재합니다. 현재보다 이전으로 기간을 설정해 주세요."),
    INVALID_RANKING_PERIOD(HttpStatus.UNPROCESSABLE_ENTITY,
            "[ERROR] 종료 날짜가 시작 날짜보다 빠릅니다. 종료 날짜가 시작 날짜보다 뒤에 오도록 요청해 주세요."),
    LONG_RANKING_PERIOD(HttpStatus.UNPROCESSABLE_ENTITY,
            String.format("[ERROR] 조회 기간이 최대 기간을 초과했습니다. 기간이 1년(%s일) 이내가 되도록 다시 요청해 주세요.", MAX_RANKING_PERIOD)),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "[ERROR] 서버 내부에서 에러가 발생했습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
