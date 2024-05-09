package roomescape.reservation.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.TestFixture.*;

class ReservationTest {

    @ParameterizedTest
    @MethodSource(value = "reservationDate")
    @DisplayName("예약자 날짜가 당일 혹은 이전인지 확인한다.")
    void validateName(LocalDate date, boolean expectedResult) {
        // given
        Reservation reservation = new Reservation(USER_MIA(), date, new ReservationTime(MIA_RESERVATION_TIME), WOOTECO_THEME());

        // when
        boolean actualResult = reservation.isBeforeOrOnToday();

        // then
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    private static Stream<Arguments> reservationDate() {
        return Stream.of(
                Arguments.of(LocalDate.now().minusDays(1), true),
                Arguments.of(LocalDate.now(), true),
                Arguments.of(LocalDate.now().plusDays(1), false)
        );
    }
}
