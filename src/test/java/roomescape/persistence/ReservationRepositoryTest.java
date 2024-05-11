package roomescape.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.reservation.Name;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.reservation.ReservationRepository;
import roomescape.domain.reservation.ReservationTime;
import roomescape.domain.reservation.ReservationTimeRepository;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
class ReservationRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ReservationRepository reservationRepository;
    private ReservationTimeRepository reservationTimeRepository;
    private ThemeRepository themeRepository;

    @BeforeEach
    void setUp() {
        reservationRepository = new H2ReservationRepository(jdbcTemplate, jdbcTemplate.getDataSource());
        reservationTimeRepository = new H2ReservationTimeRepository(jdbcTemplate, jdbcTemplate.getDataSource());
        themeRepository = new H2ThemeRepository(jdbcTemplate, jdbcTemplate.getDataSource());
    }

    @DisplayName("예약을 저장한다")
    @Test
    void when_saveReservation_then_saved() {
        // given
        ReservationTime savedReservationTime = reservationTimeRepository.save(Fixture.reservationTime);
        Theme savedTheme = themeRepository.save(Fixture.theme);

        // when
        Reservation reservation = new Reservation("피케이", Fixture.tomorrow, savedReservationTime, savedTheme);
        Reservation savedReservation = reservationRepository.save(reservation);

        // then
        assertAll(
                () -> assertThat(savedReservation.getId()).isNotNull(),
                () -> assertThat(savedReservation.getName()).isEqualTo(new Name("피케이")),
                () -> assertThat(savedReservation.getDate()).isEqualTo(new ReservationDate(Fixture.tomorrow)),
                () -> assertThat(savedReservation.getTime()).isEqualTo(savedReservationTime),
                () -> assertThat(savedReservation.getTheme()).isEqualTo(savedTheme)
        );
    }

    @DisplayName("존재하지 않는 테마와 시간에 예약하면 예외가 발생한다")
    @Test
    void when_saveReservationWithNonExistentThemeAndTime_then_throwException() {
        // when, then
        assertThatThrownBy(() -> {
            Reservation reservation = new Reservation("피케이", Fixture.tomorrow, Fixture.unkownReservationTime,
                    Fixture.unkownTheme);
            reservationRepository.save(reservation);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 정보가 올바르지 않습니다.");
    }

    @DisplayName("모든 예약을 조회한다")
    @Test
    void when_findAllReservations_then_returnAllReservations() {
        // given
        ReservationTime savedReservationTime = reservationTimeRepository.save(Fixture.reservationTime);
        Theme savedTheme = themeRepository.save(Fixture.theme);
        for (int i = 0; i < 3; i++) {
            Reservation reservation = new Reservation("피케이", LocalDate.now().plusDays(i), savedReservationTime,
                    savedTheme);
            reservationRepository.save(reservation);
        }

        // when, then
        assertThat(reservationRepository.findAll())
                .hasSize(3);
    }

    @DisplayName("날짜와 테마로 예약을 조회한다")
    @Test
    void when_findReservationByDateAndTheme_then_returnCorrespondReservations() {
        // given
        ReservationTime savedReservationTime = reservationTimeRepository.save(Fixture.reservationTime);
        Theme savedTheme = themeRepository.save(Fixture.theme);
        for (int i = 1; i < 4; i++) {
            Reservation reservation = new Reservation("피케이", LocalDate.now().plusDays(i), savedReservationTime,
                    savedTheme);
            reservationRepository.save(reservation);
        }

        // when
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        long themeId = savedTheme.getId();
        List<Reservation> reservations = reservationRepository.findByDateAndThemeId(tomorrow, themeId);

        // then
        assertThat(reservations)
                .hasSize(1);
    }

    @DisplayName("기간 내의 예약을 조회한다")
    @Test
    void when_findReservationByPeriod_then_returnCorrespondReservations() {
        // given
        ReservationTime savedReservationTime = reservationTimeRepository.save(Fixture.reservationTime);
        Theme savedTheme = themeRepository.save(Fixture.theme);
        for (int i = 1; i < 5; i++) {
            Reservation reservation = new Reservation("피케이", LocalDate.now().plusDays(i), savedReservationTime,
                    savedTheme);
            reservationRepository.save(reservation);
        }

        // when
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(3);
        var reservations = reservationRepository.findByPeriod(startDate, endDate);

        // then
        assertThat(reservations)
                .hasSize(3);
    }

    @DisplayName("예약을 삭제한다")
    @Test
    void when_deleteReservation_then_deleted() {
        // given
        ReservationTime savedReservationTime = reservationTimeRepository.save(Fixture.reservationTime);
        Theme savedTheme = themeRepository.save(Fixture.theme);
        Reservation reservation = new Reservation("피케이", Fixture.tomorrow, savedReservationTime, savedTheme);
        Reservation savedReservation = reservationRepository.save(reservation);

        // when
        reservationRepository.deleteById(savedReservation.getId());

        // then
        assertThat(reservationRepository.findAll())
                .isEmpty();
    }

    private static class Fixture {
        public static final Theme theme = new Theme("테마", "테마 설명", "https://1.jpg");
        public static final ReservationTime reservationTime = new ReservationTime(LocalTime.of(10, 0));
        public static final LocalDate tomorrow = LocalDate.now().plusDays(1);

        public static final Theme unkownTheme = new Theme(100L, "테마", "테마 설명", "https://1.jpg");
        public static final ReservationTime unkownReservationTime = new ReservationTime(100L, LocalTime.of(10, 0));
    }
}
