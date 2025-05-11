package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.testFixture.Fixture.RESERVATION_TIME_1;
import static roomescape.testFixture.Fixture.RESERVATION_TIME_2;
import static roomescape.testFixture.Fixture.THEME_1;
import static roomescape.testFixture.Fixture.THEME_2;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import roomescape.time.domain.ReservationTime;

class ReservationTest {

    @DisplayName("식별자인 id가 동일하면 같은 예약으로 취급한다.")
    @Test
    void sameReservation_whenSameId() {
        // given
        Reservation reservation1 = Reservation.of(1L, 1L, THEME_1, LocalDate.of(2025, 1, 1), RESERVATION_TIME_1);
        Reservation reservation2 = Reservation.of(1L, 1L, THEME_2, LocalDate.of(2025, 2, 1), RESERVATION_TIME_2);

        // when & then
        assertThat(reservation1).isEqualTo(reservation2);
    }

    @DisplayName("식별자가 null일 때 비교 시 항상 다른 예약과 동일취급되지 않는다.")
    @Test
    void noSameReservation_whenNullId() {
        // given
        Reservation reservation1 = Reservation.of(null, 1L, THEME_1, LocalDate.of(2025, 1, 1), RESERVATION_TIME_1);
        Reservation reservation2 = Reservation.of(null, 1L, THEME_1, LocalDate.of(2025, 1, 1), RESERVATION_TIME_1);

        // when & then
        assertThat(reservation1).isNotEqualTo(reservation2);
    }

    @DisplayName("일시가 같으면 중복된 예약이다")
    @Test
    void isDuplicated() {
        // given
        LocalDate date = LocalDate.of(2025, 1, 1);
        ReservationTime reservationTime = ReservationTime.of(1L, LocalTime.of(10, 0));
        Reservation reservation1 = Reservation.of(1L, 1L, THEME_1, date, reservationTime);

        // when
        Reservation reservation2 = Reservation.of(2L, 2L, THEME_1, date, reservationTime);
        boolean duplicated = reservation2.isDuplicatedWith(reservation1);

        // then
        assertThat(duplicated).isTrue();
    }

    @DisplayName("일시가 같지 않으면 중복된 예약이 아니다")
    @ParameterizedTest
    @CsvSource({
            "2025-02-01,10:00,2",
            "2025-01-01,11:00,3",
    })
    void isNotDuplicated(String date, String time, Long timeId) {
        // given
        LocalDate date1 = LocalDate.of(2025, 1, 1);
        ReservationTime time1 = ReservationTime.of(1L, LocalTime.of(10, 0));
        Reservation reservation1 = Reservation.of(1L, 1L, THEME_1, date1, time1);

        // when
        LocalDate date2 = LocalDate.parse(date);
        ReservationTime time2 = ReservationTime.of(timeId, LocalTime.parse(time));
        Reservation reservation2 = Reservation.of(2L, 1L, THEME_1, date2, time2);
        boolean duplicated = reservation2.isDuplicatedWith(reservation1);

        // then
        assertThat(duplicated).isFalse();
    }
}
