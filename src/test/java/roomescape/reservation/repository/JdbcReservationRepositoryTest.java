package roomescape.reservation.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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
        LocalDate date = LocalDate.of(2099, 5, 6);
        ReservationTime reservationTime = reservationTimeRepository.findById(1L)
                .orElseThrow(() -> new ReservationTimeNotFoundException(1L));
        Theme theme = themeRepository.findById(1L)
                .orElseThrow(() -> new ThemeNotFoundException(1L));

        Reservation reservation = reservationRepository.save(Reservation.of(name, date, reservationTime, theme));
        assertThat(reservation.getId()).isPositive();
        assertThat(reservation.getName()).isEqualTo(name);
        assertThat(reservation.getDate()).isEqualTo(date);
        assertThat(reservation.getTime()).isEqualTo(reservationTime);
        assertThat(reservation.getTheme()).isEqualTo(theme);
    }

    @Test
    void 모든_예약을_조회하는_테스트() {
        String name = "봉구스";
        LocalDate date = LocalDate.of(2099, 5, 6);
        ReservationTime reservationTime = reservationTimeRepository.findById(1L)
                .orElseThrow(() -> new ReservationTimeNotFoundException(1L));
        Theme theme = themeRepository.findById(1L)
                .orElseThrow(() -> new ThemeNotFoundException(1L));

        Reservation reservation = reservationRepository.save(Reservation.of(name, date, reservationTime, theme));
        List<Reservation> reservations = reservationRepository.findAll();

        assertThat(reservations).containsExactly(reservation);
    }

    @Test
    void 예약을_취소하는_테스트() {
        String name = "봉구스";
        LocalDate date = LocalDate.of(2099, 5, 6);
        ReservationTime reservationTime = reservationTimeRepository.findById(1L)
                .orElseThrow(() -> new ReservationTimeNotFoundException(1L));
        Theme theme = themeRepository.findById(1L)
                .orElseThrow(() -> new ThemeNotFoundException(1L));

        Reservation reservation = reservationRepository.save(Reservation.of(name, date, reservationTime, theme));
        reservationRepository.deleteById(reservation.getId());

        List<Reservation> reservations = reservationRepository.findAll();
        assertThat(reservations).doesNotContain(reservation);
    }

    @Test
    void 있는_예약을_날짜_시간_테마로_조회하는_경우_예약을_반환한다() {
        String name = "봉구스";
        LocalDate date = LocalDate.of(2099, 5, 6);
        long time_id = 1L;
        ReservationTime reservationTime = reservationTimeRepository.findById(time_id)
                .orElseThrow(() -> new ReservationTimeNotFoundException(time_id));
        long theme_id = 1L;
        Theme theme = themeRepository.findById(theme_id)
                .orElseThrow(() -> new ThemeNotFoundException(theme_id));

        reservationRepository.save(Reservation.of(name, date, reservationTime, theme));
        Optional<Reservation> reservation = reservationRepository.findByDateAndTimeIdAndThemeId(date,
                time_id,
                theme_id);

        assertThat(reservation).isPresent();
    }

    @Test
    void 없는_예약을_날짜_시간_테마로_조회하는_경우_Empty를_반환한다() {
        LocalDate date = LocalDate.of(2099, 5, 6);
        long time_id = 1L;
        long theme_id = 1L;

        Optional<Reservation> reservation = reservationRepository.findByDateAndTimeIdAndThemeId(date,
                time_id,
                theme_id);

        assertThat(reservation).isEmpty();
    }
}
