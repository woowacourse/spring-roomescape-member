package roomescape.reservation.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.config.TestFixture.futureReservationDate;
import static roomescape.config.TestFixture.nextReservationDate;
import static roomescape.config.TestFixture.reservation;
import static roomescape.config.TestFixture.reservationTime;
import static roomescape.config.TestFixture.theme;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.config.FixedClockTestConfig;
import roomescape.reservation.entity.Reservation;
import roomescape.reservationtime.entity.ReservationTime;
import roomescape.reservationtime.repository.JdbcReservationTimeRepository;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.entity.Theme;
import roomescape.theme.repository.JdbcThemeRepository;
import roomescape.theme.repository.ThemeRepository;

@JdbcTest
@Import({
        JdbcReservationRepository.class,
        JdbcReservationTimeRepository.class,
        JdbcThemeRepository.class,
        FixedClockTestConfig.class
})
class JdbcReservationRepositoryTest {

    private static final String DEFAULT_RESERVATION_NAME = "밀란";
    private static final String OTHER_RESERVATION_NAME = "봉구스";
    private static final String DEFAULT_THEME_NAME = "테마";
    private static final LocalTime DEFAULT_START_AT = LocalTime.of(10, 0);
    private static final LocalTime UPDATED_START_AT = LocalTime.of(11, 0);

    @Autowired
    private Clock clock;

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
        LocalDate reservationDate = futureReservationDate(clock);
        Reservation reservation = reservation(
                DEFAULT_RESERVATION_NAME,
                reservationDate,
                reservationTime,
                theme
        );

        // when
        Reservation savedReservation = reservationRepository.save(reservation);

        // then
        assertThat(savedReservation.getId()).isPositive();
        assertThat(savedReservation.getName()).isEqualTo(DEFAULT_RESERVATION_NAME);
        assertThat(savedReservation.getDate()).isEqualTo(reservationDate);
        assertThat(savedReservation.getTime()).isEqualTo(reservationTime);
        assertThat(savedReservation.getTheme()).isEqualTo(theme);
    }

    @Test
    void 예약을_수정하는_테스트() {
        // given
        ReservationTime reservationTime1 = reservationTimeRepository.save(reservationTime(DEFAULT_START_AT));
        Theme theme = themeRepository.save(theme(DEFAULT_THEME_NAME));
        LocalDate reservationDate = futureReservationDate(clock);
        LocalDate updateDate = nextReservationDate(clock);
        Reservation reservation = reservation(
                DEFAULT_RESERVATION_NAME,
                reservationDate,
                reservationTime1,
                theme
        );
        Reservation savedReservation = reservationRepository.save(reservation);

        ReservationTime reservationTime2 = reservationTimeRepository.save(reservationTime(UPDATED_START_AT));

        // when
        Reservation updatedReservation = reservationRepository.update(
                savedReservation,
                updateDate,
                reservationTime2
        );

        // then
        assertThat(updatedReservation.getId()).isPositive();
        assertThat(updatedReservation.getName()).isEqualTo(DEFAULT_RESERVATION_NAME);
        assertThat(updatedReservation.getDate()).isEqualTo(updateDate);
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
                futureReservationDate(clock),
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
                futureReservationDate(clock),
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
                futureReservationDate(clock),
                reservationTime,
                theme);
        Reservation savedReservation = reservationRepository.save(reservation);

        // when
        List<Reservation> reservations = reservationRepository.findAllByName(OTHER_RESERVATION_NAME);

        // then
        assertThat(reservations).contains(savedReservation);
    }

}
