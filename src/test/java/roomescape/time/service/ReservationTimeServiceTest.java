package roomescape.time.service;

import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.exception.DataExistException;
import roomescape.time.repository.H2ReservationTimeRepository;
import roomescape.time.repository.ReservationTimeRepository;

@JdbcTest
class ReservationTimeServiceTest {

    @Autowired
    private ReservationTimeService reservationTimeService;

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

    @TestConfiguration
    static class TestConfig {

        @Bean
        public ReservationTimeRepository reservationTimeRepository(
                final JdbcTemplate jdbcTemplate
        ) {
            return new H2ReservationTimeRepository(jdbcTemplate);
        }

        @Bean
        public ReservationTimeService reservationTimeService(
                final ReservationTimeRepository reservationTimeRepository
        ) {
            return new ReservationTimeService(reservationTimeRepository);
        }
    }
}