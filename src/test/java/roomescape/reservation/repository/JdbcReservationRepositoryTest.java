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

    private static final String DEFAULT_RESERVATION_NAME = "밀란";
    private static final String OTHER_RESERVATION_NAME = "봉구스";
    private static final String DEFAULT_THEME_NAME = "테마";
    private static final LocalDate DEFAULT_RESERVATION_DATE = LocalDate.of(2026, 5, 10);
    private static final LocalDate NEXT_RESERVATION_DATE = LocalDate.of(2026, 5, 11);
    private static final LocalTime DEFAULT_START_AT = LocalTime.of(10, 0);
    private static final LocalTime UPDATED_START_AT = LocalTime.of(11, 0);

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Test
    void 예약을_저장하는_테스트() {
        // given
        ReservationTime reservationTime = reservationTimeRepository.save(reservationTime(DEFAULT_START_AT));
        Theme theme = themeRepository.save(theme(DEFAULT_THEME_NAME));
        Reservation reservation = reservation(
                DEFAULT_RESERVATION_NAME,
                DEFAULT_RESERVATION_DATE,
                reservationTime,
                theme
        );

        // when
        Reservation savedReservation = reservationRepository.save(reservation);

        // then
        assertThat(savedReservation.getId()).isPositive();
        assertThat(savedReservation.getName()).isEqualTo(DEFAULT_RESERVATION_NAME);
        assertThat(savedReservation.getDate()).isEqualTo(DEFAULT_RESERVATION_DATE);
        assertThat(savedReservation.getTime()).isEqualTo(reservationTime);
        assertThat(savedReservation.getTheme()).isEqualTo(theme);
    }

    @Test
    void 예약을_수정하는_테스트() {
        // given
        ReservationTime reservationTime1 = reservationTimeRepository.save(reservationTime(DEFAULT_START_AT));
        Theme theme = themeRepository.save(theme(DEFAULT_THEME_NAME));
        Reservation reservation = reservation(
                DEFAULT_RESERVATION_NAME,
                DEFAULT_RESERVATION_DATE,
                reservationTime1,
                theme
        );
        Reservation savedReservation = reservationRepository.save(reservation);

        ReservationTime reservationTime2 = reservationTimeRepository.save(reservationTime(UPDATED_START_AT));

        // when
        Reservation updatedReservation = reservationRepository.update(
                savedReservation,
                NEXT_RESERVATION_DATE,
                reservationTime2
        );

        // then
        assertThat(updatedReservation.getId()).isPositive();
        assertThat(updatedReservation.getName()).isEqualTo(DEFAULT_RESERVATION_NAME);
        assertThat(updatedReservation.getDate()).isEqualTo(NEXT_RESERVATION_DATE);
        assertThat(updatedReservation.getTime()).isEqualTo(reservationTime2);
        assertThat(updatedReservation.getTheme()).isEqualTo(theme);
    }

    @Test
    void 모든_예약을_조회하는_테스트() {
        // given
        ReservationTime reservationTime = reservationTimeRepository.save(reservationTime(DEFAULT_START_AT));
        Theme theme = themeRepository.save(theme(DEFAULT_THEME_NAME));
        Reservation reservation = reservation(
                DEFAULT_RESERVATION_NAME,
                DEFAULT_RESERVATION_DATE,
                reservationTime,
                theme
        );

        Reservation savedReservation = reservationRepository.save(reservation);

        // when
        List<Reservation> reservations = reservationRepository.findAll();

        // then
        assertThat(reservations).containsExactly(savedReservation);
    }

    @Test
    void 예약을_취소하는_테스트() {
        // given
        ReservationTime reservationTime = reservationTimeRepository.save(reservationTime(DEFAULT_START_AT));
        Theme theme = themeRepository.save(theme(DEFAULT_THEME_NAME));
        Reservation reservation = reservation(
                DEFAULT_RESERVATION_NAME,
                DEFAULT_RESERVATION_DATE,
                reservationTime,
                theme
        );

        Reservation savedReservation = reservationRepository.save(reservation);

        // when
        reservationRepository.deleteById(savedReservation.getId());

        // then
        List<Reservation> reservations = reservationRepository.findAll();
        assertThat(reservations).doesNotContain(savedReservation);
    }

    @Test
    void 이름으로_예약들을_조회하는_테스트() {
        // given
        ReservationTime reservationTime = reservationTimeRepository.save(reservationTime(DEFAULT_START_AT));
        Theme theme = themeRepository.save(theme(DEFAULT_THEME_NAME));
        Reservation reservation = reservation(
                OTHER_RESERVATION_NAME,
                DEFAULT_RESERVATION_DATE,
                reservationTime,
                theme);
        Reservation savedReservation = reservationRepository.save(reservation);

        // when
        List<Reservation> reservations = reservationRepository.findAllByName(OTHER_RESERVATION_NAME);

        // then
        assertThat(reservations).contains(savedReservation);
    }

}
