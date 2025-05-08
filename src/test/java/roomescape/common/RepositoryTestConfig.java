package roomescape.common;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import roomescape.integration.fixture.MemberDbFixture;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.integration.fixture.ReservationDbFixture;
import roomescape.integration.fixture.ReservationTimeDbFixture;
import roomescape.integration.fixture.ThemeDbFixture;

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

    @Bean
    public MemberDbFixture memberDbFixture(JdbcTemplate jdbcTemplate) {
        return new MemberDbFixture(jdbcTemplate);
    }

    @Bean
    @Primary
    public PasswordEncoder passwordTestEncoder() {
        return new BCryptPasswordEncoder();
    }
}
