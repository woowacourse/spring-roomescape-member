package roomescape.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // Reservation
    RESERVATION_NOT_FOUND("RESERVATION_404", "%s의 예약(%d번)이 존재하지 않습니다."),
    RESERVATION_ALREADY_EXIST("RESERVATION_409", "스케줄(%d번)을 사용중인 예약이 이미 존재합니다."),
    RESERVATION_DELETE_FAILED("RESERVATION_DELETE_500", "서버 내부 오류가 발생하였습니다. 관리자에게 문의해주세요."),
    RESERVATION_UPDATE_EMPTY("RESERVATION_400", "수정할 예약 정보가 없습니다."),
    RESERVATION_UPDATE_FAILED("RESERVATION_UPDATE_500", "서버 내부 오류가 발생하였습니다. 관리자에게 문의해주세요."),
    RESERVATION_NOT_FOUND_AFTER_UPDATE("RESERVATION_404_AFTER_UPDATE", "수정 후 %s의 예약(%d번)을 찾을 수 없습니다."),

    // Schedule
    SCHEDULE_NOT_FOUND_WITH_CONDITION("SCHEDULE_404_WITH_CONDITION", "해당 조건의 스케줄 id가 존재하지 않습니다. date=%s, timeId=%d, themeId=%d"),
    SCHEDULE_NOT_FOUND("SCHEDULE_404", "스케줄(%d번)이 존재하지 않습니다."),
    SCHEDULE_DELETE_FAILED("SCHEDULE_500", "서버 내부 오류가 발생하였습니다. 관리자에게 문의해주세요."),
    SCHEDULE_TIME_IN_USE("SCHEDULE_TIME_409", "해당 시간(%d번)을 사용하는 일정이 있어 삭제할 수 없습니다."),
    SCHEDULE_THEME_IN_USE("SCHEDULE_THEME_409", "해당 테마(%d번)를 사용하는 일정이 있어 삭제할 수 없습니다."),
    PAST_SCHEDULE("PAST_SCHEDULE_400","이미 지난 예약이거나 날짜/시간은 처리할 수 없습니다."),

    // ReservationTime
    RESERVATIONTIME_NOT_FOUND("RESERVATIONTIME_404", "시간(%d번)이 존재하지 않습니다."),

    // Theme
    THEME_NOT_FOUND("THEME_404", "테마(%d번)이 존재하지 않습니다."),

    // 요청 값
    INVALID_INPUT("INVALID_INPUT_400", "요청 값이 올바르지 않습니다."),

    // 서버 에러
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR_500", "서버 내부 오류가 발생하였습니다. 관리자에게 문의해주세요.")
    ;

    private final String code;
    private final String message;

    public String formatMessage(Object... args) {
        return String.format(message, args);
    }
}
