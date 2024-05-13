package roomescape.domain.reservationtime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ReservationStartAtTest {

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("예약 시작 시간은 null이나 공백 문자열이 입력되면 예외가 발생한다.")
    void createReservationStartAtByNullOrEmpty(String given) {
        //when //then
        assertThatThrownBy(() -> ReservationStartAt.from(given))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("시작 시간은 비어있을 수 없습니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"29:12", "12.12", "8:9"})
    @DisplayName("예약 시작 시간의 형식이 잘못되면 예외가 발생한다.")
    void createReservationStartAtInvalidTimeFormat(String given) {
        //when //then
        assertThatThrownBy(() -> ReservationStartAt.from(given))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("시작 시간 형식은 HH:mm 이어야 합니다.");
    }
}
