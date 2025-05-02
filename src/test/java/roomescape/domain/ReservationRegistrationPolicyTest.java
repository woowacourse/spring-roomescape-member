package roomescape.domain;

import static roomescape.exception.ReservationErrorCode.ALREADY_RESERVED;
import static roomescape.exception.ReservationErrorCode.PAST_RESERVATION;
import static roomescape.testFixture.Fixture.RESERVATION_1;
import static roomescape.testFixture.Fixture.RESERVATION_TIME_1;
import static roomescape.testFixture.Fixture.THEME_1;

import java.time.LocalDate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.exception.ImpossibleReservationException;

class ReservationRegistrationPolicyTest {

    @DisplayName("과거 시간으로의 예약은 등록이 불가하여 예외를 발생시킨다.")
    @Test
    void error_when_register_isPast() {
        // given
        LocalDate pastDate = LocalDate.now().minusDays(1);
        Reservation candidate = Reservation.withoutId("멍구", THEME_1, pastDate, RESERVATION_TIME_1);
        ReservationRegistrationPolicy reservationRegistrationPolicy = new ReservationRegistrationPolicy();

        boolean existsDuplicatedReservation = false;

        // when & then
        Assertions.assertThatThrownBy(() ->
                reservationRegistrationPolicy.validate(candidate, existsDuplicatedReservation)
        ).isInstanceOf(ImpossibleReservationException.class)
                .hasMessage(PAST_RESERVATION.getMessage());
    }

    @DisplayName("날짜, 테마, 시간이 중복된다면 등록이 불가하여 예외를 발생시킨다.")
    @Test
    void error_when_register_duplicated() {
        // given
        ReservationRegistrationPolicy reservationRegistrationPolicy = new ReservationRegistrationPolicy();
        Reservation candidate = RESERVATION_1;

        boolean existsDuplicatedReservation = true;

        // when & then
        Assertions.assertThatThrownBy(() ->
                reservationRegistrationPolicy.validate(candidate, existsDuplicatedReservation)
        ).isInstanceOf(ImpossibleReservationException.class)
                .hasMessage(ALREADY_RESERVED.getMessage());
    }

}
