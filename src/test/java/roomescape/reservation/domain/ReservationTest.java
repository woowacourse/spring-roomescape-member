package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import roomescape.reservation.fixture.TestFixture;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

class ReservationTest {

    private final Theme theme = TestFixture.makeTheme();

    @Test
    void createReservation_shouldThrowException_whenTimeIsBeforeNow() {
        assertThatThrownBy(() -> Reservation.createUpcomingReservationWithUnassignedId(
                "대니",
                LocalDate.now().minusDays(1),
                ReservationTime.of(1L, LocalTime.now().minusHours(1)),
                theme, LocalDateTime.now())
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 시간이 현재 시간보다 이전일 수 없습니다.");
    }

    @Test
    void createReservation_shouldThrowException_whenNameLengthOverTen() {
        assertThatThrownBy(() -> Reservation.of(1L,
                "비행기타는희귀데코피크민",
                LocalDate.now().minusDays(1),
                ReservationTime.of(1L, LocalTime.now()),
                theme)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이름은 10글자 이내여야 합니다.");
    }
}
