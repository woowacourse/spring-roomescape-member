package roomescape.reservation.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.exception.ReservationDuplicatedException;
import roomescape.reservation.exception.ReservationNotFoundException;
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
    void 이름으로_예약을_조회하는_테스트() {
        LocalDate date = LocalDate.of(2099, 5, 6);
        ReservationTime reservationTime1 = reservationTimeRepository.findById(1L)
                .orElseThrow(() -> new ReservationTimeNotFoundException(1L));
        ReservationTime reservationTime2 = reservationTimeRepository.findById(2L)
                .orElseThrow(() -> new ReservationTimeNotFoundException(2L));
        Theme theme = themeRepository.findById(1L)
                .orElseThrow(() -> new ThemeNotFoundException(1L));

        Reservation reservation1 = reservationRepository.save(Reservation.of("봉구스", date, reservationTime1, theme));
        Reservation reservation2 = reservationRepository.save(Reservation.of("봉구스", date, reservationTime2, theme));
        reservationRepository.save(Reservation.of("밀란", date, reservationTime1, themeRepository.findById(2L)
                .orElseThrow(() -> new ThemeNotFoundException(2L))));

        List<Reservation> reservations = reservationRepository.findByName("봉구스");

        assertThat(reservations).containsExactly(reservation2, reservation1);
    }

    @Test
    void 예약을_수정하는_테스트() {
        LocalDate date = LocalDate.of(2099, 5, 6);
        ReservationTime reservationTime1 = reservationTimeRepository.findById(1L)
                .orElseThrow(() -> new ReservationTimeNotFoundException(1L));
        ReservationTime reservationTime2 = reservationTimeRepository.findById(2L)
                .orElseThrow(() -> new ReservationTimeNotFoundException(2L));
        Theme theme = themeRepository.findById(1L)
                .orElseThrow(() -> new ThemeNotFoundException(1L));
        Reservation reservation = reservationRepository.save(Reservation.of("봉구스", date, reservationTime1, theme));

        Reservation updatedReservation = reservationRepository.update(
                Reservation.of(reservation.getId(), "봉구스", LocalDate.of(2099, 5, 7), reservationTime2, theme)
        );

        assertThat(updatedReservation.getDate()).isEqualTo(LocalDate.of(2099, 5, 7));
        assertThat(updatedReservation.getTime()).isEqualTo(reservationTime2);
        assertThat(reservationRepository.findById(reservation.getId())).contains(updatedReservation);
    }

    @Test
    void 없는_예약을_수정하면_에러를_던진다() {
        ReservationTime reservationTime = reservationTimeRepository.findById(1L)
                .orElseThrow(() -> new ReservationTimeNotFoundException(1L));
        Theme theme = themeRepository.findById(1L)
                .orElseThrow(() -> new ThemeNotFoundException(1L));
        Reservation reservation = Reservation.of(999L, "봉구스", LocalDate.of(2099, 5, 6), reservationTime, theme);

        assertThatThrownBy(() -> reservationRepository.update(reservation))
                .isInstanceOf(ReservationNotFoundException.class);
    }

    @Test
    void 이미_예약된_날짜_시간_테마로_수정하면_에러를_던진다() {
        LocalDate date = LocalDate.of(2099, 5, 6);
        ReservationTime reservationTime1 = reservationTimeRepository.findById(1L)
                .orElseThrow(() -> new ReservationTimeNotFoundException(1L));
        ReservationTime reservationTime2 = reservationTimeRepository.findById(2L)
                .orElseThrow(() -> new ReservationTimeNotFoundException(2L));
        Theme theme = themeRepository.findById(1L)
                .orElseThrow(() -> new ThemeNotFoundException(1L));
        reservationRepository.save(Reservation.of("봉구스", date, reservationTime1, theme));
        Reservation reservation = reservationRepository.save(Reservation.of("밀란", date, reservationTime2, theme));
        Reservation duplicatedReservation = Reservation.of(reservation.getId(), "밀란", date, reservationTime1, theme);

        assertThatThrownBy(() -> reservationRepository.update(duplicatedReservation))
                .isInstanceOf(ReservationDuplicatedException.class);
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
