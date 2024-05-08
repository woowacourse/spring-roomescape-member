package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import roomescape.exception.RoomEscapeException;
import roomescape.exception.message.ExceptionMessage;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

class ReservationTest {
    private final Theme theme = new Theme(1L, "정글 모험", "열대 정글의 심연을 탐험하세요.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
    private final ReservationTime reservationTime = new ReservationTime(1L, "15:46");

    @DisplayName("이름이 null 혹은 공백인 경우 예외가 발생한다")
    @Test
    void validateNameExist() {
        assertAll(
                () -> assertThatThrownBy(() -> new Reservation(1L, null, new ReservationDate("2024-04-30"), reservationTime, theme))
                        .isInstanceOf(RoomEscapeException.class)
                        .hasMessage(ExceptionMessage.INVALID_USER_NAME.getMessage()),
                () -> assertThatThrownBy(() -> new Reservation(1L, "", new ReservationDate("2024-04-30"), reservationTime, theme))
                        .isInstanceOf(RoomEscapeException.class)
                        .hasMessage(ExceptionMessage.INVALID_USER_NAME.getMessage()),
                () -> assertThatThrownBy(() -> new Reservation(1L, " ", new ReservationDate("2024-04-30"), reservationTime, theme))
                        .isInstanceOf(RoomEscapeException.class)
                        .hasMessage(ExceptionMessage.INVALID_USER_NAME.getMessage())
        );
    }

    @DisplayName("존재하지 않는 날짜를 선택했을 경우 예외가 발생한다")
    @Test
    void validateDateAndTimeExist() {
        ReservationTime reservationTime = new ReservationTime(1L, "15:46");

        assertAll(
                () -> assertThatThrownBy(() -> new Reservation(1L, "hotea", new ReservationDate(null), reservationTime, theme))
                        .isInstanceOf(RoomEscapeException.class)
                        .hasMessage(ExceptionMessage.FAIL_PARSE_DATE.getMessage()),
                () -> assertThatThrownBy(() -> new Reservation(1L, "hotea", new ReservationDate("2024-14-30"), reservationTime, theme))
                        .isInstanceOf(RoomEscapeException.class)
                        .hasMessage(ExceptionMessage.FAIL_PARSE_DATE.getMessage()),
                () -> assertThatThrownBy(() -> new Reservation(1L, "hotea", new ReservationDate("2024-04-50"), reservationTime, theme))
                        .isInstanceOf(RoomEscapeException.class)
                        .hasMessage(ExceptionMessage.FAIL_PARSE_DATE.getMessage())
        );
    }
}
