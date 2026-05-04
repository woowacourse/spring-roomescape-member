package roomescape.theme.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.time.domain.ReservationTime;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(JdbcThemeRepository.class)
class JdbcThemeRepositoryTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcThemeRepository repository;

    @Test
    @DisplayName("예약되지 않은 시간만 조회된다")
    void findAvailableTimes() {
        // given
        Long themeId = 1L;
        LocalDate date = LocalDate.of(2025, 1, 1);

        jdbcTemplate.update("insert into reservation_time(id, start_at) values (1, '10:00:00')");
        jdbcTemplate.update("insert into reservation_time(id, start_at) values (2, '11:00:00')");
        jdbcTemplate.update("insert into reservation_time(id, start_at) values (3, '12:00:00')");

        jdbcTemplate.update("insert into theme(id, name, description, thumbnail_url) values (1, '테마', '설명', 'url')");

        jdbcTemplate.update("""
            insert into reservation(name, reservation_date, time_id, theme_id)
            values ('user', ?, 2, ?)
        """, date, themeId);

        // when
        List<ReservationTime> result = repository.findAvailableTimes(themeId, date);

        // then
        assertThat(result).hasSize(2);
        assertThat(result)
                .extracting(ReservationTime::getId)
                .containsExactlyInAnyOrder(1L, 3L);
    }
}
