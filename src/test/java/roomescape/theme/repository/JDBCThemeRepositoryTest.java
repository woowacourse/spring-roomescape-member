package roomescape.theme.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.theme.domain.Theme;

@JdbcTest
class JDBCThemeRepositoryTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ThemeRepository themeRepository;

    @BeforeEach
    void setUp() {
        themeRepository = new JDBCThemeRepository(jdbcTemplate);
    }

    @Test
    void findTop10PopularThemesWithinLastWeek_shouldReturnCorrectly() {
        LocalDate baseDate = LocalDate.of(2025, 5, 1);
        LocalDate startDate = baseDate.minusDays(7);
        LocalDate endDate = baseDate.minusDays(1);

        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at) VALUES (100, '10:00')");
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at) VALUES (101, '11:00')");
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at) VALUES (102, '12:00')");
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at) VALUES (103, '13:00')");
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at) VALUES (104, '14:00')");
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at) VALUES (200, '15:00')");
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at) VALUES (201, '16:00')");
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at) VALUES (202, '17:00')");

        jdbcTemplate.update(
                "INSERT INTO theme (id, name, description, thumbnail) VALUES (1, '추리', '셜록 with Danny', 'image/thumbnail.png')");
        jdbcTemplate.update(
                "INSERT INTO theme (id, name, description, thumbnail) VALUES (2, '공포', '어둠 속의 비명', 'image/thumbnail.png')");
        jdbcTemplate.update(
                "INSERT INTO theme (id, name, description, thumbnail) VALUES (3, '모험', '잃어버린 도시', 'image/thumbnail.png')");

        for (int i = 0; i < 5; i++) {
            jdbcTemplate.update(
                    "INSERT INTO reservation (id, date, member_id, time_id, theme_id) VALUES (?, ?, ?, ?, ?)",
                    (long) (i + 1), startDate.plusDays(i).toString(), i, 100 + i, 1
            );
        }

        for (int i = 0; i < 3; i++) {
            jdbcTemplate.update(
                    "INSERT INTO reservation (id, date,member_id, time_id, theme_id) VALUES (?, ?, ?, ?, ?)",
                    (long) (i + 10), startDate.plusDays(i).toString(), i, 200 + i, 2
            );
        }

        List<Theme> result = themeRepository.findTop10PopularThemesWithinLastWeek(baseDate);

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }
}
