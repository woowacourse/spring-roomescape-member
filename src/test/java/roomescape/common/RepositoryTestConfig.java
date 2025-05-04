package roomescape.common;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.repository.ReservationTimeRepository;
import roomescape.unit.fixture.ReservationDbFixture;
import roomescape.unit.fixture.ReservationTimeDbFixture;
import roomescape.unit.fixture.ThemeDbFixture;

@TestConfiguration
public class RepositoryTestConfig {

    @Bean
    public ThemeRepository themeRepository(JdbcTemplate jdbcTemplate) {
        return new ThemeRepository(jdbcTemplate);
    }

    @Bean
    public ReservationRepository reservationRepository(JdbcTemplate jdbcTemplate) {
        return new ReservationRepository(jdbcTemplate);
    }

    @Bean
    public ReservationTimeRepository reservationTimeRepository(JdbcTemplate jdbcTemplate) {
        return new ReservationTimeRepository(jdbcTemplate);
    }

    @Bean
    public ThemeDbFixture themeDbFixture(JdbcTemplate jdbcTemplate) {
        return new ThemeDbFixture(jdbcTemplate);
    }

    @Bean
    public ReservationDbFixture reservationDbFixture(JdbcTemplate jdbcTemplate) {
        return new ReservationDbFixture(jdbcTemplate);
    }

    @Bean
    public ReservationTimeDbFixture reservationTimeDbFixture(JdbcTemplate jdbcTemplate) {
        return new ReservationTimeDbFixture(jdbcTemplate);
    }
}
