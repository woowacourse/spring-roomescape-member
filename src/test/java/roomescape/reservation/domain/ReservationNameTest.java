package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ReservationNameTest {

    @Test
    @DisplayName("예약자명에 null을 입력받으면 예외가 발생한다.")
    void createReservationNameByNullTest() {
        assertThatThrownBy(() -> new ReservationName(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @DisplayName("예약자명에 빈 문자열, 공백 입력시 예외가 발생한다.")
    @ValueSource(strings = {"", " "})
    void createReservationNameByBlankTest(String input) {
        assertThatThrownBy(() -> new ReservationName(input))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("예약자명에 한글이 아닌 값을 입력시 예외가 발생한다.")
    void createReservationNameByInvalidFormat() {
        assertThatThrownBy(() -> new ReservationName("카ki"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("예약자명이 4글자를 초과하면 예외가 발생한다.")
    void createReservationNameByLengthTest() {
        assertThatThrownBy(() -> new ReservationName("abcde"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
