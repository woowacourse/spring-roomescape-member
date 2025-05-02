package roomescape.reservation.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.fixture.ReservationFixture;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.repository.ReservationTimeRepositoryImpl;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepositoryImpl;

@JdbcTest
@Import({ReservationRepositoryImpl.class, ReservationTimeRepositoryImpl.class, ThemeRepositoryImpl.class})
class ReservationRepositoryImplTest {

    @Autowired
    private ReservationRepositoryImpl reservationRepository;
    @Autowired
    private ReservationTimeRepositoryImpl reservationTimeRepository;
    @Autowired
    private ThemeRepositoryImpl themeRepository;

    @DisplayName("예약 시간에 해당하는 예약의 존재 여부를 알 수 있다.")
    @Test
    void existsByReservationTime() {
        // given
        ReservationTime reservationTime = reservationTimeRepository.add(new ReservationTime(LocalTime.now()));

        Theme theme = new Theme("name1", "dd", "tt");
        Theme savedTheme = themeRepository.add(theme);

        Reservation reservation = ReservationFixture.create("r1", LocalDate.now().plusMonths(2), reservationTime,
                savedTheme);
        reservationRepository.add(reservation);

        // when
        boolean actual = reservationRepository.existsByReservationTime(reservationTime);

        // then
        assertThat(actual).isTrue();
    }
}
