package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ReservationTest {

    @Test
    void 정상적인_예약_정보를_생성한다() {
        // given
        String name = "이프";
        LocalDate date = LocalDate.now().plusDays(1);
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));

        // when
        Reservation reservation = new Reservation(name, date, reservationTime);

        // then
        assertThat(reservation)
                .extracting(Reservation::getName, Reservation::getDate, Reservation::getTime)
                .containsExactly(name,  date, reservationTime);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    void 예약자_이름이_비어있을_경우_예외가_발생한다(String invalidName) {
        // given
        LocalDate date = LocalDate.now().plusDays(1);
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));

        // when & then
        assertThatThrownBy(() -> new Reservation(invalidName, date, reservationTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약자 정보는 비어있을 수 없습니다.");
    }

    @ParameterizedTest(name = "날짜 {0}, 시간 {1} 일 때 예외가 발생한다")
    @MethodSource("roomescape.domain.fixture.ReservationFixture#invalidReservationConstructor")
    void 예약_일시_검증_통합_테스트(LocalDate date, ReservationTime reservationTime, String expectedMessage) {
        // when & then
        assertThatThrownBy(() -> new Reservation("이프", date, reservationTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(expectedMessage);
    }
}
