package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationTime;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.TestFixture.*;

class ReservationTest {

    @ParameterizedTest
    @MethodSource("invalidLocalDate")
    @DisplayName("예약 날짜가 현재 날짜 이후가 아닌 경우 예외가 발생한다.")
    void throwExceptionWhenInvalidDate(final LocalDate invalidDate) {
        // when & then
        assertThatThrownBy(() -> new Reservation(MEMBER_MIA(), invalidDate.toString(), new ReservationTime(MIA_RESERVATION_TIME), WOOTECO_THEME()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<LocalDate> invalidLocalDate() {
        return Stream.of(
                LocalDate.now(),
                LocalDate.now().minusDays(1L)
        );
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "22:00:00", "abc"})
    @DisplayName("예약 날짜 입력 값이 유효하지 않으면 예외가 발생한다.")
    void throwExceptionWhenCannotConvertToLocalDate(final String invalidDate) {
        // when & then
        assertThatThrownBy(() -> new Reservation(MEMBER_MIA(), invalidDate, new ReservationTime(MIA_RESERVATION_TIME), WOOTECO_THEME()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @MethodSource("reservationsAndExpectedResult")
    @DisplayName("예약이 동일한 예약 시간을 갖는지 확인한다.")
    void hasSameDateTime(final LocalDate date, final String time, final boolean expectedResult) {
        // given
        final Reservation reservation = MIA_RESERVATION(new ReservationTime(MIA_RESERVATION_TIME), WOOTECO_THEME());

        // when
        final boolean actualResult = reservation.hasSameDateTime(date, new ReservationTime(time));

        // then
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    private static Stream<Arguments> reservationsAndExpectedResult() {
        return Stream.of(
                Arguments.of(MIA_RESERVATION_DATE, MIA_RESERVATION_TIME, true),
                Arguments.of(TOMMY_RESERVATION_DATE, TOMMY_RESERVATION_TIME, false)
        );
    }
}
