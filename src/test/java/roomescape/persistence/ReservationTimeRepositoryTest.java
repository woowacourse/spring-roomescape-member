package roomescape.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationRepository;
import roomescape.domain.reservation.ReservationTime;
import roomescape.domain.reservation.ReservationTimeRepository;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
public class ReservationTimeRepositoryTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ReservationTimeRepository reservationTimeRepository;
    private ReservationRepository reservationRepository;
    private ThemeRepository themeRepository;

    @BeforeEach
    void setUp() {
        reservationTimeRepository = new H2ReservationTimeRepository(jdbcTemplate, jdbcTemplate.getDataSource());
        reservationRepository = new H2ReservationRepository(jdbcTemplate, jdbcTemplate.getDataSource());
        themeRepository = new H2ThemeRepository(jdbcTemplate, jdbcTemplate.getDataSource());
    }

    @DisplayName("예약 시간을 저장한다")
    @Test
    void when_saveReservationTime_then_saved() {
        // when
        ReservationTime savedReservationTime = reservationTimeRepository.save(Fixture.reservationTime);

        // then
        assertAll(
                () -> assertThat(savedReservationTime.getId()).isNotNull(),
                () -> assertThat(savedReservationTime.getStartAt()).isEqualTo("10:00")
        );
    }

    @DisplayName("예약 시간을 모두 조회한다")
    @Test
    void when_findAllReservationTimes_then_returnAllReservationTimes() {
        // given
        for (int time = 10; time < 20; time++) {
            ReservationTime reservationTime = new ReservationTime(LocalTime.of(time, 0));
            reservationTimeRepository.save(reservationTime);
        }

        // when, then
        assertThat(reservationTimeRepository.findAll())
                .hasSize(10);
    }

    @DisplayName("예약 시간을 조회한다")
    @Test
    void when_findById_then_returnCorrespondReservationTime() {
        // given
        ReservationTime savedReservationTime = reservationTimeRepository.save(Fixture.reservationTime);

        // when, then
        assertThat(reservationTimeRepository.findById(savedReservationTime.getId()))
                .isPresent();
    }

    @DisplayName("예약 시간이 없는 경우에 조회하면 빈 값을 반환한다")
    @Test
    void when_findById_then_returnEmpty() {
        // when
        Optional<ReservationTime> savedReservationTime = reservationTimeRepository.findById(100L);

        // then
        assertThat(savedReservationTime)
                .isEmpty();
    }

    @DisplayName("예약 시간을 삭제한다")
    @Test
    void when_deleteById_then_deleted() {
        // given
        ReservationTime savedReservationTime = reservationTimeRepository.save(Fixture.reservationTime);

        // when
        reservationTimeRepository.deleteById(savedReservationTime.getId());

        // then
        assertThat(reservationTimeRepository.findAll())
                .isEmpty();
    }

    @DisplayName("존재하지 않는 예약 시간을 삭제할 경우, 예외가 발생하지 않는다")
    @Test
    void when_deleteNotExistReservationTime_then_throwsNothing() {
        // when, then
        assertThatCode(() -> reservationTimeRepository.deleteById(100L))
                .doesNotThrowAnyException();
    }

    @DisplayName("예약 시간을 삭제할 경우, 참조된 예약이 있으면 예외를 발생한다")
    @Test
    void when_deleteReferencedReservationTime_throw_exception() {
        // given
        ReservationTime savedReservationTime = reservationTimeRepository.save(Fixture.reservationTime);
        Theme savedTheme = themeRepository.save(Fixture.theme);
        Reservation reservation = new Reservation("피케이", LocalDate.now().plusDays(1), savedReservationTime, savedTheme);
        reservationRepository.save(reservation);

        // when, then
        assertThatThrownBy(() -> reservationTimeRepository.deleteById(savedReservationTime.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("해당 시간은 예약이 존재하여 삭제할 수 없습니다. ");
    }

    private static class Fixture {
        private static final ReservationTime reservationTime = new ReservationTime(LocalTime.of(10, 0));
        private static final Theme theme = new Theme("테마", "테마 설명", "https://1.jpg");
    }
}
