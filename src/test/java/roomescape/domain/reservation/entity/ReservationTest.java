package roomescape.domain.reservation.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import roomescape.common.exception.InvalidArgumentException;
import roomescape.domain.auth.entity.User;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class ReservationTest {

    @DisplayName("아이디 존재 여부")
    @ParameterizedTest
    @CsvSource(value = {"1,true", "null,false"}, nullValues = "null")
    void test1(final Long id, final boolean expected) {
        // given
        final ReservationTime reservationTime =mock(ReservationTime.class);
        final Theme theme = mock(Theme.class);
        final User user = mock(User.class);

        final Reservation reservation = new Reservation(id, user, LocalDate.now(), reservationTime, theme);

        // when
        final boolean result = reservation.existId();

        // then
        assertThat(result).isEqualTo(expected);
    }

    @DisplayName("예약 날짜는 현재보다 미래여야 한다.")
    @Test
    void test4() {
        // given
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime future = now.plusDays(1);

        final Theme theme = mock(Theme.class);
        final ReservationTime reservationTime = new ReservationTime(1L, future.toLocalTime());
        final User user = mock(User.class);

        final Reservation reservation = new Reservation(1L, user, future.toLocalDate(), reservationTime, theme);

        // when & then
        assertThatCode(() -> reservation.validateNotPastReservation(now)).doesNotThrowAnyException();
    }

    @DisplayName("예약 날짜가 과거라면 예외를 반환한다.")
    @Test
    void notPastReservation_throwsException() {
        // given
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime pastDay = now.minusDays(1);

        final Theme theme = mock(Theme.class);
        final ReservationTime reservationTime = new ReservationTime(1L, pastDay.toLocalTime());
        final User user = mock(User.class);

        final Reservation reservation = new Reservation(1L, user, pastDay.toLocalDate(), reservationTime, theme);

        // when & then
        assertThatThrownBy(() -> reservation.validateNotPastReservation(now)).isInstanceOf(
                InvalidArgumentException.class);
    }


}
