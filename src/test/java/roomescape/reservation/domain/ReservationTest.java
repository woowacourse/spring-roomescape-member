package roomescape.reservation.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.InvalidDateException;
import roomescape.exception.InvalidNameException;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class ReservationTest {
    private final Theme theme = new Theme(1L, "정글 모험", "열대 정글의 심연을 탐험하세요.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
    private final ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(15, 46));

    @DisplayName("이름이 null 혹은 공백인 경우 예외가 발생한다")
    @Test
    void validateNameExist() {
        assertAll(
                () -> assertThatThrownBy(() -> new Reservation(1L, null, LocalDate.of(2024, 4, 30), reservationTime, theme))
                        .isInstanceOf(InvalidNameException.class),
                () -> assertThatThrownBy(() -> new Reservation(1L, "", LocalDate.of(2024, 4, 30), reservationTime, theme))
                        .isInstanceOf(InvalidNameException.class),
                () -> assertThatThrownBy(() -> new Reservation(1L, " ", LocalDate.of(2024, 4, 30), reservationTime, theme))
                        .isInstanceOf(InvalidNameException.class)
        );
    }

    @DisplayName("날짜가 null인 경우 예외가 발생한다")
    @Test
    void validateDateAndTimeExist() {
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(15, 46));

        assertThatThrownBy(() -> new Reservation(1L, "hotea", null, reservationTime, theme))
                .isInstanceOf(InvalidDateException.class);
    }
}