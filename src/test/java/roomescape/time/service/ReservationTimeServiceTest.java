package roomescape.time.service;

import java.time.LocalDate;
import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.exception.DataExistException;
import roomescape.exception.DataNotFoundException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.H2ReservationRepository;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.H2ReservationTimeRepository;
import roomescape.time.repository.ReservationTimeRepository;

@JdbcTest
class ReservationTimeServiceTest {

    @Autowired
    private ReservationTimeService reservationTimeService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    void 예약시간을_추가한다() {
        // given
        final LocalTime startAt = LocalTime.of(19,55);

        // when & then
        Assertions.assertThatCode(() -> reservationTimeService.save(startAt))
                .doesNotThrowAnyException();
    }

    @Test
    void 이미_존재하는_예약시간을_추가하면_예외가_발생한다() {
        // given
        final LocalTime startAt = LocalTime.of(19,55);
        reservationTimeService.save(startAt);

        // when & then
        Assertions.assertThatThrownBy(() -> reservationTimeService.save(startAt))
                .isInstanceOf(DataExistException.class);
    }

    @Test
    void 예약시간을_삭제한다() {
        // given
        final LocalTime startAt = LocalTime.of(20,28);
        final Long id = reservationTimeService.save(startAt);

        // when & then
        Assertions.assertThatCode(() -> reservationTimeService.deleteById(id))
                .doesNotThrowAnyException();
    }

    @Test
    void 삭제할_예약시간이_없으면_예외가_발생한다() {
        // given
        final Long id = 1L;

        // when & then
        Assertions.assertThatThrownBy(() -> reservationTimeService.deleteById(id))
                .isInstanceOf(DataNotFoundException.class);
    }

    @Test
    void 삭제할_예약시간을_사용하고있는_예약정보가_있으면_예외가_발생한다() {
        // given
        final LocalTime startAt = LocalTime.of(20,28);
        final Long savedTimeId = reservationTimeService.save(startAt);
        final ReservationTime reservationTime = reservationTimeService.getById(savedTimeId);
        final Reservation reservation = new Reservation(
                "우가",
                LocalDate.of(2025,4,29),
                reservationTime
        );
        reservationRepository.save(reservation);

        // when & then
        Assertions.assertThatThrownBy(() -> reservationTimeService.deleteById(savedTimeId))
                .isInstanceOf(DataExistException.class);
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public ReservationTimeRepository reservationTimeRepository(
                final JdbcTemplate jdbcTemplate
        ) {
            return new H2ReservationTimeRepository(jdbcTemplate);
        }

        @Bean
        public ReservationRepository reservationRepository(
                final JdbcTemplate jdbcTemplate
        ) {
            return new H2ReservationRepository(jdbcTemplate);
        }

        @Bean
        public ReservationTimeService reservationTimeService(
                final ReservationTimeRepository reservationTimeRepository,
                final ReservationRepository reservationRepository
        ) {
            return new ReservationTimeService(reservationTimeRepository, reservationRepository);
        }
    }
}