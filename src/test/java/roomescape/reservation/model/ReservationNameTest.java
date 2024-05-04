package roomescape.reservation.model;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class ReservationNameTest {

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("예약자 명이 공백 문자일 경우 예외가 발생한다.")
    void createReservationName_WhenNameIsBlank(String name) {
        assertThatThrownBy(() -> new ReservationName(name))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약자 명은 공백 문자가 불가능합니다.");
    }

    @Test
    @DisplayName("예약자 명이 공백 문자일 경우 예외가 발생한다.")
    void createReservationName_WhenNameOverLength() {
        assertThatThrownBy(() -> new ReservationName("a".repeat(256)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약자 명은 최대 255자까지 입력이 가능합니다.");
    }
}
