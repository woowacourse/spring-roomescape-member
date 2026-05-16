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
import roomescape.global.exception.AlreadyPassedReservationException;
import roomescape.global.exception.PastReservationTimeException;
import roomescape.global.exception.ValidationException;

class ReservationTest {

    private final ReservationTime reservationTime = ReservationTimeFixture.createDefaultReservationTime();
    private final Theme theme = ThemeFixture.createDefaultTheme();

    @Test
    void 정상적인_예약_정보를_생성한다() {
        // given
        String name = "이프";
        LocalDate date = LocalDate.now().plusDays(1);

        // when
        Reservation reservation = Reservation.create(name, date, theme, reservationTime);

        // then
        assertThat(reservation).extracting(Reservation::getName, Reservation::getDate, Reservation::getTime)
                .containsExactly(name, date, reservationTime);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    void 예약자_이름이_비어있을_경우_예외가_발생한다(String invalidName) {
        // given
        LocalDate date = LocalDate.now().plusDays(1);

        // when & then
        assertThatThrownBy(() -> Reservation.create(invalidName, date, theme, reservationTime)).isInstanceOf(
                ValidationException.class).hasMessage("예약자 정보는 비어있을 수 없습니다.");
    }

    @ParameterizedTest(name = "날짜 {0}, 테마 {1}, 시간 {2} 일 때, {3} 예외가 발생한다")
    @MethodSource("roomescape.domain.fixture.ReservationFixture#invalidReservationConstructor")
    void 유효하지_않은_예약_일시와_테마로_예약을_생성하면_예외가_발생한다(LocalDate date, Theme theme, ReservationTime reservationTime,
                                              String expectedMessage) {
        // when & then
        assertThatThrownBy(() -> Reservation.create("이프", date, theme, reservationTime)).isInstanceOf(
                ValidationException.class).hasMessageContaining(expectedMessage);
    }

    @ParameterizedTest(name = "날짜 {0}, 테마 {1}, 시간 {2} 일 때, 과거 예약 일시 예외가 발생한다")
    @MethodSource("roomescape.domain.fixture.ReservationFixture#pastReservationDateTimeConstructor")
    void 과거_시간으로_예약을_생성하면_예외가_발생한다(LocalDate date, Theme theme, ReservationTime reservationTime,
                                      String expectedMessage) {
        // when & then
        assertThatThrownBy(() -> Reservation.create("이프", date, theme, reservationTime)).isInstanceOf(
                PastReservationTimeException.class).hasMessageContaining(expectedMessage);
    }

    @Test
    void 예약을_취소하면_예약_상태는_취소_상태가_된다() {
        // given
        LocalDate date = LocalDate.now().plusDays(1);
        Reservation reservation = Reservation.create("바니", date, theme, reservationTime);
        ReservationStatus beforeCanceledStatus = reservation.getStatus();

        // when
        Reservation canceledReservation = reservation.cancel();

        // then
        assertThat(beforeCanceledStatus).isEqualTo(ReservationStatus.RESERVED);
        assertThat(canceledReservation.getStatus()).isEqualTo(ReservationStatus.CANCELED);
    }

    @Test
    void 이미_지난_예약을_취소하면_예외가_발생한다() {
        // given
        LocalDate date = LocalDate.now().minusDays(1);
        Reservation reservation = Reservation.restore(1L, "바니", date, theme, reservationTime,
                ReservationStatus.RESERVED);

        // when & then
        assertThatThrownBy(reservation::cancel).isInstanceOf(AlreadyPassedReservationException.class);
    }
}
