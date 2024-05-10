package roomescape.domain.reservation;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.fixture.MemberFixtures;
import roomescape.fixture.ReservationFixtures;
import roomescape.fixture.ReservationTimeFixtures;
import roomescape.fixture.ThemeFixtures;

class ReservationTest {

    @Test
    @DisplayName("지나간 날짜에 대한 예약을 추가하면 예외가 발생한다.")
    void createReservationByPastDate() {
        //given
        LocalDateTime now = LocalDateTime.of(2024, 5, 2, 12, 2);
        LocalDate pastDay = LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth())
                .minusDays(1);
        Reservation reservation = ReservationFixtures.createReservation(
                MemberFixtures.createUserMember("daon"),
                pastDay.toString(),
                ReservationTimeFixtures.createReservationTime("12:12"),
                ThemeFixtures.createDefaultTheme()
        );

        //when //then
        assertThatThrownBy(() -> reservation.validatePast(now))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약일과 예약 시간은 과거일 수 없습니다.");
    }

    @Test
    @DisplayName("지나간 시간에 대한 예약을 추가하면 예외가 발생한다.")
    void createReservationByPastTime() {
        //given
        LocalDateTime now = LocalDateTime.of(2024, 5, 2, 12, 2);
        LocalDate today = LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth());
        LocalTime past = LocalTime.of(now.getHour(), now.getMinute())
                .minusHours(1);

        Reservation reservation = ReservationFixtures.createReservation(
                MemberFixtures.createUserMember("daon"),
                today.toString(),
                ReservationTimeFixtures.createReservationTime(past.toString()),
                ThemeFixtures.createDefaultTheme()
        );

        //when //then
        assertThatThrownBy(() -> reservation.validatePast(now))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약일과 예약 시간은 과거일 수 없습니다.");
    }
}
