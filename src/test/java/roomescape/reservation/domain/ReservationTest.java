package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.format.DateTimeParseException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import roomescape.exception.InvalidNameException;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

class ReservationTest {
    private final Theme theme = new Theme(1L, "정글 모험", "열대 정글의 심연을 탐험하세요.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
    private final ReservationTime reservationTime = new ReservationTime(1L, "15:46");

    @DisplayName("이름이 null 혹은 공백인 경우 예외가 발생한다")
    @Test
    void validateNameExist() {
        assertAll(
                () -> assertThatThrownBy(() -> Reservation.of(1L, null, "2024-04-30", reservationTime, theme))
                        .isInstanceOf(InvalidNameException.class),
                () -> assertThatThrownBy(() -> Reservation.of(1L, "", "2024-04-30", reservationTime, theme))
                        .isInstanceOf(InvalidNameException.class),
                () -> assertThatThrownBy(() -> Reservation.of(1L, " ", "2024-04-30", reservationTime, theme))
                        .isInstanceOf(InvalidNameException.class)
        );
    }

    @DisplayName("존재하지 않는 날짜를 선택했을 경우 예외가 발생한다")
    @Test
    void validateDateAndTimeExist() {
        ReservationTime reservationTime = new ReservationTime(1L, "15:46");

        assertAll(
                () -> assertThatThrownBy(() -> Reservation.of(1L, "hotea", null, reservationTime, theme))
                        .isInstanceOf(NullPointerException.class),
                () -> assertThatThrownBy(() -> Reservation.of(1L, "hotea", "2024-14-30", reservationTime, theme))
                        .isInstanceOf(DateTimeParseException.class),
                () -> assertThatThrownBy(() -> Reservation.of(1L, "hotea", "2024-04-50", reservationTime, theme))
                        .isInstanceOf(DateTimeParseException.class)
        );
    }
}
