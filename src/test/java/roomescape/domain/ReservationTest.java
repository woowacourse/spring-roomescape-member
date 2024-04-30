package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.exception.BadRequestException;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTest {

    @DisplayName("예약자명이 공백인 경우 예외가 발생한다.")
    @ValueSource(strings = {"", " ", "    ", "\n", "\r", "\t"})
    @ParameterizedTest
    void validateName(String blankName) {
        ReservationTime time = new ReservationTime(LocalTime.MAX);
        assertThatThrownBy(() -> new Reservation(blankName, LocalDate.MAX, time))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("이름은 공백일 수 없습니다.");
    }
}
