package roomescape.reservation.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import roomescape.reservation.entity.Reservation;
import roomescape.reservationtime.entity.ReservationTime;
import roomescape.reservationtime.exception.ReservationTimeNotFoundException;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.entity.Theme;
import roomescape.theme.exception.ThemeNotFoundException;
import roomescape.theme.repository.ThemeRepository;

@Sql({"/create_reservation_time.sql", "/create_theme.sql"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
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
        ReservationTime reservationTime = reservationTimeRepository.findById(1L)
                .orElseThrow(() -> new ReservationTimeNotFoundException(1L));
        Theme theme = themeRepository.findById(1L)
                .orElseThrow(() -> new ThemeNotFoundException(1L));
        Reservation reservation = Reservation.create(
                name,
                date,
                reservationTime,
                theme
        );

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
        ReservationTime reservationTime = reservationTimeRepository.findById(1L)
                .orElseThrow(() -> new ReservationTimeNotFoundException(1L));
        Theme theme = themeRepository.findById(1L)
                .orElseThrow(() -> new ThemeNotFoundException(1L));
        Reservation reservation = Reservation.create(
                name,
                date,
                reservationTime,
                theme
        );

        Reservation savedReservation = reservationRepository.save(reservation);
        List<Reservation> reservations = reservationRepository.findAll();

        assertThat(reservations).containsExactly(savedReservation);
    }

    @Test
    void 예약을_취소하는_테스트() {
        String name = "봉구스";
        LocalDate date = LocalDate.of(2026, 5, 6);
        ReservationTime reservationTime = reservationTimeRepository.findById(1L)
                .orElseThrow(() -> new ReservationTimeNotFoundException(1L));
        Theme theme = themeRepository.findById(1L)
                .orElseThrow(() -> new ThemeNotFoundException(1L));
        Reservation reservation = Reservation.create(
                name,
                date,
                reservationTime,
                theme
        );

        Reservation savedReservation = reservationRepository.save(reservation);
        reservationRepository.deleteById(savedReservation.getId());

        List<Reservation> reservations = reservationRepository.findAll();
        assertThat(reservations).doesNotContain(savedReservation);
    }

}
