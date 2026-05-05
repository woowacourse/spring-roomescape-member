package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.domain.fixture.ReservationTimeFixture;
import roomescape.domain.fixture.ThemeFixture;

class ReservationTest {

    private ReservationTime reservationTime = ReservationTimeFixture.createDefaultReservationTime();
    private Theme theme = ThemeFixture.createDefaultTheme();

    @Test
    void 정상적인_예약_정보를_생성한다() {
        // given
        String name = "이프";
        LocalDate date = LocalDate.now().plusDays(1);

        // when
        Reservation reservation = Reservation.reserve(name, date, theme, reservationTime);

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

        // when & then
        assertThatThrownBy(() -> Reservation.reserve(invalidName, date, theme, reservationTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약자 정보는 비어있을 수 없습니다.");
    }

    @ParameterizedTest(name = "날짜 {0}, 테마 {1}, 시간 {2} 일 때, {3} 예외가 발생한다")
    @MethodSource("roomescape.domain.fixture.ReservationFixture#invalidReservationConstructor")
    void 예약_일시와_테마_검증_통합_테스트(LocalDate date, Theme theme, ReservationTime reservationTime, String expectedMessage) {
        // when & then
        assertThatThrownBy(() -> Reservation.reserve("이프", date, theme, reservationTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(expectedMessage);
    }
}
