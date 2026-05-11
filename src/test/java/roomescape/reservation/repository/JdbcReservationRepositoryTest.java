package roomescape.reservation.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.config.TestFixture.reservation;
import static roomescape.config.TestFixture.reservationTime;
import static roomescape.config.TestFixture.theme;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.reservation.entity.Reservation;
import roomescape.reservationtime.entity.ReservationTime;
import roomescape.reservationtime.repository.JdbcReservationTimeRepository;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.entity.Theme;
import roomescape.theme.repository.JdbcThemeRepository;
import roomescape.theme.repository.ThemeRepository;

@JdbcTest
@Import({JdbcReservationRepository.class, JdbcReservationTimeRepository.class, JdbcThemeRepository.class})
class JdbcReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Test
    void 예약을_저장하는_테스트() {
        String name = "봉구스";
        LocalDate date = LocalDate.of(2026, 5, 6);
        ReservationTime reservationTime = reservationTimeRepository.save(reservationTime(LocalTime.of(10, 0)));
        Theme theme = themeRepository.save(theme("테마"));
        Reservation reservation = reservation(name, date, reservationTime, theme);

        Reservation savedReservation = reservationRepository.save(reservation);

        assertThat(savedReservation.getId()).isPositive();
        assertThat(savedReservation.getName()).isEqualTo(name);
        assertThat(savedReservation.getDate()).isEqualTo(date);
        assertThat(savedReservation.getTime()).isEqualTo(reservationTime);
        assertThat(savedReservation.getTheme()).isEqualTo(theme);
    }

    @Test
    void 모든_예약을_조회하는_테스트() {
        String name = "봉구스";
        LocalDate date = LocalDate.of(2026, 5, 6);
        ReservationTime reservationTime = reservationTimeRepository.save(reservationTime(LocalTime.of(10, 0)));
        Theme theme = themeRepository.save(theme("테마"));
        Reservation reservation = reservation(name, date, reservationTime, theme);

        Reservation savedReservation = reservationRepository.save(reservation);
        List<Reservation> reservations = reservationRepository.findAll();

        assertThat(reservations).containsExactly(savedReservation);
    }

    @Test
    void 예약을_취소하는_테스트() {
        String name = "봉구스";
        LocalDate date = LocalDate.of(2026, 5, 6);
        ReservationTime reservationTime = reservationTimeRepository.save(reservationTime(LocalTime.of(10, 0)));
        Theme theme = themeRepository.save(theme("테마"));
        Reservation reservation = reservation(name, date, reservationTime, theme);

        Reservation savedReservation = reservationRepository.save(reservation);
        reservationRepository.deleteById(savedReservation.getId());

        List<Reservation> reservations = reservationRepository.findAll();
        assertThat(reservations).doesNotContain(savedReservation);
    }

}
