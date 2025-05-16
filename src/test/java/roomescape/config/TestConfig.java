package roomescape.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.member.repository.JdbcMemberRepository;
import roomescape.member.repository.MemberRepository;
import roomescape.reservation.repository.JDBCReservationRepository;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.service.ReservationService;
import roomescape.reservationtime.repository.JDBCReservationTimeRepository;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.repository.JDBCThemeRepository;
import roomescape.theme.repository.ThemeRepository;

@Configuration
public class TestConfig {

    @Bean
    public ReservationTimeRepository reservationTimeRepository(JdbcTemplate jdbcTemplate) {
        return new JDBCReservationTimeRepository(jdbcTemplate);
    }

    @Bean
    public ThemeRepository themeRepository(JdbcTemplate jdbcTemplate) {
        return new JDBCThemeRepository(jdbcTemplate);
    }

    @Bean
    public MemberRepository memberRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcMemberRepository(jdbcTemplate);
    }

    @Bean
    public ReservationRepository reservationRepository(JdbcTemplate jdbcTemplate) {
        return new JDBCReservationRepository(jdbcTemplate);
    }

    @Bean
    public ReservationService reservationService(
            ReservationRepository reservationRepository,
            ReservationTimeRepository reservationTimeRepository,
            ThemeRepository themeRepository,
            MemberRepository memberRepository) {
        return new ReservationService(reservationRepository, reservationTimeRepository,
                themeRepository, memberRepository);
    }
}
